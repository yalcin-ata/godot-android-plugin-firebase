# Godot (3.2.2) Android plugin for Firebase

This is the updated version of [godot-android-module-firebase](https://github.com/yalcin-ata/godot-android-module-firebase) for the new Godot (3.2.2) Android plugin system (v1).

For the general documentation look [there](https://github.com/yalcin-ata/godot-android-module-firebase).

## Steps to add this plugin to your Godot project are as follows:

- Open your project in Godot
- Install Export Templates if necessary
- Install Android build template
- Download from this repository's ```downloads``` directory both __Firebase.release.aar__ and __Firebase.release.gdpa__ and copy them to ```[GODOT-PROJECT]/android/plugins/```
- Download from this repository's ```downloads``` directory __godot-firebase-config.json__ and copy it to ```[GODOT-PROJECT]/android/build/assets``` (create the directory if necessary) and edit it to match your settings (especially AdMob App ID and ad unit IDs)
- Download your project's __google-services.json__ from Firebase Console and copy it to ```[GODOT-PROJECT]/android/build/```
- Edit ```[GODOT-PROJECT]/android/build/build.gradle```:
  - add ```classpath 'com.google.gms:google-services:4.3.3'``` above the line ```//CHUNK_BUILDSCRIPT_DEPENDENCIES_BEGIN```
  - add ```apply plugin: 'com.google.gms.google-services'``` above the line ```//CHUNK_GLOBAL_BEGIN```
  - optional (for Authentication): search for ```buildTypes.all {``` and add to this block: ```resValue "string", "server_client_id", "project-123456..."``` where ```project-123456...``` is the *public-facing name* of your Firebase project
- Edit ```[GODOT-PROJECT]/android/build/AndroidManifest.xml```:
  - add the following above the line ```<!--CHUNK_APPLICATION_BEGIN-->``` and do not forget to set your AdMob App Id
        
        <meta-data
            android:name="com.google.android.gms.ads.APPLICATION_ID"
            android:value="ca-app-pub-ADMOB_APP_ID"/>
- Edit ```[GODOT-PROJECT]/android/build/config.gradle``` and set ```minSdk``` to __21__ (otherwise a gradle build error occurs: Number of method references cannot exceed 64K)
- In Godot select menu Project > Export, add Android and edit your settings (package unique name, keystores, etc.) and select under Custom Template: Use Custom Build and also under Plugins: Firebase.

That should be it!

## Steps to build .aar from this project:

After checking out this project open Android Studio and start an empty Android project (with no activity, minimum SDK 21). Then select menu File > New > Import module and import this project as a module. In the Android Studio's terminal you can then run:
- gradlew clean
- gradlew build

When finished the __.aar__ for both debug and release can be found here: ```build/outputs/aar```
