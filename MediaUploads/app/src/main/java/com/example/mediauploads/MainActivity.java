package com.example.mediauploads;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {

    public static final int PICK_IMAGE = 1;
    ImageView ivView;
    ProgressBar pbUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivView = findViewById(R.id.ivView);
        pbUpload = findViewById(R.id.pbUpload);
        Button btnPick = findViewById(R.id.btnPick),
                btnUpload = findViewById(R.id.btnUpload);

        btnPick.setOnClickListener(v -> {
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("image/*");
            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setType("image/*");
            Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
            startActivityForResult(chooserIntent, PICK_IMAGE);
        });

        btnUpload.setOnClickListener(v -> {
            BitmapDrawable drawable = (BitmapDrawable) ivView.getDrawable();
            if(drawable != null) {
                pbUpload.setVisibility(View.VISIBLE);
                Bitmap photo = drawable.getBitmap();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Name");
                final EditText input = new EditText(this);
                builder.setView(input);
                builder.setPositiveButton("OK", (dialog, which) -> Backendless.Files.Android.upload(
                        photo, Bitmap.CompressFormat.PNG, 100, input.getText().toString().trim(), "pics", new AsyncCallback<BackendlessFile>() {
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
                        }));
                builder.setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.cancel();
                    pbUpload.setVisibility(View.INVISIBLE);
                });
                builder.show();
            }
            else
                Toast.makeText(this, "No Image to upload", Toast.LENGTH_SHORT).show();

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE) {
            ivView.setImageURI(data.getData());
        }

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        pbUpload.setVisibility(View.INVISIBLE);
    }
}