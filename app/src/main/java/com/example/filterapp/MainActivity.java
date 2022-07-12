package com.example.filterapp;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.request.RequestOptions;

import java.io.FileDescriptor;
import java.io.IOException;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;
import jp.wasabeef.glide.transformations.MaskTransformation;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private Bitmap img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.image_view);
    }
    public void applyFilter(Transformation<Bitmap> filter) {
        Glide.with(this).load(img)
                .apply(RequestOptions.bitmapTransform(filter))
                .into(imageView);
    }

    public void applyBlur(View view) {
        applyFilter(new BlurTransformation(25, 3));
    }

    public void applyGray(View view) {
        applyFilter(new GrayscaleTransformation());
    }

    public void applyMask(View view) {
        applyFilter(new MaskTransformation(1));
    }

    public void choosePhoto(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        someActivityResultLauncher.launch(intent);
     }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    Uri uri  = data.getData();
                    try {
                        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().
                                openFileDescriptor(uri, "r");
                        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                        img = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                        parcelFileDescriptor.close();
                        imageView.setImageBitmap(img);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
    );

}