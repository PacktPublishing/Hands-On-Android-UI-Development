package com.packtpub.claim.ui;

import android.widget.RadioGroup;
import android.widget.TextSwitcher;

/**
 * Created by jason on 2017/11/07.
 */
public class IconPickerWrapper implements RadioGroup.OnCheckedChangeListener {

    private final TextSwitcher label;

    public IconPickerWrapper(final TextSwitcher label) {
        this.label = label;
    }

    public void setLabelText(final CharSequence text) {
        label.setText(text);
    }

    @Override
    public void onCheckedChanged(final RadioGroup group, final int checkedId) {
        setLabelText(group.findViewById(checkedId).getContentDescription());
    }

}