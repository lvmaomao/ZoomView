package com.example.xx.zoomview_mt.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.xx.zoomview_mt.R;

/**
 * Created by yangyu on 16/10/12.
 */

public class ThirdActivity extends AppCompatActivity {
    TestFragment testFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        testFragment = TestFragment.getInstance();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().add(R.id.llContent, testFragment).commit();
    }

}
