# Beacon Service

Service watches Firebase DB and will dispatch the messages accordingly via "topics".
All individual phones subscribed to a given topic will receive a message for that topic.  

# Note: 
You must have the Beacon-1628ff5e63ca.json file in this directory on your local machine in order to run the script. It cannot go onto GitHub.

This is not a local-host service so if more than one person is running this script there may be conflicts. One person should only be running this service at a time.

## To run:
```node BeaconService.js```


NOTE: You must have the Beacon-1628ff5e63ca.json file in this directory on your local machine. It cannot go onto github 
