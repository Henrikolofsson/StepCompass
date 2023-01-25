package com.example.stepcompass.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.stepcompass.Controller;
import com.example.stepcompass.R;


public class LoginFragment extends Fragment {
    private EditText et_username;
    private EditText et_password;
    private Button btn_log_in;
    private Controller controller;

    public LoginFragment(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initializeComponents(view);
        registerListeners();
        return view;
    }

    private void initializeComponents(View view) {
        et_username = (EditText) view.findViewById(R.id.et_username);
        et_password = (EditText) view.findViewById(R.id.et_password);
        btn_log_in = (Button) view.findViewById(R.id.btn_login);
    }

    private void registerListeners() {
        btn_log_in.setOnClickListener(new ButtonLogInListener());
    }

    private class ButtonLogInListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String username = et_username.getText().toString();
            String password = et_password.getText().toString();
            controller.signCreateBtnPressed(username, password);
        }
    }
}