package com.example.ivanovnv.myfirstapplication;

import android.content.SharedPreferences;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class AuthActivity extends AppCompatActivity {

    private EditText mLogin;
    private EditText mPassword;
    private Button mEnter;
    private Button mRegister;

    private View.OnClickListener mOnEnterClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isEmailValid() && isPasswordValid()) {
                //переход в приложение
            } else
            {
                showMessage(R.string.login_input_error);
            }
        }
    };

    private View.OnClickListener mOnRegisterClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // todo Обработка нажатия по кнопке
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
        Toast.makeText(this,string,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_auth);

        mLogin = findViewById(R.id.etLogin);
        mPassword = findViewById(R.id.etPassword);
        mEnter = findViewById(R.id.buttonEnter);
        mRegister = findViewById(R.id.buttonRegister);

        mEnter.setOnClickListener(mOnEnterClickListener);
        mRegister.setOnClickListener(mOnRegisterClickListener);
    }
}
