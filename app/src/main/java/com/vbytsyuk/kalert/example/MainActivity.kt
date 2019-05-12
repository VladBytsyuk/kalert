package com.vbytsyuk.kalert.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.vbytsyuk.kalert.lib.KAlert
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val TITLE = "Title"
const val MESSAGE = "Message"

const val POSITIVE = "Positive"
const val NEGATIVE = "Negative"
const val CANCELLED = "Cancelled"


class MainActivity : AppCompatActivity(), CoroutineScope {

    override val coroutineContext = Dispatchers.Main

    private fun toast(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        commonAlert.setOnClickListener { commonAlert() }
        kAlert.setOnClickListener { dialogXAlert() }
    }

    private fun commonAlert() {
        AlertDialog.Builder(this@MainActivity).apply {
            setTitle(TITLE)
            setMessage(MESSAGE)
            setPositiveButton(POSITIVE) { _, _ -> toast(POSITIVE) }
            setNegativeButton(NEGATIVE) { _, _ -> toast(NEGATIVE) }
            setOnCancelListener { toast(CANCELLED) }
        }.show()
    }

    private fun dialogXAlert() = launch {
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
    }
}
