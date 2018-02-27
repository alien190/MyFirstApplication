package com.example.ivanovnv.myfirstapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

/**
 * Created by IvanovNV on 21.02.2018.
 */

public class RegistrationFragment extends Fragment {

    private EditText mLogin;
    private EditText mPassword;
    private EditText mPasswordAgain;
    private Button mRegistration;
    private SharedPreferencesHelper mSharedPreferencesHelper;

    public static RegistrationFragment newInstance () {return new RegistrationFragment();}

    private View.OnClickListener mOnRegistrationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(isInputValid()){
                if(mSharedPreferencesHelper.addUser(new User (
                        mLogin.getText().toString(),
                        mPassword.getText().toString())))
                {
                    showMessage(R.string.login_register_success);
                    getFragmentManager().popBackStack();
                }
                else {
                    showMessage(R.string.login_register_error);
                }
            }
            else {
                showMessage(R.string.login_input_error);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_registration,container,false);

        mSharedPreferencesHelper = new SharedPreferencesHelper(getActivity());

        mLogin = view.findViewById(R.id.etRegEmail);
        mPassword = view.findViewById(R.id.etRegPassword);
        mPasswordAgain = view.findViewById(R.id.etRegPasswordAgain);
        mRegistration = view.findViewById(R.id.btnRegRegistration);

        mRegistration.setOnClickListener(mOnRegistrationClickListener);

        return view;
    }

    private boolean isInputValid(){
        String email = mLogin.getText().toString();
        if(isEmailValid(email) && isPasswordValid()){
            return true;
        }
        return false;
    }

    private boolean isEmailValid (String email){
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid (){
        String password = mPassword.getText().toString();
        String passwordAgain = mPasswordAgain.getText().toString();

        return password.equals(passwordAgain) && !TextUtils.isEmpty(password);
    }

    private void showMessage (@StringRes int string) {
        Toast.makeText(getActivity(), string,Toast.LENGTH_SHORT).show();
    }
}
