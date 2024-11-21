package com.example.melodate.ui.shared.custom_view

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.InputType
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.melodate.R

class PasswordEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnTouchListener {

    private var visiblePasswordIcon: Drawable? =
        ContextCompat.getDrawable(context, R.drawable.ic_visible)
    private var invisiblePasswordIcon: Drawable? =
        ContextCompat.getDrawable(context, R.drawable.ic_invisible)


    private var passwordIcon: Drawable? = ContextCompat.getDrawable(context, R.drawable.ic_password)

    init {


        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        setOnTouchListener(this)
        setCompoundDrawablesWithIntrinsicBounds(passwordIcon, null, invisiblePasswordIcon, null)

        // Load the custom font from resources
        val typeface: Typeface? = ResourcesCompat.getFont(context, R.font.montserrat_regular)
        // Apply the custom font to the EditText
        this.typeface = typeface

    }

    private fun changePasswordVisibility() {
        if (inputType == (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            setCompoundDrawablesWithIntrinsicBounds(passwordIcon, null, visiblePasswordIcon, null)
        } else {
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            setCompoundDrawablesWithIntrinsicBounds(passwordIcon, null, invisiblePasswordIcon, null)
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (compoundDrawables[2] != null) { // Check if the right drawable (icon) is set
            val iconEnd =
                (right - compoundDrawablePadding - (compoundDrawables[2]?.intrinsicWidth ?: 0))
            if (event?.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= iconEnd) {
                    changePasswordVisibility()
                    return true
                }
            }
        }
        return false
    }
}