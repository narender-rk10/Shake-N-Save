package com.example.safetyalert;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link S4Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class S4Fragment extends Fragment {

    public static S4Fragment newInstance(String param1, String param2) {
        S4Fragment fragment = new S4Fragment();
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
        View myFragmentView = inflater.inflate(R.layout.fragment_s4, container, false);
        return myFragmentView;
    }
}
