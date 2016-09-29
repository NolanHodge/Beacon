var firebase = require('firebase');
var request = require('request');
// NOLAN
var API_KEY = "AIzaSyBLHE922C2cB3gNOVFOGVwkAciQyUO1GoA"; //Firebase Cloud Server API key

firebase.initializeApp({
  serviceAccount: "Beacon-1628ff5e63ca.json",
  databaseURL: "https://beacon-2bfb0.firebaseio.com/"
});

ref = firebase.database().ref();

function listenForNotificationRequests() {
  console.log("Beacon Service Running!");
  var requests = ref.child('notificationRequests');
  requests.on('child_added', function(requestSnapshot) {
    var request = requestSnapshot.val();
    console.log("Message Received");
    sendNotificationToUser(
      request.username, 
      request.message,
      function() {
        requestSnapshot.ref.remove();
      }
    );
  }, function(error) {
    console.error(error);
  });
};

function sendNotificationToUser(username, message, onSuccess) {
  request({
    url: 'https://fcm.googleapis.com/fcm/send',
    method: 'POST',
    headers: {
      'Content-Type' :' application/json',
      'Authorization': 'key='+API_KEY
    },
    body: JSON.stringify({
      notification: {
        title: message
      },
      to : '/topics/user_'+username
    })
  }, function(error, response, body) {
    if (error) { console.error(error); console.log(error); }
    else if (response.statusCode >= 400) { 
      console.error('HTTP Error: '+response.statusCode+' - '+response.statusMessage); 
      console.log('Fuck');
    }
    else {
      onSuccess();
    }
  });
}

// start listening
listenForNotificationRequests();
