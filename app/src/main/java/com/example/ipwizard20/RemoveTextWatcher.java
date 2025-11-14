package com.example.ipwizard20;

import android.text.TextWatcher;
import android.widget.EditText;

public class RemoveTextWatcher extends Thread {
    EditText editText;
    TextWatcher textWatcher;

    public RemoveTextWatcher(EditText editText_, TextWatcher textWatcher_) {
        editText = editText_;
        textWatcher = textWatcher_;
    }

    public void run() {
        editText.removeTextChangedListener(textWatcher);
    }
}