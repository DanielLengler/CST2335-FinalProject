package com.asis.finalproject.nasaearthimage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.asis.finalproject.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteDetails extends Fragment {

    public FavoriteDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite_details, container, false);


        return view;

    }
}
