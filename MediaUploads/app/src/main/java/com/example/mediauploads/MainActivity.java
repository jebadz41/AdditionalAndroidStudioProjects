package com.example.mediauploads;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static final int PICK_IMAGE = 1;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ivView)
    ImageView ivView;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btnPick)
    Button btnPick;
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
    @OnClick(value = {R.id.btnUpload,R.id.btnPick})
    public void userClick(View v){
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        switch (v.getId()){
            case R.id.btnUpload:
                UploadPick();
                break;
            case R.id.btnPick:
                ImagePick();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            ivView.setImageURI(data.getData());
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
    }

    public void UploadPick(){
        BitmapDrawable drawable = (BitmapDrawable) ivView.getDrawable();
        if(drawable != null) {
            Bitmap photo = drawable.getBitmap();
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Name");
            final EditText input = new EditText(this);

            builder.setView(input);
            builder.setPositiveButton("OK", (dialog, which) -> {
                pbUpload.setVisibility(View.VISIBLE);
                Backendless.Files.Android.upload(photo, Bitmap.CompressFormat.PNG, 100, input.getText().toString().trim(),
                        "pics", new AsyncCallback<BackendlessFile>() {
                        @Override
                        public void handleResponse(BackendlessFile response) {
                            Toast.makeText(MainActivity.this, "Upload Success", Toast.LENGTH_SHORT).show();
                            pbUpload.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(MainActivity.this, "Upload Failed\n" + fault.getMessage(), Toast.LENGTH_SHORT).show();
                            pbUpload.setVisibility(View.INVISIBLE);
                        }
                    });
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> {
                dialog.cancel();
                pbUpload.setVisibility(View.INVISIBLE);
            });
            builder.show();
        }
        else
            Toast.makeText(this, "No Image to upload", Toast.LENGTH_SHORT).show();
    }
}