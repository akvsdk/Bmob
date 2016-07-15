package com.ep.joy.bmob.utils;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.text.TextWatcher;

/**
 * author   Joy
 * Date:  2016/7/12.
 * version:  V1.0
 * Description:
 */
public abstract class ErrorTextWatcher implements  TextWatcher {

    private TextInputLayout mTextInputLayout;
    private String errorMessage;

    protected ErrorTextWatcher(@NonNull final TextInputLayout textInputLayout, @NonNull final String errorMessage) {
        this.mTextInputLayout = textInputLayout;
        this.errorMessage = errorMessage;
    }

    public final boolean hasError() {
        return mTextInputLayout.getError() != null;
    }

    public abstract boolean validate();

    protected String getEditTextValue() {
        return mTextInputLayout.getEditText().getText().toString();
    }

    protected void showError(final boolean error) {
        if (!error) {
            mTextInputLayout.setError(null);
            mTextInputLayout.setErrorEnabled(false);
        } else {
            if (!errorMessage.equals(mTextInputLayout.getError())) {
                // Stop the flickering that happens when setting the same error message multiple times
                mTextInputLayout.setError(errorMessage);
            }
            mTextInputLayout.requestFocus();
        }
    }

}
