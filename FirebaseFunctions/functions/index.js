const functions = require('firebase-functions');
var admin = require('firebase-admin');
var serviceAccount = require("./prz-grzybek-firebase-adminsdk-wo9xo-42fa0b73f7.json");
admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
    //credential: admin.credential.applicationDefault(),
    //databaseURL: "https://prz-grzybek.firebaseio.com"
});

exports.getCustomToken = functions.https.onRequest((req, res) => {
    res.send("TEST123");
});

exports.test = functions.https.onCall((data) => {
    
    var uid = '225328';
    var my64base = 'CgoKCjxjYXM6c2VydmljZVJlc3BvbnNlIHhtbG5zOmNhcz0naHR0cDovL3d3dy55YWxlLmVkdS90cC9jYXMnPgoJPGNhczphdXRoZW50aWNhdGlvblN1Y2Nlc3M+CgkJPGNhczp1c2VyPjE1ODg0MUBzdHVkLnByei5lZHUucGw8L2Nhczp1c2VyPgoJCQoJPGNhczphdHRyaWJ1dGVzPgoJICAKCSAgICAgIAoJCSAgPGNhczp1aWQ+MTU4ODQxPC9jYXM6dWlkPgoJICAgICAgCgkJICA8Y2FzOm1haWw+MTU4ODQxQHN0dWQucHJ6LmVkdS5wbDwvY2FzOm1haWw+CgkgICAgICAKCQkgIDxjYXM6dXNvc19pZD4yMjUzMjg8L2Nhczp1c29zX2lkPgoJICAgICAgCgkJICA8Y2FzOmVtcGxveWVldHlwZT5zdHVkZW50PC9jYXM6ZW1wbG95ZWV0eXBlPgoJICAgICAgCgkJICA8Y2FzOnJlZ2lzdGVyZWRhZGRyZXNzPjE1ODg0MUBzdHVkLnByei5lZHUucGw8L2NhczpyZWdpc3RlcmVkYWRkcmVzcz4KCSAgICAgIAoJCSAgPGNhczpkZXBhcnRtZW50bnVtYmVyPkVGPC9jYXM6ZGVwYXJ0bWVudG51bWJlcj4KCSAgICAgIAoJICAKCTwvY2FzOmF0dHJpYnV0ZXM+CgogICAgICAgIAogICAgICAgIAoJPC9jYXM6YXV0aGVudGljYXRpb25TdWNjZXNzPgo8L2NhczpzZXJ2aWNlUmVzcG9uc2U+';
    const myJSON = baseToJSON(my64base);

    console.log("Try to get json properties!");
    console.log(myJSON["cas:serviceResponse"]["cas:authenticationSuccess"]["cas:user"]["_text"]);
    var prop = myJSON["cas:serviceResponse"]["cas:authenticationSuccess"];
    console.log(prop["cas:attributes"]["cas:uid"]["_text"]);

    return createFirebaseAccountCAS(uid);
});


async function createFirebaseAccountCAS(studentID) {
    // The UID we'll assign to the user
    const uid = `${studentID}`;
    console.log("User UID: ", uid);

    var test = {
        displayName: "displayName",
        emailVerified: true,
        email: 'user@example.com',
        displayName: 'John Doe',
        photoURL: 'http://www.example.com/12345678/photo.png',
        disabled: false
    };

    // Create or update the user account.
    const userCreationTask = admin.auth()
    .updateUser(uid, test)
    .catch(error => {
        // If user does not exist we create it.
        if (error.code === 'auth/user-not-found') {
            test.uid = uid;
            return admin.auth().createUser(test);
        }
        throw error;
    });

    // Wait for all async task to complete then generate and return a custom auth token.
    await Promise.all([userCreationTask]);
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