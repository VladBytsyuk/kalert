package com.vbytsyuk.kalert.lib

import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AlertDialog
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


/**
 * Not implemented yet [AlertDialog.Builder] functionality
 * TODO: [AlertDialog.Builder.setOnDismissListener] (must be implemented or not?)
 * TODO: [AlertDialog.Builder.setOnKeyListener]
 * TODO: [AlertDialog.Builder.setItems]
 * TODO: [AlertDialog.Builder.setAdapter]
 * TODO: [AlertDialog.Builder.setCursor]
 * TODO: [AlertDialog.Builder.setMultiChoiceItems]
 * TODO: [AlertDialog.Builder.setSingleChoiceItems]
 * TODO: [AlertDialog.Builder.setSingleChoiceItems]
 * TODO: [AlertDialog.Builder.setOnItemSelectedListener]
 * TODO: [AlertDialog.Builder.setView]
 * TODO: [AlertDialog.Builder.setInverseBackgroundForced] Deprecated
 * TODO: [AlertDialog.Builder.setRecycleOnMeasureEnabled] Deprecated
 *
 */
class KAlert(
    val context: Context,
    @StyleRes themeResId: Int = 0,

    title: CharSequence? = null,
    @StringRes titleId: Int? = null,
    customTitle: View? = null,

    message: CharSequence? = null,
    @StringRes messageId: Int? = null,

    icon: Drawable? = null,
    @DrawableRes iconId: Int? = null,
    @AttrRes iconAttrId: Int? = null,

    private val positiveText: CharSequence? = null,
    @StringRes private val positiveTextId: Int? = null,
    private val positiveIcon: Drawable? = null,

    private val negativeText: CharSequence? = null,
    @StringRes private val negativeTextId: Int? = null,
    private val negativeIcon: Drawable? = null,

    private val neutralText: CharSequence? = null,
    @StringRes private val neutralTextId: Int? = null,
    private val neutralIcon: Drawable? = null,

    cancelable: Boolean = true
) {
    sealed class Action {
        data class Positive(val dialog: DialogInterface) : Action()
        data class Negative(val dialog: DialogInterface) : Action()
        data class Neutral(val dialog: DialogInterface) : Action()
        data class Cancelled(val dialog: DialogInterface) : Action()
    }


    /* =============================================================================================
     * Configuring AlertDialog body
     * =============================================================================================
     */

    private val builder = AlertDialog.Builder(context, themeResId).apply {
        fillTitle(title, titleId, customTitle)
        fillMessage(message, messageId)
        fillIcon(icon, iconId)
        fillIconAttribute(iconAttrId)
        setCancelable(cancelable)
    }

    private fun AlertDialog.Builder.fillTitle(
        title: CharSequence?,
        @StringRes titleId: Int?,
        customTitle: View?
    ) {
        when {
            title != null -> setTitle(title)
            titleId != null -> setTitle(titleId)
            customTitle != null -> setCustomTitle(customTitle)
        }
    }

    private fun AlertDialog.Builder.fillMessage(
        message: CharSequence? = null,
        @StringRes messageId: Int? = null
    ) {
        when {
            message != null -> setMessage(message)
            messageId != null -> setMessage(messageId)
        }
    }

    private fun AlertDialog.Builder.fillIcon(
        icon: Drawable? = null,
        @DrawableRes iconId: Int? = null
    ) {
        when {
            icon != null -> setIcon(icon)
            iconId != null -> setIcon(icon)
        }
    }

    private fun AlertDialog.Builder.fillIconAttribute(
        @AttrRes iconAttrId: Int? = null
    ) {
        when {
            iconAttrId != null -> setIconAttribute(iconAttrId)
        }
    }


    /* =============================================================================================
     * Configuring AlertDialog actions
     * =============================================================================================
     */

    suspend fun awaitUserAction() = suspendCoroutine<Action> { continuation ->
        builder.apply {
            setPositiveButton(continuation, positiveText, positiveTextId, positiveIcon)
            setNegativeButton(continuation, negativeText, negativeTextId, negativeIcon)
            setNeutralButton(continuation, neutralText, neutralTextId, neutralIcon)
            setOnCancelListener(cancelListener(continuation))
        }.show()
    }

    private fun AlertDialog.Builder.setPositiveButton(
        continuation: Continuation<Action>,
        positiveText: CharSequence? = null,
        @StringRes positiveTextId: Int? = null,
        positiveIcon: Drawable? = null
    ) {
        when {
            positiveText != null -> setPositiveButton(positiveText, positiveListener(continuation))
            positiveTextId != null -> setPositiveButton(positiveTextId, positiveListener(continuation))
        }
        when {
            positiveIcon != null -> setPositiveButtonIcon(positiveIcon)
        }
    }

    private fun positiveListener(
        continuation: Continuation<Action>
    ) = DialogInterface.OnClickListener { dialog, _ ->
        continuation.resume(Action.Positive(dialog))
    }

    private fun AlertDialog.Builder.setNegativeButton(
        continuation: Continuation<Action>,
        negativeText: CharSequence? = null,
        @StringRes negativeTextId: Int? = null,
        negativeIcon: Drawable? = null
    ) {
        when {
            negativeText != null -> setNegativeButton(negativeText, negativeListener(continuation))
            negativeTextId != null -> setNegativeButton(negativeTextId, negativeListener(continuation))
        }
        when {
            negativeIcon != null -> setNegativeButtonIcon(negativeIcon)
        }
    }

    private fun negativeListener(
        continuation: Continuation<Action>
    ) = DialogInterface.OnClickListener { dialog, _ ->
        continuation.resume(Action.Negative(dialog))
    }

    private fun AlertDialog.Builder.setNeutralButton(
        continuation: Continuation<Action>,
        neutralText: CharSequence? = null,
        @StringRes neutralTextId: Int? = null,
        neutralIcon: Drawable? = null
    ) {
        when {
            neutralText != null -> setNeutralButton(neutralText, neutralListener(continuation))
            neutralTextId != null -> setNeutralButton(neutralTextId, neutralListener(continuation))
        }
        when {
            neutralIcon != null -> setNegativeButtonIcon(neutralIcon)
        }
    }

    private fun neutralListener(
        continuation: Continuation<Action>
    ) = DialogInterface.OnClickListener { dialog, _ ->
        continuation.resume(Action.Neutral(dialog))
    }

    private fun cancelListener(
        continuation: Continuation<Action>
    ) = DialogInterface.OnCancelListener { dialog ->
        continuation.resume(Action.Cancelled(dialog))
    }
}