package com.asis.finalproject.bbc;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asis.finalproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BbcFragment extends Fragment {

    public BbcFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bbc, container, false);
    }
}
