const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.removeOldOffers = functions.https.onRequest((req, res) => {
    console.log("Delete old offers")
    const timeNow = Date.now();
    const messagesRef = admin.database().ref('/offers');
    messagesRef.once('value', (snapshot) => {
        snapshot.forEach((child) => {
            if ((Number(child.val()['endDate'])) <= timeNow) {
                child.ref.set(null);
            }
        });
    });
    return res.status(200).end();
});
