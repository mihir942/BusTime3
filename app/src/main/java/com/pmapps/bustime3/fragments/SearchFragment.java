package com.pmapps.bustime3.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.pmapps.bustime3.R;

public class SearchFragment extends Fragment {

    private Context mContext;

    // initialising the context of fragment, for ease of use. no need to call requireContext() all the time.
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        initialiseStuffs(v);
        return v;
    }


    private void initialiseStuffs(View v) {
        SearchView searchView = v.findViewById(R.id.search_view);

        // GET: gets the "cross button" component/child of the search view
        AppCompatImageView imageView = (AppCompatImageView) searchView.findViewById(androidx.appcompat.R.id.search_close_btn);

        // SET: sets the image of the "cross button"
        imageView.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_searchview_cancel_icon));


        // GET: gets the edit text component/child of the searchview
        EditText editText = (EditText) searchView.findViewById(androidx.appcompat.R.id.search_src_text);

        // SET: sets text appearance as 12sp, white, circularstd font (light)
        editText.setTextAppearance(R.style.EditTextStyle);

        // SET: sets hint text "find bus stops / bus routes"
        editText.setHint(R.string.searchview_hint_text);

        // SET: re-sets the hint text color to faded white, since the original text appearance is white.
        editText.setHintTextColor(ContextCompat.getColor(mContext,R.color.faded_white));


    }
}