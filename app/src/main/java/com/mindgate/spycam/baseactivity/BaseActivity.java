package com.mindgate.spycam.baseactivity;

import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {
    public abstract void initView();
    public abstract void setClickListener();
}
