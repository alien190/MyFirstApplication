package com.example.ivanovnv.myfirstapplication;

import android.support.v4.app.Fragment;

/**
 * Created by IvanovNV on 22.02.2018.
 */

public class RegistrationActivity extends SingleFragmentActivity {
    @Override
    protected Fragment getFragment() {
        return RegistrationFragment.newInstance();
    }
}
