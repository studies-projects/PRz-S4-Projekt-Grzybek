const functions = require('firebase-functions');
var admin = require('firebase-admin');
var serviceAccount = require("./prz-grzybek-firebase-adminsdk-wo9xo-42fa0b73f7.json");
admin.initializeApp({
    credential: admin.credential.cert(serviceAccount)
});

exports.getCustomToken = functions.https.onCall(data => {
    // Read 64base from APP
    var base64FromApp = data.response;
    console.log("Data from APP:", JSON.stringify(data));

    const myJSON = baseToJSON(base64FromApp);
    return createFirebaseAccountCAS(myJSON);
});

exports.notifyNewEvent = functions.firestore
    .document('/Events/{eventID}')
    .onCreate((snap, context)=>{
        const newEvent = snap.data();
        const name = newEvent['Name'];
        const notificationBody = newEvent['Desc'];
        const owner = newEvent['Owner'];
        const eventDate = newEvent['DateStart'];

        const payload = {
            notification: {
                title: "Nowe wydarzenie na grzybku: " + name,
                body: notificationBody,
            },
            topic: "notifications"
        };
        admin.messaging().send(payload)
            .then((response) => {
            // Response is a message ID string.
            console.log('Successfully sent message:', response);
          })
          .catch((error) => {
            console.log('Error sending message:', error);
          });


    })


async function createFirebaseAccountCAS(userJSON) {
    var ref = userJSON["cas:serviceResponse"]["cas:authenticationSuccess"];
    // The UID we'll assign to the user
    const uid = ref["cas:attributes"]["cas:uid"]["_text"];
    console.log("User UID: ", uid);

    var info = {
        displayName: ref["cas:user"]["_text"],
        emailVerified: true,
        email: ref["cas:attributes"]["cas:mail"]["_text"],
        disabled: false
    };
    console.log("UserINFO:", info);

    // Create or update the user account.
    const userCreationTask = admin.auth()
    .updateUser(uid, info)
    .catch(error => {
        // If user does not exist we create it.
        if (error.code === 'auth/user-not-found') {
            info.uid = uid;
            return admin.auth().createUser(info);
        }
        throw error;
    });

    // Information about user which can't be put into admin.auth()
    var aditionalInfo = {
        usos_id: ref["cas:attributes"]["cas:usos_id"]["_text"],
        departmentnumber: ref["cas:attributes"]["cas:departmentnumber"]["_text"],
        employeetype: ref["cas:attributes"]["cas:employeetype"]["_text"]
    };

    // Adding additional information to datebase
    const databaseTask = admin.firestore().doc(`/Users/${uid}`).set(aditionalInfo);

    // Wait for all async task to complete then generate and return a custom auth token.
    await Promise.all([userCreationTask, databaseTask]);
    // Create a Firebase custom auth token.
    const token = await admin.auth().createCustomToken(uid);
    console.log('Created Custom token for UID "', uid, '" Token:', token);
    return token;
}

function baseToJSON(response) {
    const convert = require('xml-js');

    // Parspe respone (base64) to string (XML)
    const decoded = Buffer.from(response, 'base64').toString('ascii');
    console.log("Decoded base64 to string:", decoded);

    // Parse XML to JS Object
    const options = {compact:true, spaces: 4}
    const result = convert.xml2js(decoded, options);

    console.log("Convert XML to JSON: ", JSON.stringify(result, null, 4));

    return result;
}
