package tech.tanztalks.android.myfirebaseapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class UploadProfilePicture extends AppCompatActivity {

    private ProgressBar progressBar;
    private ImageView imageViewUploadPic;
    private FirebaseAuth authProfile;
    private StorageReference storageReference;
    private FirebaseUser firebaseUser;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri uriImage;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile_picture);

        getSupportActionBar().setTitle("Upload Profile Picture");

        Button buttonUploadPicChoose= findViewById(R.id.upload_pic_choose_button);
        Button buttonUploadPic = findViewById(R.id.upload_pic_button);
        progressBar = findViewById(R.id.progressBar_upload_photo);
        imageViewUploadPic= findViewById(R.id.imageView_profile_dp);

        authProfile = FirebaseAuth.getInstance();
        firebaseUser= authProfile.getCurrentUser();

        storageReference= FirebaseStorage.getInstance().getReference("DisplayPics"); //if exists, it will be returned otherwise created
        Uri uri = firebaseUser.getPhotoUrl(); // unifrom resource identifier

        // Set User's current DP in ImageView(if uploaded already). We will use Picasso since imageViewer setImage
        //Regular URIs

        Picasso.with(UploadProfilePicture.this).load(uri).into(imageViewUploadPic);

        //choosing image to upload
        buttonUploadPicChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              openFileChooser();
            }
        });

        // Upload image
        buttonUploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                UploadPic();
            }
        });

    }
    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST );

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_IMAGE_REQUEST && resultCode== RESULT_OK && data!= null && data.getData() != null){
            uriImage= data.getData();
            imageViewUploadPic.setImageURI(uriImage);

        }
    }

    private void UploadPic(){
        if(uriImage!= null){
            //Save the image with uid of the currently logged user
            StorageReference fileReference= storageReference.child(authProfile.getCurrentUser().getUid()+"."+getFileExtension(uriImage));

            // Upload image to storage
            fileReference.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUri = uri;
                            firebaseUser = authProfile.getCurrentUser();

                            // Finally set the display image of the user after upload
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setPhotoUri(downloadUri).build();
                            firebaseUser.updateProfile(profileUpdates);
                        }
                    });

                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(UploadProfilePicture.this, "Upload successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UploadProfilePicture.this, UserAcitivity.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadProfilePicture.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(UploadProfilePicture.this,"No file selected", Toast.LENGTH_LONG).show();
        }
    }
    //Obtaining file extension of the image
    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    //Creating ActionBar Menu
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

//        } else if(id == R.id.menu_change_password){
//            Intent intent = new Intent(UserAcitivity.this, ChangePasswordActivity.class);
//            startActivity(intent);
        } else if(id == R.id.menu_sign_out){
            authProfile.signOut();
            Toast.makeText(UploadProfilePicture.this, "Signed Out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UploadProfilePicture.this, MainActivity.class);

            // Clear stack to prevent user coming back to UserActivity on pressing back button signing out
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Closer UserActivity after signing out
        } else{
            Toast.makeText(UploadProfilePicture.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }






}