package com.example.melodate.ui.shared.custom_view

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.melodate.R


class EmailEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

        private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        private val emailIcon = ContextCompat.getDrawable(context, R.drawable.ic_email_light)

    init {
        hint = context.getString(R.string.email_example)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //nothing
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (!p0.toString().matches(emailPattern.toRegex())) {
                    setError(context.getString(R.string.email_not_valid), null)
                } else {
                    error = null
                }
            }

        })

        setCompoundDrawablesWithIntrinsicBounds(emailIcon, null, null, null)

        val typeface: Typeface? = ResourcesCompat.getFont(context, R.font.montserrat_regular)
        // Apply the custom font to the EditText
        this.typeface = typeface
    }

    override fun setError(error: CharSequence?, icon: Drawable?) {
        super.setError(error, null)
    }

//    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//        return false
//    }
}