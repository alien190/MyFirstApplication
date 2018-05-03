package com.example.ivanovnv.myfirstapplication.albums;

import android.support.v4.app.Fragment;

import com.example.ivanovnv.myfirstapplication.SingleFragmentActivity;

public class AlbumsActivity extends SingleFragmentActivity {

    @Override
    protected Fragment getFragment() {
        return AlbumsFragment.newInstance();
    }
}
