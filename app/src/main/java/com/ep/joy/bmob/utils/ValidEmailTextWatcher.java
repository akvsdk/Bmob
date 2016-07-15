package com.ep.joy.bmob.utils;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;

import com.ep.joy.bmob.R;

/**
 * author   Joy
 * Date:  2016/7/12.
 * version:  V1.0
 * Description:
 */
public class ValidEmailTextWatcher extends ErrorTextWatcher {
    private final EmailAddressValidator mValidator = new EmailAddressValidator();
    private boolean mValidated = false;


    public ValidEmailTextWatcher(@NonNull final TextInputLayout textInputLayout) {
        this(textInputLayout, R.string.error_invalid_email);
    }

    public ValidEmailTextWatcher(@NonNull final TextInputLayout textInputLayout, @StringRes final int errorMessage) {
        super(textInputLayout, textInputLayout.getContext().getString(errorMessage));
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(final CharSequence text, final int start, final int before, final int count) {
        // The hasError is needed here because I can restore the instance state of the input layout without updating this flag in the watcher
        if (mValidated || hasError()) {
            validate();
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public boolean validate() {
        showError(!mValidator.isValid(getEditTextValue()));
        mValidated = true;
        return !hasError();
    }
}
