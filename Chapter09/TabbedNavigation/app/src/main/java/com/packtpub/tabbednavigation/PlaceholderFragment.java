package com.packtpub.tabbednavigation;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class PlaceholderFragment extends Fragment {

    private static final String ARG_TEXT = "text";

    public View onCreateView(
            final LayoutInflater inflater,
            final ViewGroup container,
            final Bundle savedInstanceState) {

        final View rootView = inflater.inflate(
                R.layout.fragment_placeholder,
                container,
                false
        );

        final TextView textView = rootView.findViewById(R.id.placeholder_text);
        textView.setText(getArguments().getString(ARG_TEXT));
        return rootView;
    }

    public static PlaceholderFragment newInstance(final String text) {
        final PlaceholderFragment fragment = new PlaceholderFragment();
        final Bundle args = new Bundle();
        args.putString(ARG_TEXT, text);
        fragment.setArguments(args);
        return fragment;
    }

}
