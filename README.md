# Flashcards

Unofficial mobile client for https://www.duolingo.com/.
Allows viewing list of known words, and reviewing them via flashcards. (this functionality is missing from their mobile app)

Use your Duoling login and password to login.
If you don't have one but want to preview app use the following one: appiumtest/appiumtest

|Android|iOS|
|---|---|
|![AndroidList](media/android/list.png)|![iOSList](media/ios/list.png)|
|<a href='https://play.google.com/store/apps/details?id=com.triangleleft.flashcards&hl=en&utm_source=global_co&utm_medium=prtnr&utm_content=Mar2515&utm_campaign=PartBadge&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png'/></a>|TBD|

## Cross-Platform
All business logic is placed into separate java-only module (called "core"). Android app, simply includes it as jar dependency, while for iOS it is translated by [j2objc](http://j2objc.org/) into Obj-C code that is then compiled and packaged into .framework (type of library for iOS projects).
Translation and compilation are done using [j2objc-gradle](https://github.com/j2objc-contrib/j2objc-gradle) plugin.
Packaging into framework is done by custom makefile (core/Makefile)

This app uses vector graphics only, so they too are shared between Android and iOS.
As I am targeting API 16, during build of Android app, aapt generates multiple png images for each dpi. For iOS we need only three types: 1x (mdpi), 2x (xhdpi), 3x (xxhdpi). They are packaged into .assets file (core/assets.mk) that is included into XCode project.

## Tests
All unit tests within "core" module are also translated to ObjC and run automatically during building of framework. (to make sure that translated code also works as expected)

UI testing is done using [appium](http://appium.io/). Test are mostly platform independent (with the exception of keyboard uage). To interact with ui elements I use "pages": abstract objects with fields representing elements on screen. Mapping is done using annotations, which one is used depends on the current driver.
```java
    @AndroidFindBy(id = "com.triangleleft.flashcards:id/flashcard_translation")
    @iOSFindBy(accessibility = "flashcard_translation")
    public WebElement translation;
```
As appium is black-box testing so in order to mock network responses we build a special version of app with endpoint replaced to adress of local server. (Ideally one should use proxy or custom DNS server to intercept requests and test release version of app)
To run UI tests for Android:
* start appium server
* start android emulator
* build appium variant of app  (`./gradlew :app:assembleAppium`)
* run appium tests (`./gradlew :appium:androidTest`)

To run UI tests for iOS: (broken as of 04.10.16 due to XCode 8 update)
* start appium server
* start iOS simulator
* build and install appium varion of app using XCode
* run appium tests (`./gradlew :appium:iosTest`)

## Bulding and running
* Download and unpack j2objc-1.1
* Create symlink to your unpacked j2objc: `ln -s <path_to_unpacked_j2objc ~/Library/j2objc`. This is the directory XCode expects it to be. (Alternatively your can manually update XCode targets and change user-defined build setting J2OBJC_HOME)
* Update your `local.properties` file:
```
sdk.dir=<path_to_android_sdk>
j2objc.home=<path_to_unpacked_j2objc>
```
* Copy frameworks from j2objc to your frameworks dir:
```
sudo cp -a <path_to_unpacked_j2objc>/frameworks/. /Library/Frameworks/
```
* If you want to assemble release version of Android App create `keystore.properties` file with following contents:
```
keyPath=<path_to_your_key>
keyAlias=<key_alias>
keyPassword=<key_password>
storePassword=<key_store_password>
```
* Add `JAVA7_HOME` - path to JDK7 to your environment variables (this is needed for Retrolambda to make sure unit-tests are run using JDK7)

To build Android version:
* `./gradlew assembleDebug`

To build iOS version
* `./build_framework.sh`
* make sure you have CocoaPods installed
* move to ios dir and run `pod install` (you have to do it once)
* open `ios/Flashcards.xcworkspace`
* Use XCode to build