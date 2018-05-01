package com.example.ivanovnv.myfirstapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ivanovnv.myfirstapplication.model.RegistrationError;
import com.example.ivanovnv.myfirstapplication.model.UserForRegistration;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import retrofit2.HttpException;

/**
 * Created by IvanovNV on 21.02.2018.
 */

public class RegistrationFragment extends Fragment {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private EditText mLogin;
    private EditText mPassword;
    private EditText mPasswordAgain;
    private EditText mName;
    private Button mRegistration;

    public static RegistrationFragment newInstance() {
        return new RegistrationFragment();
    }

    private View.OnClickListener mOnRegistrationClickListener = new View.OnClickListener() {
        @SuppressLint("CheckResult")
        @Override
        public void onClick(View v) {

            if (isInputValid()) {

                UserForRegistration userForRegistration = new UserForRegistration(
                        mLogin.getText().toString(),
                        mName.getText().toString(),
                        mPassword.getText().toString());


                ApiUtils.getApi()
                        .registration(userForRegistration)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action() {
                            @Override
                            public void run() throws Exception {
                                showMessage(R.string.login_register_success);
                                getFragmentManager().popBackStack();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                showMessage(R.string.request_error);
                                HttpException exception = (HttpException) throwable;
                                RegistrationError error = ApiUtils.parseRegistrationError((retrofit2.Response<Void>)exception.response());
                                showError(error);
                            }
                        });

            } else {
                showMessage(R.string.login_input_error);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_registration, container, false);

        mLogin = view.findViewById(R.id.etRegEmail);
        mPassword = view.findViewById(R.id.etRegPassword);
        mPasswordAgain = view.findViewById(R.id.etRegPasswordAgain);
        mRegistration = view.findViewById(R.id.btnRegRegistration);
        mName = view.findViewById(R.id.et_name);
        mRegistration.setOnClickListener(mOnRegistrationClickListener);

        return view;
    }

    private boolean isInputValid() {
        String email = mLogin.getText().toString();
        if (isEmailValid(email) && isPasswordValid()) {
            return true;
        }
        return false;
    }

    private boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid() {
        String password = mPassword.getText().toString();
        String passwordAgain = mPasswordAgain.getText().toString();

        return password.equals(passwordAgain) && !TextUtils.isEmpty(password);
    }

    private void showMessage(@StringRes int string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
    }

    private void showError(RegistrationError error) {
        if (error.getErrors().getEmail() != null) {
            mLogin.setError(error.getErrors().getEmail().toArray()[0].toString());
        }
        if (error.getErrors().getPassword() != null) {
            mPassword.setError(error.getErrors().getPassword().toArray()[0].toString());
            mPasswordAgain.setError(error.getErrors().getPassword().toArray()[0].toString());
        }
        if (error.getErrors().getName() != null) {
            mName.setError(error.getErrors().getName().toArray()[0].toString());
        }
    }
}
