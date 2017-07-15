package com.mindgate.spycam.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mindgate.spycam.R;
import com.mindgate.spycam.imagemodel.ImageModel;
import com.mindgate.spycam.view.ImageDetails;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    Context context;
    ArrayList<String> images = new ArrayList<String>();
    ArrayList<ImageModel> imageModels;
    public ImageAdapter(Context context,ArrayList<ImageModel> imageModels) {
        this.context=context;
        this.imageModels=imageModels;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_cards, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageAdapter.ImageViewHolder holder, int position) {
        ImageModel image=imageModels.get(position);
        Bitmap myBitmap = BitmapFactory.decodeFile(image.getImage_uri());
        holder.imageView.setImageBitmap(myBitmap);
    }

    @Override
    public int getItemCount() {
        return imageModels.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        AppCompatImageView imageView;
        CardView cardView;
        public ImageViewHolder(final View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.cameraImageView);
            cardView=itemView.findViewById(R.id.cardView);
            cardView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case  R.id.cardView:
                    Intent intent=new Intent(context,ImageDetails.class);
                    Bundle args = new Bundle();
                    intent.putExtra("picture", images);
                    intent.putExtra("transition", ViewCompat.getTransitionName(cardView));
                    ActivityOptionsCompat options = ActivityOptionsCompat
                            .makeSceneTransitionAnimation((AppCompatActivity) context,
                                    cardView, context.getString(R.string.custom_transition));
                    context.startActivity(intent, options.toBundle());
                    break;
            }
        }
            /*File file= new File(android.os.Environment.getExternalStorageDirectory(),"SpyCam");
            if (file.isDirectory())
            {
                listFile = file.listFiles();
                for (int i = 0; i < listFile.length; i++)
                {
                    images.add(listFile[i].getAbsolutePath());
                }
            }*/
        }

        /*@Override
        public void onClick(View view) {
            switch (view.getId()){
               case  R.id.cardView:
                   Intent intent=new Intent(context,ImageDetails.class);
                   Bundle args = new Bundle();

                   ActivityOptionsCompat options = ActivityOptionsCompat
                           .makeSceneTransitionAnimation((AppCompatActivity) context,
                                   cardView, context.getString(R.string.custom_transition));
                   context.startActivity(intent, options.toBundle());
                break;
            }
        }*/
    }


