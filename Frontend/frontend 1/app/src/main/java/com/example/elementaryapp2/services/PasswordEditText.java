package com.example.elementaryapp2.services;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatEditText;

import com.example.elementaryapp2.R;

public class PasswordEditText extends AppCompatEditText {

    private boolean isPasswordVisible = false;

    public PasswordEditText(Context context) {
        super(context);
        init();
    }

    public PasswordEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PasswordEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // Initialize with eye-off icon
        setCompoundDrawablesWithIntrinsicBounds(R.drawable.key_24px, 0, R.drawable.visibility_off_24px, 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int DRAWABLE_RIGHT = 2;
        if (event.getAction() == MotionEvent.ACTION_UP) {
            Drawable rightDrawable = getCompoundDrawables()[DRAWABLE_RIGHT];
            if (rightDrawable != null && event.getRawX() >= (getRight() - rightDrawable.getBounds().width())) {
                togglePasswordVisibility();
                performClick(); // Call performClick for accessibility
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Hide password
            setTransformationMethod(PasswordTransformationMethod.getInstance());
            setCompoundDrawablesWithIntrinsicBounds(R.drawable.key_24px, 0, R.drawable.visibility_off_24px, 0);
        } else {
            // Show password
            setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            setCompoundDrawablesWithIntrinsicBounds(R.drawable.key_24px, 0, R.drawable.visibility_24px, 0);
        }
        // Move cursor to the end of the text
        setSelection(getText().length());
        isPasswordVisible = !isPasswordVisible;
    }
}

