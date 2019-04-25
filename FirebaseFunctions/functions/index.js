const functions = require('firebase-functions');

// Create and Deploy Your First Cloud Functions
// https://firebase.google.com/docs/functions/write-firebase-functions

exports.helloWorld = functions.https.onRequest((request, response) => {
 response.send("Hello from Firebase!");
 console.log('Returned Message');
});

// Create CallAble function
// https://firebase.google.com/docs/functions/callable
// exports.addMessage = functions.https.onCall((data, context) => {
//     const text = data.text;

//     // const uid = context.auth.uid;
//     // const name = context.auth.token.name || null;
//     // const picture =context.auth.token.picture || null;
//     // const email = context.auth.token.email || null;

//     return{
//         "TEST123"
//     }
//     // return {
//     //     firstNumber: firstNumber,
//     //     secondNumber: secondNumber,
//     //     operator: '+',
//     //     operationResult: firstNumber + secondNumber,
//     // }
//     console.log('Returned message');
// });

exports.getCustomToken = functions.https.onRequest((req, res) => {
    res.send("TEST123");
});

exports.test = functions.https.onCall((data) => {
    if(data.manufacturer.lenght > 0) {
        //console.log(data.manufacturer);
        return data.manufacturer.toUpperCase();
    }

//    console.log("Unknown");
    return "Unknown";
});
