package com.mindgate.spycam.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;

import com.mindgate.spycam.R;

public class ImageDetails extends AppCompatActivity {
    AppCompatImageView imageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_details);
        imageView= (AppCompatImageView) findViewById(R.id.imageDetails);
        Bundle extras = getIntent().getExtras();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }
}
