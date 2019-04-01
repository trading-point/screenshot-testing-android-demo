# Screenshot Tests

This is a project to demonstrate how screenshot testing works and how to setup and configure in your project.

## Documentation
Take a look at the documentation at http://facebook.github.io/screenshot-tests-for-android/#getting-started
and https://github.com/Karumi/Shot

## Getting started

#### Setup python:

1. Install Python
2. Set Android-Sdk and python paths (or for Windows set Environments Variables)
3. Upgrade pip ``pip -m pip install --upgrade pip``
4. Install Pillow ``pip install Pillow``

Setup the Gradle plugin:

```groovy
  buildscript {
    // ...
    dependencies {
      // ...
      classpath 'com.karumi:shot:2.2.0'
    }
  }
  apply plugin: 'shot'

  shot {
    appId = 'YOUR_APPLICATION_ID'
  }
```

This plugin sets up a few convenience commands you can list executing ``./gradlew tasks`` and reviewing the ``Shot`` associated tasks:

**If you are using flavors update your shot configuration inside the ``build.gradle`` file as follows:**

```groovy
  shot {
    appId = 'YOUR_APPLICATION_ID'
    instrumentationTestTask = 'connected<FlavorName><BuildTypeName>AndroidTest'
    packageTestApkTask = 'package<FlavorName><BuildTypeName>AndroidTest'
  }
```

The screenshots library needs the ``WRITE_EXTERNAL_STORAGE`` permission. 
If your app already has the permission then nothing needs to be done.
But if your app does not have it then there are two ways to do it:

1.  You can create an ``AndroidManifest.xml`` file inside the ``androidTest`` folder but make sure that your main ``AndroidManifest.xml`` and the one in the ``androidTest`` have the same ``sharedUserId``

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="<YOUR_APP_ID>.test"
    android:sharedUserId="<YOUR_APP_ID>.uid">

    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

</manifest>
```

2.  You can create a ``debug`` folder in ``src`` and inside it you can create an ``AndroidManifest.xml`` which will have the permission.

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="<YOUR_APP_ID>">

    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

</manifest>
```


Also remember to configure the instrumentation test runner in your ``build.gradle`` as follows:

```groovy
android {
    // ...
    defaultConfig {
        // ...
        testInstrumentationRunner "<YOUR_APP_ID>.<PATH_TO_FILE>.ScreenshotTestRunner"
    }

```

In order to do this, you'll have to create a class named ``ScreenshotTestRunner``, like the following one, inside your instrumentation tests source folder:

```java
public class ScreenshotTestRunner extends AndroidJUnitRunner {

    @Override
    public void onCreate(Bundle args) {
        ScreenshotRunner.onCreate(this, args);
        super.onCreate(args);
    }

    @Override
    public void finish(int resultCode, Bundle results) {
        ScreenshotRunner.onDestroy();
        super.finish(resultCode, results);
    }
}
```

## Setup an emulator

In order to be able to record/verify among different machines then you need to setup the same emulator on all machines

You can do it the classic way from the Android Studio or by running the ``avdmanager`` command:

```bash
avdmanager create avd -f -n <name> -k <"image"> -c <size> -d <id>
```

and an example of it is:

```bash
avdmanager create avd -n screenshot_tester -k "system-images;android-21;default;armeabi-v7a" -c 100M -f -d 22
```

## Write a test

```kotlin
@Test
fun theActivityIsShownProperly() {
        val mainActivity = startMainActivity()
       /*
         * Take the actual screenshot. At the end of this call, the screenshot
         * is stored on the device and the gradle plugin takes care of
         * pulling it and displaying it to you in nice ways.
         */
        Screenshot.snapActivity(activity).record()
}
```

According to the Android version you will choose for your emulator you may have issues with permission granting.
In order to solve this you can add a library and use the ``GrantPermissionRule`` in your test class.

Add ``com.android.support.test:rules:1.1.0`` or ``androidx.test:rules:1.1.0``(if you migrated to AndroidX) in your app's ``build.gradle`` file.
Then add:

```kotlin
@JvmField
@Rule
val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
```
or in java

```java
@Rule
public GrantPermissionRule permissionRule = GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
```

## Recording tests

You can record your screenshot tests executing this command:

```shell
./gradlew executeScreenshotTests -Precord
```

This will execute all your integration tests and it will pull all the generated screenshots into your repository so you can easily add them to the version control system.

## Verifying tests

Once you have a bunch of screenshot tests recorded you can easily verify if the behaviour of your app is the correct one executing this command:

```shell
./gradlew executeScreenshotTests
```

**After executing your screenshot tests using the Gradle task ``executeScreenshotTests`` a report with all your screenshots will be generated.**

## Running only some tests

You can run a single test or test class, just add the `android.testInstrumentationRunnerArguments.class` parameter within your gradle call. This option works for both modes, verification and recording, just remember to add the `-Precord` if you want to do the latter.

**Running all tests in a package:**

```shell
./gradlew executeScreenshotTests -Pandroid.testInstrumentationRunnerArguments.package=com.your.package
```

**Running all tests in a class:**

```shell
./gradlew executeScreenshotTests -Pandroid.testInstrumentationRunnerArguments.class=com.your.package.YourClassTest
```

**Running a single test:**

```shell
./gradlew executeScreenshotTests -Pandroid.testInstrumentationRunnerArguments.class=com.your.package.YourClassTest#yourTest
```
