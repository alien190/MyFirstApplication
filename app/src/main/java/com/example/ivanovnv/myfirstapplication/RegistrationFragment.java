package com.example.ivanovnv.myfirstapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import com.google.gson.Gson;

import java.io.IOException;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
    private SharedPreferencesHelper mSharedPreferencesHelper;

    public static RegistrationFragment newInstance () {return new RegistrationFragment();}

    private View.OnClickListener mOnRegistrationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(isInputValid()) {

                User user = new User(
                        mLogin.getText().toString(),
                        mName.getText().toString(),
                        mPassword.getText().toString());

                final Request request = new Request.Builder()
                        .url(BuildConfig.SERVER_URL.concat("registration/"))
                        .post(RequestBody.create(JSON, new Gson().toJson(user)))
                        .build();

                OkHttpClient client = new OkHttpClient();
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
                                if (response.isSuccessful()) {
                                    showMessage(R.string.login_register_success);
                                    getFragmentManager().popBackStack();
                                } else {
                                    //todo детеальная обработка ошибок
                                    showMessage(R.string.login_register_error);
                                }
                            }
                        });
                    }
                });
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
        mName = view.findViewById(R.id.et_name);

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
