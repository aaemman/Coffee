Coffee
======


Build Instructions:


   * There are no special build instructions for ;this particular application, you can simply load the code into Android Studio and hit the build button to run the application on a connected device. (Note that you may need an internet connection in order to build the project for the first time in order to get and load the appropriate dependencies into the apk).

Design and Technical Decisions:


   * I decided to utilize the ActionBarSherlock library in order to implement the custom action bar as it is a library which allows for very robust usage but also facilitates custom action bar layouts quite easily.



   * I decided to add persistent storage to the application in order to allow for minimal offline usage.



   * I decided not to persistently store the pictures associated to the individual posts because:
   * 
      * I didn’t believe them to be integral to the offline usage of the application 
      * Allows the application to save a significant about of disk space
   * I decided to add in a bounce animation for the List View loading progress bar as it:
   * 
      * Is a smoother more natural transition which is inline with the android design guidelines 



   * I decided to add a fade animation to all components of the coffee type detail page as it:
   * 
      * Gives a better user experience 
      * Matches with the fade in animation done on the pictures in the list view page (done by default by the spiceList module of RoboSpice).
      

   * I decided to add in basic messages in the application to notify teh user when there was a problem fulfilling any requests (ex. no network connection). I chose to do this because:
      * It is a better user experience, without them users could mistakenly think/feel that it is the application is broken if they were to get stuck waiting for a network connection, or if something else were to go wrong.


Known Bugs:

Jump-y Scrolling:
     Bug: 


   * When scrolling up in the List view page; the UI seems to jump rather aggressively when loading in new images

     Reason:


   * Because there is no standard size for the images, the list item’s size can only be determined once its image has been loaded from the provided URL

   * This causes the list items to resize when scrolling which causes the ‘jumping’ effect

     Solution:


  1. Send the dimensions of the image in the api response so that the list items sizes can be set accordingly when loading. (Bad Solution)
  2. Standardize the size of all images; this way a set sized pending image can be used in place of the actual image when loading to maintain list item size. (My Recommendation - Better Experience, more visually appealing)


Temporarily Disabled Share Button:


     Bug:


   * The share button is disabled in the coffee type detail page until all of that page’s data has been loaded.

     Reason:


   * This is done on purpose.

   * Because the description given for each item in the coffee index request is incomplete, the full description from the coffee show request is necessary to share properly

     Solution:


   * Send the full description in the index request. (Bad Solution - can become a large amount of data if application is expanded to have many more posts/entries)
   * Leave it how it is for now. (My Recommendation - Although not a perfect solution, it is not a critical issue and I don’t believe that it will effect the majority of users).

Tablet Optimization:
     
     Bug:


   * Although this application will work on a tablet, it is not optimized for larger screens
