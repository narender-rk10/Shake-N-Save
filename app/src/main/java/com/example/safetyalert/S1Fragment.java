package com.example.safetyalert;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link S1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class S1Fragment extends Fragment {

    public static S1Fragment newInstance(String param1, String param2) {
        S1Fragment fragment = new S1Fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.fragment_s1, container, false);
        return myFragmentView;
    }
}
