/*
BEACON SERVICE 
Watches Firebase DB and will dispatch the messages accordingly via "topics".
All individual phones subscribed to a given topic will receive the message. 
 
*/

var firebase = require('firebase');
var request = require('request');
var API_KEY = "AIzaSyBLHE922C2cB3gNOVFOGVwkAciQyUO1GoA"; //Firebase Cloud Server API key

firebase.initializeApp({
  serviceAccount: "Beacon-1628ff5e63ca.json",
  databaseURL: "https://beacon-2bfb0.firebaseio.com/"
});

ref = firebase.database().ref();

function listenForNotificationRequests() {
  console.log("Beacon Service Running!");
  
  //Register the different requests here...
  var notificationRequests = ref.child('notificationRequest');
  var beaconInvitationRequests = ref.child('beaconRequest');
  
  notificationRequests.on('child_added', function(requestSnapshot) {
    var request = requestSnapshot.val();
    console.log("Notification Message Received");
    sendNotificationToUser(
      request.username, 
      request.message
    );
  }, function(error) {
    console.error(error);
  });
  
  beaconInvitationRequests.on('child_added', function(requestSnapshot) {
  	var request = requestSnapshot.val();
  	console.log("Beacon Invitation Received");
  	sendNotificationToUser(
  		request.username,
  		request.message
  	);
  }, function(error) {
  	console.error(error);
  });
  
};

function sendNotificationToUser(username, message) {
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
    }
  });
}

// start listening
listenForNotificationRequests();
