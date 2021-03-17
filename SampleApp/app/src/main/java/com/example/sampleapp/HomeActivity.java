package com.example.sampleapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {

    public static final int PICK_IMAGE = 1;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btnCamera)
    Button btnCamera;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btnGallery)
    Button btnGallery;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btnPick)
    Button btnPick;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ivPicture)
    ImageView ivPicture;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btnUpload)
    Button btnUpload;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.pbUpload)
    ProgressBar pbUpload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(value = {R.id.btnCamera,R.id.btnGallery,R.id.btnPick,R.id.btnUpload})
    public void userClick(View v)
    {
        Intent intent;
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        switch (v.getId())
        {
            case R.id.btnCamera:
                intent = new Intent(HomeActivity.this,CameraActivity.class);
                startActivity(intent);
                break;
            case R.id.btnGallery:
                intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_VIEW);
                intent.setType("image/*");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.btnPick:
                ImagePick();
                break;
            case R.id.btnUpload:
                UploadPick();
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if(ivPicture.getDrawable() != null)
                ivPicture.setImageURI(null);

            try {
                ivPicture.setImageURI(data.getData());
            }catch (Exception e) { }

        }
    }


    public void ImagePick(){
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");
        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
        startActivityForResult(chooserIntent, PICK_IMAGE);

        btnUpload.setVisibility(View.VISIBLE);
    }

    public void UploadPick(){
        BitmapDrawable drawable = (BitmapDrawable) ivPicture.getDrawable();
        if(drawable != null) {
            Bitmap photo = drawable.getBitmap();
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setTitle("Name");
            final EditText input = new EditText(this);

            builder.setView(input);
            builder.setPositiveButton("OK", (dialog, which) -> {
                pbUpload.setVisibility(View.VISIBLE);
                Backendless.Files.Android.upload(photo, Bitmap.CompressFormat.PNG, 100, input.getText().toString().trim(),
                        "pics", new AsyncCallback<BackendlessFile>() {
                            @Override
                            public void handleResponse(BackendlessFile response) {
                                Toast.makeText(HomeActivity.this, "Upload Success", Toast.LENGTH_SHORT).show();
                                pbUpload.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Toast.makeText(HomeActivity.this, "Upload Failed\n" + fault.getMessage(), Toast.LENGTH_SHORT).show();
                                pbUpload.setVisibility(View.INVISIBLE);
                            }
                        });
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> {
                dialog.cancel();
                pbUpload.setVisibility(View.INVISIBLE);
                btnUpload.setVisibility(View.VISIBLE);
            });
            builder.show();
        }
        else
            Toast.makeText(this, "No Image to upload", Toast.LENGTH_SHORT).show();

        btnUpload.setVisibility(View.INVISIBLE);
    }


}