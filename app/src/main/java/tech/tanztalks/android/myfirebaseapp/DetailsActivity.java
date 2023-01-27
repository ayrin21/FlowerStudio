package tech.tanztalks.android.myfirebaseapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import tech.tanztalks.android.myfirebaseapp.models.FlowerResponseModel;

public class DetailsActivity extends AppCompatActivity {

    TextView textView;
    ImageView imageView;
    ActionBar actionBar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Details");

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        imageView = findViewById(R.id.image);
        textView = findViewById(R.id.tv);

        FlowerResponseModel responseModel = (FlowerResponseModel) getIntent().getSerializableExtra("flowers");
        //take photo from response Respose

        String photoString = responseModel.getPhoto();
        Uri photoUri = Uri.parse(FlowerLibraryActivity.BASE_URL+"/photos/"+photoString);
        Picasso.with(DetailsActivity.this).load(photoUri).into(imageView);

        textView.setText(responseModel.getName()+"\n"+responseModel.getCategory()+"\n"+responseModel.getInstructions()+"\n");


    }
    public void back(View view){
        startActivity(new Intent(DetailsActivity.this, FlowerLibraryActivity.class));
    }

    public boolean onSupportNavigateUp(){
        onBackPressed();//goto previous activity
        return super.onSupportNavigateUp();
    }
}