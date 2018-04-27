package com.example.ivanovnv.myfirstapplication;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ivanovnv.myfirstapplication.model.User;
import com.example.ivanovnv.myfirstapplication.model.UserForRegistration;


public class AuthFragment extends Fragment {

    private AutoCompleteTextView mLogin;
    private EditText mPassword;
    private Button mEnter;
    private Button mRegister;


    public static AuthFragment newInstance() {

        Bundle args = new Bundle();

        AuthFragment fragment = new AuthFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private View.OnClickListener mOnEnterClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isEmailValid() && isPasswordValid()) {

                ApiUtils.getApi(mLogin.getText().toString(), mPassword.getText().toString())
                        .getUser().enqueue(new retrofit2.Callback<User>() {

                    Handler handler = new Handler(getActivity().getMainLooper());

                    @Override
                    public void onResponse(retrofit2.Call<User> call, final retrofit2.Response<User> response) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (!response.isSuccessful()) {
                                    //todo детеальная обработка ошибок
                                    showMessage(R.string.auth_error);
                                } else {
                                    if (response.body() != null) {

                                        UserForRegistration userForRegistration =
                                                new UserForRegistration(response.body().getData().getEmail(),
                                                        response.body().getData().getName(), "");

                                        Intent startProfileIntent =
                                                new Intent(getActivity(), ProfileActivity.class);
                                        startProfileIntent.putExtra(ProfileActivity.USER_KEY, userForRegistration);
                                        startActivity(startProfileIntent);
                                        getActivity().finish();
                                    }
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(retrofit2.Call<User> call, Throwable t) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                showMessage(R.string.request_error);
                            }
                        });
                    }
                });

            } else {
                showMessage(R.string.login_input_error);
            }
        }
    };

    private View.OnClickListener mOnRegisterClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, RegistrationFragment.newInstance())
                    .addToBackStack(RegistrationFragment.class.getName())
                    .commit();

        }
    };


    private boolean isEmailValid() {
        return !TextUtils.isEmpty(mLogin.getText()) &&
                Patterns.EMAIL_ADDRESS.matcher(mLogin.getText()).matches();
    }

    private boolean isPasswordValid() {
        return !TextUtils.isEmpty(mPassword.getText());
    }

    private void showMessage(@StringRes int string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_auth, container, false);

        mLogin = v.findViewById(R.id.etLogin);
        mPassword = v.findViewById(R.id.etPassword);
        mEnter = v.findViewById(R.id.buttonEnter);
        mRegister = v.findViewById(R.id.buttonRegister);

        mEnter.setOnClickListener(mOnEnterClickListener);
        mRegister.setOnClickListener(mOnRegisterClickListener);

        return v;
    }

   }
