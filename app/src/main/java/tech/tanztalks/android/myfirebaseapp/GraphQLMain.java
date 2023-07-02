package tech.tanztalks.android.myfirebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;



import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class GraphQLMain extends AppCompatActivity implements PlantIdDataFetcher.PlantIdentificationListener {

    private static final String TAG = GraphQLMain.class.getSimpleName();
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;

    private ImageView imageView;
    private TextView plantNameTextView;
    private TextView plantDescriptionTextView;
    private Button captureButton;
    private Button pickButton;
    private Button identifyButton;

    private Uri imageUri;
    ActionBar actionBar;
    private FirebaseAuth authProfile;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_qlmain);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Plant Identification");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        imageView = findViewById(R.id.imageView);
        plantNameTextView = findViewById(R.id.plantNameTextView);
        plantDescriptionTextView = findViewById(R.id.plantDescriptionTextView);
        captureButton = findViewById(R.id.captureButton);
        pickButton = findViewById(R.id.pickButton);
        identifyButton = findViewById(R.id.identifyButton);

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        pickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageFromGallery();
            }
        });

        identifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null) {
                    identifyPlant(imageUri);
                } else {
                    Toast.makeText(GraphQLMain.this, "Please select an image first", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
            imageUri = null;
        } else if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            imageView.setImageURI(selectedImageUri);
            imageUri = selectedImageUri;
        }
    }

    private void identifyPlant(Uri imageUri) {
        try {
            InputStream imageStream = getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);

            if (bitmap != null) {
                PlantIdDataFetcher.identifyPlant(bitmap, this);
            } else {
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Log.e(TAG, "Error loading image: " + e.getMessage());
            Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayResult(Plant plant) {
        String plantName = plant.getName();
        String plantDescription = plant.getDescription();

        plantNameTextView.setText(plantName);
        plantDescriptionTextView.setText(plantDescription);
    }

    @Override
    public void onPlantIdentified(final Plant plant) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                displayResult(plant);
            }
        });
    }
    public boolean onSupportNavigateUp(){
        onBackPressed();//goto previous activity
        return super.onSupportNavigateUp();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //Inflate menu items
        getMenuInflater().inflate(R.menu.common_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    // When any menu item is selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();
        if(id == R.id.menu_refresh){
            //Refresh Activity
            startActivity((getIntent()));
            finish();

        } else if(id == R.id.menu_flower_classification){
            Intent intent = new Intent(GraphQLMain.this, FlowerClassification.class);
            startActivity(intent);

        }
        else if(id == R.id.menu_watch_video){
            Intent intent = new Intent(GraphQLMain.this, VideoActivity.class);
            startActivity(intent);
        }

        else if(id == R.id.menu_location){
            Intent intent = new Intent(GraphQLMain.this, LocationActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_reviews){
            Intent intent = new Intent(GraphQLMain.this, AddReviewActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_user_panel){
            Intent intent = new Intent(GraphQLMain.this, ShowPostActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_user_list){
            Intent intent = new Intent(GraphQLMain.this, UserListActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_flower_library){
            Intent intent = new Intent(GraphQLMain.this, FlowerLibraryActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_flower_dictionary){
            Intent intent = new Intent(GraphQLMain.this, FlowerDicActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.menu_contact_us) {
            Intent intent = new Intent(GraphQLMain.this, ContactUs.class);
            startActivity(intent);
        }
        else if (id == R.id.menu_know_plant) {
            Intent intent = new Intent(GraphQLMain.this, GraphQLMain.class);
            startActivity(intent);
        }
        else if (id == R.id.menu_report) {
            Intent intent = new Intent(GraphQLMain.this, GeneratePdfActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_sign_out){
            authProfile.signOut();
            Toast.makeText(GraphQLMain.this, "Signed Out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(GraphQLMain.this, MainActivity.class);

            // Clear stack to prevent user coming back to UserActivity on pressing back button signing out
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Closer UserActivity after signing out
        } else{
            Toast.makeText(GraphQLMain.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
