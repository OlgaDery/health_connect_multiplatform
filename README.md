# health_connect_multiplatform
KMM/KMP sample app using coroutines, flow, viewmodel, SQLDelight as a local data storage), platform-specific location APIs and health data repositories. 

Business Requirements:
- App runs on both Android and iOS platforms.
- App reads desired health data from HealthKit on iOS and HealthConnect on Android (if available) every 3 minutes in the background and foreground.
- App receives the phone location updates or reads the current location every 1-5 minutes (depending on the API).
- App stores both health and location data in the local database unless the user erases it.
- User is able to enter the desired health metrics and the health record is created out of it. There is a map displayed on the same screen to let the user to select the location associated with the record.
- There is a screen to display all the health records in a list. If there is no location associated with a health record, we want to display the phone’s location instead if the timestamp is close.
- There is a dedicated screen with a slider. It has 3 slides containing the averages or sums of health metrics the app is collecting.
- There is a screen with a calendar view. We highlight the dates of the current month the user has associated records with. On the same screen, there is a button to erase all the health and location records.
- The app handles rotation, switch between background and foreground and configuration changes.

Technical Requirements:
- All the code except the app’s entry points goes to the “shared” module, including UI and navigation. 
- The app architecture should follow the principles of clear code. All the layers starting from view model (if in use) and down to the layer where the platform specific components are being used should be unit tested.
- Using Kotlin best practices to handle asynchronous calls and data flow.
- Options for adding UI tests should be investigated.




