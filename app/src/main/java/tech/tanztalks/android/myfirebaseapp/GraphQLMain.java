package tech.tanztalks.android.myfirebaseapp;

import androidx.appcompat.app.AppCompatActivity;



import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

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

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_qlmain);

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
}
