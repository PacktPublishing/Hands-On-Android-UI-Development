package com.packtpub.claim.ui;

import android.os.Bundle;

import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.widget.RadioGroup;
import android.widget.TextSwitcher;

import com.packtpub.claim.R;

import com.packtpub.claim.model.Category;

/**
 * Created by jason on 2017/11/07.
 */
public class CategoryPickerFragment extends Fragment {

    private RadioGroup categories;
    private TextSwitcher categoryLabel;

    @Override
    public View onCreateView(
            final LayoutInflater inflater,
            final @Nullable ViewGroup container,
            final @Nullable Bundle savedInstanceState) {

        final View picker = inflater.inflate(R.layout.fragment_category_picker, container, false);

        categories = (RadioGroup) picker.findViewById(R.id.categories);
        categoryLabel = (TextSwitcher) picker.findViewById(R.id.selected_category);

        categories.setOnCheckedChangeListener(new IconPickerWrapper(categoryLabel));
        categories.check(R.id.other);

        return picker;
    }

    public Category getSelectedCategory() {
        return Category.forIdResource(categories.getCheckedRadioButtonId());
    }
    public void setSelectedCategory(final Category category){
        categories.check(category.getIdResource());
    }


}