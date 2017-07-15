package com.mindgate.spycam.view;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v7.widget.AppCompatTextView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.mindgate.spycam.baseactivity.BaseActivity;
import com.mindgate.spycam.R;
import com.mindgate.spycam.presenter.SpyCamPresenter;
import com.mindgate.spycam.utils.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SpyCamMainActivity extends BaseActivity implements SpyCamMainActivityInterface, SurfaceHolder.Callback {
    private static final int REQUEST_CODE = 1;
    private static final String TAG = "Result_check";
    SpyCamPresenter presenter;
    Context context;
    String Address, mydatetime;
    int count = 0;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private Camera mCamera;
    private CameraPreview mCameraPreview;
    int id = 11;
    boolean TIMER_STARTED;
    AppCompatTextView timerTextView;
    Camera.PreviewCallback previewCallback;
    FrameLayout frameLayout;
    public int counter;
    CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getLocationInfo();
        //captureImage();
        initView();
        setClickListener();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

        } else {
            Toast.makeText(this, "Device not supported", Toast.LENGTH_SHORT).show();
        }
        shutterTimer();
    }

    private void shutterTimer() {

        timer=new CountDownTimer(10000,1000) {
            public void onTick(long millisUntilFinished) {
                timerTextView.setText(getString(R.string.remaining_seconds) + millisUntilFinished / 1000);
                counter++;
            }

            public void onFinish() {
                timerTextView.setText(getString(R.string.pictured_captured));
                mCamera.takePicture(null, null, mPicture);
                finish();
                startActivity(getIntent());
            }
        }.start();
    }

    @Override
    public void initView() {
        // surfaceView= (SurfaceView) findViewById(R.id.surfaceView);
        presenter = new SpyCamPresenter(context, this);
        sharedPreferences = getSharedPreferences("count_key", MODE_PRIVATE);
        timerTextView = (AppCompatTextView) findViewById(R.id.timer_textView);
        editor = sharedPreferences.edit();
        count = sharedPreferences.getInt(Constants.KEY_COUNT, 0);
        mydatetime = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        frameLayout = (FrameLayout) findViewById(R.id.camera_frame);
        mCamera = getCameraInstance();
        mCameraPreview = new CameraPreview(this, mCamera);
        //FrameLayout preview = (FrameLayout) findViewById(R.id.camera_frame);
        frameLayout.addView(mCameraPreview);
    }

    @Override
    public void setClickListener() {

    }

    Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile(data, Address);
            if (pictureFile == null) {
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {

            } catch (IOException e) {
            }
        }

    };



    private File getOutputMediaFile(byte[] data, String Address) {
        File filepath = Environment.getExternalStorageDirectory();
        // Create a new folder in SD Card
        File directory = new File(filepath.getAbsolutePath() + "/SpyCam/");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File outputFile = new File(directory, getString(R.string.photo_stamp) + Address + mydatetime + " _00 " + count + getString(R.string.image_format));
        editor.putInt(Constants.KEY_COUNT, ++count);
        editor.apply();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.capture_image:
                captureImage();
                break;
            case R.id.sdcard:
                openFolder();
                finish();
                break;
            case R.id.openImage:
                timer.cancel();
                startActivity(new Intent(this, AllImagesActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void openFolder() {
        Uri selectedUri = Uri.parse(Environment.getExternalStorageDirectory() + "/SpyCam/");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(selectedUri, "resource/folder");
        startActivity(intent);
    }
    private Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            Toast.makeText(context, "Camera not available", Toast.LENGTH_SHORT).show();
        }
        return camera;
    }

    private void getLocationInfo() {
        LocationFind locationFind = new LocationFind(this);
        double latitude = locationFind.getLatitude();
        double longitude = locationFind.getLongitude();
        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        StringBuilder builder = new StringBuilder();
        try {
            List<Address> address = geoCoder.getFromLocation(latitude, longitude, 1);
            int maxLines = address.get(0).getMaxAddressLineIndex();
            for (int i = 0; i < maxLines; i++) {
                String addressStr = address.get(0).getAddressLine(i);
                builder.append(addressStr);
                builder.append(" ");
            }
            String finalAddress = builder.toString();
            Address = finalAddress;
        } catch (IOException e) {
        } catch (NullPointerException e) {
        }
    }


    private void captureImage() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent, REQUEST_CODE);
        //Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            storeCameraPhotoInSDCard(bitmap, Address);
        }
    }

    private void storeCameraPhotoInSDCard(Bitmap bitmap, String Address) {

        File outputFile = new File(Environment.getExternalStorageDirectory(), getString(R.string.photo_stamp) + Address + mydatetime + " _00 " + count + getString(R.string.image_format));
        editor.putInt(Constants.KEY_COUNT, ++count);
        editor.apply();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
// Now that the size is known, set up the camera parameters and begin
        // the preview.
        Camera.Parameters parameters = mCamera.getParameters();
        //parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
        // requestLayout();
        mCamera.setParameters(parameters);

        // Important: Call startPreview() to start updating the preview surface.
        // Preview must be started before you can take a picture.
        mCamera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
// Surface will be destroyed when we return, so stop the preview.
        if (mCamera != null) {
            // Call stopPreview() to stop updating the preview surface.
            mCamera.stopPreview();
        }
    }

    /**
     * When this function returns, mCamera will be null.
     */
    private void stopPreviewAndFreeCamera() {

        if (mCamera != null) {
            // Call stopPreview() to stop updating the preview surface.
            mCamera.stopPreview();

            // Important: Call release() to release the camera for use by other
            // applications. Applications should release the camera immediately
            // during onPause() and re-open() it during onResume()).
            mCamera.release();

            mCamera = null;
        }
    }
}
