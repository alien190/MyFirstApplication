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
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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

                Request request = new Request.Builder()
                        .url(BuildConfig.SERVER_URL.concat("user/"))
                        .build();

                OkHttpClient client = ApiUtils.getBasicAuthClient(
                        mLogin.getText().toString(),
                        mPassword.getText().toString(),
                        true);

                client.newCall(request).enqueue(new Callback() {

                    Handler handler = new Handler(getActivity().getMainLooper());

                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                showMessage(R.string.request_error);
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (!response.isSuccessful()) {
                                    //todo детеальная обработка ошибок
                                    showMessage(R.string.auth_error);
                                } else {
                                    try {
                                        Gson gson = new Gson();
                                        JsonObject json = gson.fromJson(response.body().string(), JsonObject.class);
                                        User user = gson.fromJson(json.get("data"), User.class);
                                        Intent startProfileIntent =
                                                new Intent(getActivity(), ProfileActivity.class);
                                        startProfileIntent.putExtra(ProfileActivity.USER_KEY, user);
                                        startActivity(startProfileIntent);
                                        getActivity().finish();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
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

    private View.OnFocusChangeListener mOnLoginFocusListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                // mLogin.showDropDown();
            }
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
        mLogin.setOnFocusChangeListener(mOnLoginFocusListener);

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
//    @Override
//    public void onDestroy() {
//        mLoginedUsersAdapter = null;
//        super.onDestroy();
//    }
}
