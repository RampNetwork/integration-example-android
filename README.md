
# Ramp Android integration tutorial

If you have an Android app built with Kotlin, here's a tutorial on how to add Ramp to it in just a few steps.

Remember to check out our full documentation at [https://docs.ramp.network/](https://docs.ramp.network/).

## What do we need to do?
- compose a URL for the Ramp widget with parameters of your choice
- create an intent and send it to the system to handle it
- prepare the app for handle a  return intent after a successful purchase

## In detail

### Composing widget URL

The Ramp widget allows you to provide some parameters before displaying it, so the user doesn't have to type or copy-paste information. You can set options such as wallet address, cryptocurrency and crypto amount, etc. In order to do so, we need to create a URL with proper query parameters as showcased in the snippet below. You can find the description of all available parameters in our [documentation](https://docs.ramp.network/configuration/).

```kotlin
    private fun composeUrl(): String {
        return Uri.Builder()
            .scheme("https")
            .authority(rampHost)
            .appendQueryParameter("swapAsset", swapAsset)
            .appendQueryParameter("userAddress", userAddress)
            .appendQueryParameter("hostApiKey", hostApiKey)
            .appendQueryParameter("hostAppName", hostAppName)
            .appendQueryParameter("hostLogoUrl", hostLogoUrl)
            .appendQueryParameter("finalUrl", finalUrl)
            .build()
            .toString()
    }
```

### Create an intent and send it

In order to open Ramp in a browser, we need to create an intent with the action parameter set to `Intent.ACTION_VIEW` and add the previously created URL as `data`. Then, we'll start an activity with this intent. From now on, the system will handle it on its own and it'll open the browser with our URL loaded.

```kotlin
 val intent = Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(composeUrl()) }
 startActivity(intent)
```

### Handling a URL callback

One of the query parameters you can pass to Ramp widget URL is `finalUrl`. If you do this, Ramp will redirect your users to this URL after the purchase is completed. You can use this mechanism for your app to detect purchase completion and perform some actions like notifying your user about the purchase status.

#### Step 1 - Define a redirection URL

Now, we need to register a URL scheme that's unique for our app. Usually the best way is to simply use the app's name. If your app's name is **Ramp-Example**, the scheme may be `ramp-example`. Next, append a host that is unique for completing Ramp Purchase, for example `ramp.purchase.complete`.

Having these two, you can now define a value for the `finalUrl` parameter - it will be `ramp-example://ramp.purchase.complete`.

#### Step 2 - Register deep link to app content

 To create a link to your app content, we need to add an intent filter with `action`, `data` and `category` elements in the `AndroidManifest.xml` file. Intent filter should contain `action` with `ACTION_VIEW` value, two categories with the values `DEFAULT` and `BROWSABLE`, and `data` with `scheme` and `host` that you defined in step 1. You can find more details on how to add a custom URL scheme in the [Android documentation](https://developer.android.com/training/app-links/deep-linking).

```xml
<activity android:name=".YourActivity">
    <intent-filter android:autoVerify="true">
        <data
            android:host="ramp.purchase.complete"
            android:scheme="ramp-example" />

        <action android:name="android.intent.action.VIEW" />

        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
    </intent-filter>
</activity>
```

To prevent multiple instances, add `singleTask` value in the `launchMode` attribute
```xml
<application
    ...
    android:launchMode="singleTask"
    ...
    >
```

#### Step 3 - Handle final callback in your activity

Now it's time to handle the URL callback inside your app by overriding the `onNewIntent` method in your `Activity` just like in the snippets below.

```kotlin
override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    processIntent(intent)
}

private fun processIntent(intent: Intent) {
    val uri = intent.data?.toString()
    if (uri == RAMP_FINAL_URL) {
        //show your user that the purchase is complete here
    }
}
```
We also need to ensure that incoming intents will be processed properly in the `onCreate` method by using `processIntent(it)` in case our activity was killed or is newly created.

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    ...

    intent?.let { processIntent(it) }

    ...
}
```

That's it - your app can now use Ramp to allow your users to buy crypto easily.
