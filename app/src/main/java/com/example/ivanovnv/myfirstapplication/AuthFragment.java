package com.example.ivanovnv.myfirstapplication;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AuthFragment extends Fragment {

    private AutoCompleteTextView mLogin;
    private EditText mPassword;
    private Button mEnter;
    private Button mRegister;
    private SharedPreferencesHelper mSharedPreferencesHelper;

    private ArrayAdapter<String> mLoginedUsersAdapter;

    public static AuthFragment newInstance() {
        
        Bundle args = new Bundle();
        
        AuthFragment fragment = new AuthFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    private View.OnClickListener mOnEnterClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isEmailValid() && isPasswordValid()) {
               User user = mSharedPreferencesHelper.login(
                       mLogin.getText().toString(),
                       mPassword.getText().toString());
                if(user != null) {
                    Intent startProfileIntent =
                            new Intent(getActivity(),ProfileActivity.class);
                    startProfileIntent.putExtra(ProfileActivity.USER_KEY,user);

                    startActivity(startProfileIntent);
                    getActivity().finish();

                }
                else {
                    showMessage(R.string.login_error);
                }
            }
            else {
                showMessage(R.string.login_input_error);
            }


        }
    };

    private View.OnClickListener mOnRegisterClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           getFragmentManager()
                   .beginTransaction()
                   .replace(R.id.fragment_container,RegistrationFragment.newInstance())
                   .addToBackStack(RegistrationFragment.class.getName())
                   .commit();

        }
    };

    private View.OnFocusChangeListener mOnLoginFocusListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus) {
               // mLogin.showDropDown();
            }
        }
    };

    private boolean isEmailValid () {
        return !TextUtils.isEmpty(mLogin.getText())&&
                Patterns.EMAIL_ADDRESS.matcher(mLogin.getText()).matches();
    }

    private boolean isPasswordValid () {
        return !TextUtils.isEmpty(mPassword.getText());
    }

    private void showMessage (@StringRes int string) {
        Toast.makeText(getActivity(),string,Toast.LENGTH_LONG).show();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_auth,container,false);

        mSharedPreferencesHelper = new SharedPreferencesHelper(getActivity());

        mLogin = v.findViewById(R.id.etLogin);
        mPassword = v.findViewById(R.id.etPassword);
        mEnter = v.findViewById(R.id.buttonEnter);
        mRegister = v.findViewById(R.id.buttonRegister);

        mEnter.setOnClickListener(mOnEnterClickListener);
        mRegister.setOnClickListener(mOnRegisterClickListener);
        mLogin.setOnFocusChangeListener(mOnLoginFocusListener);

        mLoginedUsersAdapter = new ArrayAdapter<>(
                getActivity(),
                R.layout.support_simple_spinner_dropdown_item,
                mSharedPreferencesHelper.getSuccessLogins());

        mLogin.setAdapter(mLoginedUsersAdapter);

        return v;
    }
/*
    @Override
    public void onResume() {

        super.onResume();
    }
    */
/*
    @Override
    public void onPause() {
        mEnter.setOnClickListener(null);
        mRegister.setOnClickListener(null);
        mLogin.setOnFocusChangeListener(null);
        super.onPause();
    }
*/
    @Override
    public void onDestroy() {
        mLoginedUsersAdapter = null;
        super.onDestroy();
    }
}
