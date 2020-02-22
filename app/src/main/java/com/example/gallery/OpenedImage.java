package com.example.gallery;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class OpenedImage extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opened_image);

        String url = getIntent().getStringExtra("url");
        imageView = findViewById(R.id.openedImageView);

        if(url != null) {
            Glide.with(this).load(url).into(imageView);
        } else {
            Toast.makeText(this, "Invalid or empty url", Toast.LENGTH_SHORT).show();
        }

    }
}
