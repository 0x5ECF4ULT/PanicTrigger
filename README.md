# PanicTrigger
**DISCLAIMER:** I am NOT responsible for any damage caused by this app! If you come crying to me with tears in your eyes because you lost your job or something I WILL POINT MY FINGER AT YOU AND LAUGH! I am not responsible for ANYTHING! Period.
## Description
PanicTrigger is an Android app which can help you in case of an emergency situation. In case of an emergency you tap on a big red button which causes the app to send out SMS to your preconfigured contacts without comfirmation. So don’t tap on the wrong button ;-)
PanicTrigger runs in the background and listens for incoming SMS this is why it needs the permission to read your SMS. Trust me I won’t read your SMS :)<br/>
If an ingoing SMS contains a specific word AND is from a specific number then an alarm will go off and call the sender of the SMS in one minute. Enough time for you to wake up and realize that the alarm has been triggered.<br/>
Your contact(s) can trigger your alarm with a simple SMS and vice versa. If your opponent doesn’t have this app he will only see an SMS with the text “Panic” (by default) and your current GPS coordinates. This makes this app useful regardless of your contact has this app installed or not.<br/>

If you want to contribute to this project please refer to "How to [contribute](CONTRIBUTING.md)"
## Requirements
 - Your phone
 - A connection to your provider
 - Enough credits for a few SMS
## Features
 - AMOLED theme by default
 - Simple interface
 - Triggers an alarm on your friends phone (He/She needs to have this app too)
 - More features coming soon…
 - Send last known GPS coordinates within trigger message
 - In case there are no contacts configured call emergency services
## Coming soon…
 - GPS tracker (send updated coordinates ever n minutes unless canceled)
 - Root functionality
 - THEMES!!!
## Permissions
- ```android.permission.RECEIVE_SMS``` is needed to receive the keyword for triggering your alarm
- ```android.permission.SEND_SMS``` is needed to send the keyword for triggering their alarm
- ```android.permission.ACCESS_COARSE_LOCATION``` is needed for a quick discovery where you are (I WON'T TRACK YOU)
- ```android.permission.ACCESS_FINE_LOCATION``` if set, try to discover your exact location (I WON'T TRACK YOU)
- ```android.permission.CALL_PHONE``` is needed to call the sender of the "Panic"-SMS
- ```android.permission.CALL_PRIVILEGED``` is needed for calling emergency services
- ```android.permission.READ_CONTACTS``` is needed for picking contacts (Not used yet)
- ```android.permission.INTERNET``` is needed for downloading map tiles (osmdroid)
- ```android.permission.ACCESS_NETWORK_STATE``` (osmdroid)
- ```android.permission.WRITE_EXTERNAL_STORAGE``` is needed for writing to the tile cache (osmdroid)
## Links
 - [License](LICENSE)
 - [How to contribute](CONTRIBUTING.md)
 - [PR template](PULL_REQUEST_TEMPLATE.md)
 
 - [osmdroid](https://github.com/osmdroid/osmdroid)
## Donate
**Bitcoin:** 1EVr5tm2kugffNy3RWPGFoug6X9v3GTxuJ<br/>
**Monero:** 47JuiUvKZXL7by3drpFTxHgv9DWPgj9pP8TMiGqxdZugEAVKUXwok2Hbopdc8qYBnLNuQdR6VptmFiDqByRVMVX3Ra8vw9j
## License
Got to [license](LICENSE)

<!--stackedit_data:
eyJoaXN0b3J5IjpbNzIyMzM3MjU4XX0=
-->