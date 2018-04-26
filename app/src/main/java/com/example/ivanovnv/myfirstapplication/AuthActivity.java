package com.example.ivanovnv.myfirstapplication;

import android.support.v4.app.Fragment;

/**
 * Created by IvanovNV on 21.02.2018.
 *
 *
 */

public class AuthActivity extends SingleFragmentActivity {
    @Override
    protected Fragment getFragment() {
        return AuthFragment.newInstance();
    }
}
