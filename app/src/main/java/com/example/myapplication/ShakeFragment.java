package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ShakeFragment extends Fragment {

    private static final String ARG_SHAKE_COUNTER = "shake_counter";
    private int shakeCounter;

    public static ShakeFragment newInstance(int shakeCounter) {
        ShakeFragment fragment = new ShakeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SHAKE_COUNTER, shakeCounter);
        fragment.setArguments(args);
        return fragment;
    }

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public ShakeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            shakeCounter = getArguments().getInt(ARG_SHAKE_COUNTER, 0);
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shake, container, false);

        TextView shakeCounterTextView = view.findViewById(R.id.counter);
        Button backToMainButton = view.findViewById(R.id.fragment_button);

        Bundle args = getArguments();
        if (args != null) {
            int shakeCounter = args.getInt(ARG_SHAKE_COUNTER, 0);
            shakeCounterTextView.setText("Shakes: " + shakeCounter);
        }

        backToMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}