![KAlert](logo/kalert-logo-big.png)

Dependency
------------
Add **this** to your module *build.gradle* file.
```gradle
implementation 'io.github.vladbytsyuk:kalert.lib:0.1'
```

What is it?
-------------
A wrapper library for ```AlertDialog``` class. 
It was written only for Kotlin and uses coroutines.

Concept
-------------
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

Example
-------------
```AlertDialog```
``` kotlin
AlertDialog.Builder(this@MainActivity).apply {
    setTitle(TITLE)
    setMessage(MESSAGE)
    setPositiveButton(POSITIVE) { _, _ -> toast(POSITIVE) }
    setNegativeButton(NEGATIVE) { _, _ -> toast(NEGATIVE) }
    setOnCancelListener { toast(CANCELLED) }
}.show()
```

```KAlert```
``` kotlin
val userAction = KAlert(
    context = this@MainActivity,
    title = TITLE,
    message = MESSAGE,
    positiveText = POSITIVE,
    negativeText = NEGATIVE
).awaitUserAction()
when (userAction) {
    is KAlert.Action.Positive -> toast(POSITIVE)
    is KAlert.Action.Negative -> toast(NEGATIVE)
    is KAlert.Action.Cancelled -> toast(CANCELLED)
}
```

Caution
-----------------
Use this library only if you use kotlin and coroutines in your project.
