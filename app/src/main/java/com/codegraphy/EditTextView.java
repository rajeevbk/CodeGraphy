package com.codegraphy;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EditTextView extends EditText //TextView //implements StatusChangedListner
{
    private StrokeHandler strokeHandler;
    public EditTextView(Context context) { super(context); }

    public EditTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    void setStrokeManager(StrokeHandler strokeHandler) {
        this.strokeHandler = strokeHandler;
    }


}
