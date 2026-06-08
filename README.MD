### How to build an android app
1. Implement your project structure
   a. model : data storage reference i.e
   determines our data properties
   b. navigation : create routes, pathways
   to different screens
   c. UI : user interface screens - different
   interfaces for our users to interact with
- screens : actual components
- theme : colors , fonts to be used by app
2. Adding dependencies required by project
   build.gradle (app module)
3. Define screens and navgraph that will allow navigation to
   various screens
4. Create the splashscreen : loader screen , first screen
   showcased when user opens our app. i.e. startDestination for
   our navgraph
5. Creating the authentication screens i.e. register,
   Login and forgotpassword screens
6. Connecting project to firebase
- create the firebase project @
- console.firebase.google.com
- register the app in firebase using the package name
- download google-services.json config file
- add to your projects /app folder for connection
- enable the products needed  - firebase auth -
- - firebase firestore and data storage
7. Gradle and dependencies configs to add firebase
   to project
- build.gradle.kts (project, app level)

## WIRING THE AUTH PROCESS TO USE FIREBASE
1. Create the data model class to define necessary auth fields
   i.e. user profile
2. Create the authviewmodel class and create necessary auth=
   functionality
   A viemodel is a bridge between our apps interface i.e views and
   its business logic . Designed to prepare , manage and hold UI
   related data so that the screen survives rotation and config
   changes
3. Connect the viemodel functionalities to the interfaces 





