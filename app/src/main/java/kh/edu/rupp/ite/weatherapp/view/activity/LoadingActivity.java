package kh.edu.rupp.ite.weatherapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import kh.edu.rupp.ite.weatherapp.R;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);


        ImageView gifImageView = findViewById(R.id.logoLoading);

        //Using Glide Library to get GifDrawable as Loading Screen
        Glide.with(this)
                .asGif()
                .load(R.drawable.loading)
                .into(gifImageView);

        new Handler().postDelayed(() -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }, 2000);
    }

}