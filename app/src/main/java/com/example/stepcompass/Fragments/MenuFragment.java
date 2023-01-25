package com.example.stepcompass.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.stepcompass.Controller;
import com.example.stepcompass.R;


public class MenuFragment extends Fragment {
    private ImageButton ib_steps;
    private ImageButton ib_compass;
    private Controller controller;

    public MenuFragment() {

    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        initializeComponents(view);
        registerListeners();
        return view;
    }

    private void initializeComponents(View view) {
        ib_steps = (ImageButton) view.findViewById(R.id.ib_steps);
        ib_compass = (ImageButton) view.findViewById(R.id.ib_compass);
    }

    private void registerListeners() {
        ib_steps.setOnClickListener(new StepButtonListener());
        ib_compass.setOnClickListener(new CompassButtonListener());
    }

    private class StepButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            controller.ActivityChosen("steps");
        }
    }

    private class CompassButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            controller.ActivityChosen("compass");
        }
    }
}