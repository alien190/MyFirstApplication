package com.example.ivanovnv.myfirstapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.ivanovnv.myfirstapplication.model.User;

/**
 * Created by IvanovNV on 21.02.2018.
 */

public class ProfileActivity extends AppCompatActivity {

    public static String USER_KEY = "USER_KEY";

    private TextView mEmail;
    private TextView mName;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_profile);

        mEmail = findViewById(R.id.tvEmail);
        mName = findViewById(R.id.tvName);

        Bundle bundle = getIntent().getExtras();
        User user = (User) bundle.get(USER_KEY);

        mEmail.setText(user.getEmail());
        mName.setText(user.getName());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.actionLogout:
                Intent intent = new Intent(this,AuthActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
