package com.masslight.pacifysdk.sample;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * @author Alexey
 * @since 4/20/17
 */
public abstract class TextChangesListener implements TextWatcher {

    private static final String EMPTY_STRING = "";

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        onTextChanged(s != null ? s.toString() : EMPTY_STRING);
    }

    protected abstract void onTextChanged(String text);

    @Override
    public void afterTextChanged(Editable s) {

    }
}
