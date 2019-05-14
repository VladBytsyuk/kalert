# KAlert
![KAlert](logo/kalert-logo-big.png)

A wrapper library for ```AlertDialog``` class.

## How to use
Add **this** to your module *build.gradle* file.
```gradle
implementation 'io.github.vladbytsyuk:kalert.lib:0.1'
```

Configure your dialog with ```KAlert``` class via constructor.
```kotlin
val dialog = KAlert(
    context = this@MyActivity,
    title = "Title",
    messageId = R.string.dialog_message,
    positiveTextId = R.string.ok,
    negativeText = "Cancel"
)
```

When you need to show dialog and await user action call suspend function.
```kotlin
val userAction = dialog.awaitUserAction()
```

Now you can handle user actions.
```kotlin
val toastMessage = when (userAction) {
    is KAlert.Action.Positive -> "User pressed \"OK\" button"
    is KAlert.Action.Negative -> "User pressed \"Cancel\" button"
    is KAlert.Action.Cancelled -> "User closed dialog"
}
```

## Concept
Every ```AlertDialog``` needs ```AlertDialog.Builder``` class 
to configure dialog for your case.
Every action such as positive button click, negative button click etc.
is asynchronous operation. And ```AlertDialog``` requires callback for this case.

But we can think about ```AlertDialog``` as a suspend function, that return
user choice. Now coroutines helps us. 

```KAlert``` allows us to configure ```AlertDialog``` without 
```AlertDialog.Builder``` with ```KAlert``` constructor default parameters.
Moreover, ```KAlert``` hides all ```AlertDialog``` calls inside its implementation
and you don't need to mix two different classes inside your code.
If you configure your dialog with ```KAlert``` call ```awaitUserAction()```
suspend function to get user choice. As well as it is suspend function 
you need to call it from coroutine or other suspend function.

## Syntax comparison to ```AlertDialog.Builder```
```AlertDialog```
``` kotlin
AlertDialog.Builder(this@MainActivity).apply {
    setTitle("Title")
    setMessage(R.id.dialog_message)
    setPositiveButton("OK") { _, _ -> 
        toast("User pressed \"OK\" button") 
    }
    setNegativeButton(R.string.cancel) { _, _ -> 
        toast("User pressed \"Cancel\" button") 
    }
    setOnCancelListener { toast("User closed dialog") }
}.show()
```

```KAlert```
``` kotlin
val userAction = KAlert(
    context = this@MainActivity,
    title = "Title",
    messageId = R.id.dialog_message,
    positiveText = "OK",
    negativeTextId = R.string.cancel
).awaitUserAction()
val toastMessage = when (userAction) {
    is KAlert.Action.Positive -> "User pressed \"OK\" button"
    is KAlert.Action.Negative -> "User pressed \"Cancel\" button"
    is KAlert.Action.Cancelled -> "User closed dialog"
}
toast(toastMessage)
```

Caution
-----------------
Use this library only if you use kotlin and coroutines in your project.
