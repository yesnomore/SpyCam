package com.mindgate.spycam.view;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mindgate.spycam.R;
import com.mindgate.spycam.adapter.ImageAdapter;
import com.mindgate.spycam.baseactivity.BaseActivity;
import com.mindgate.spycam.imagemodel.ImageModel;

import java.io.File;
import java.util.ArrayList;

public class AllImagesActivity extends BaseActivity {
    RecyclerView recyclerView;
    ImageAdapter imageAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_preview);
        initView();
        recyclerView.setAdapter(new ImageAdapter(this,getImages()) );
}

    @Override
    public void initView() {
        setTitle(getString(R.string.all_Images));
        recyclerView = (RecyclerView) findViewById(R.id.imageRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

    }

    @Override
    public void setClickListener() {
    }

    @Override
    public void onBackPressed() {
       // camera.startPreview();
        super.onBackPressed();
    }
    private ArrayList<ImageModel> getImages(){
        ArrayList<ImageModel> f = new ArrayList<>();// list of file paths
        File[] listFile;
        ImageModel model;
            File file= new File(android.os.Environment.getExternalStorageDirectory(),"SpyCam");
            if (file.isDirectory())
            {
                listFile = file.listFiles();
                for (int i = 0; i < listFile.length; i++)
                {
                    model = new ImageModel();
                    model.setImage_uri((listFile[i].getAbsolutePath()));
                    f.add(model);
                }
            }
        return f;
    }
}
