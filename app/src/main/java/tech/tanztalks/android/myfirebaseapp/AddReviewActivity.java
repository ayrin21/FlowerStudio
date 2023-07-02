package tech.tanztalks.android.myfirebaseapp;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class AddReviewActivity extends AppCompatActivity {

   FirebaseAuth firebaseAuth;
   private FirebaseAuth authProfile;
   DatabaseReference userDbRef;
   ActionBar actionBar;
   //permission constants
   private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    //image pick contants
    private static final int IMAGE_PICK_CAMERA_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;

    // permission array
    String[] cameraPermissions;
    String[] storagePermissions;
   //
   EditText titlep, descriptionp;
   //rating
   TextView rateCount, showRating;
   EditText review;
   RatingBar ratingBar;
   float rateValue ; String temp;
   //rating
   ImageView imageIv;
   Button buttonp;
   //user info
    String name, email, uid, dp;

   // image picked will br same in this uri
    Uri image_rui=null;

    //
    ProgressDialog pd;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);

      actionBar = getSupportActionBar();
      actionBar.setTitle("Add New Review");

      actionBar.setDisplayShowHomeEnabled(true);
      actionBar.setDisplayHomeAsUpEnabled(true);

      //init permission arrays
        cameraPermissions= new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

       pd = new ProgressDialog(this);


      firebaseAuth=FirebaseAuth.getInstance();
      checkUserStatus();

      actionBar.setTitle(email);

        //get user info for the post
        userDbRef = FirebaseDatabase.getInstance().getReference("Registered Users");
        Query query = userDbRef.orderByChild("email").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    name= ""+ds.child("fullname").getValue();
                    email = ""+ds.child("email").getValue();
                    // dp
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
               Toast.makeText(AddReviewActivity.this,"Error occured!",Toast.LENGTH_LONG).show();
            }
        });


        //init views
        titlep= findViewById(R.id.ptitle);
        descriptionp=findViewById(R.id.pdes);
        imageIv=findViewById(R.id.pImageIv);
        buttonp=findViewById(R.id.pupload);
        rateCount=findViewById(R.id.rateCount);
        ratingBar=findViewById(R.id.ratingBar);
        //review=findViewById(R.id.review);



        //get image from camera/gallery on click

        imageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show image pick dialogue
                showImagePickDialogue();
            }
        });
// rating
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rateValue=ratingBar.getRating();
                if(rateValue<=1 && rateValue >0)
                    rateCount.setText("Bad " + rateValue +"/5");
                else if (rateValue<=2 && rateValue >1)
                    rateCount.setText("Ok "+rateValue +"/5");
                else if(rateValue<=3 && rateValue >2)
                    rateCount.setText("Good " +rateValue +"/5");
                else if (rateValue<=4 && rateValue >3)
                    rateCount.setText("Very Good " + rateValue + "/5");
                else if (rateValue<=5 && rateValue >4)
                    rateCount.setText("Best " +rateValue +"/5");

            }
        });
//rating
        //upload button click listener

        buttonp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get data (title, description) from edittext
                String title = titlep.getText().toString().trim();
                String description = descriptionp.getText().toString().trim();
                temp = rateCount.getText().toString();
                //showRating.setText("Your Rating: \n" +temp +"\n" + review.getText());
//                review.setText("");
//                ratingBar.setRating(0);
//                rateCount.setText("");

                if (TextUtils.isEmpty(title)){
                    Toast.makeText(AddReviewActivity.this,"Enter title!",Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(description)){
                    Toast.makeText(AddReviewActivity.this,"Enter description!",Toast.LENGTH_LONG).show();
                    return;
                }
                if (image_rui==null){

                    // post without image
                    uploadData(title, description, "noImage", temp);
                }
                else{
                    uploadData(title, description, String.valueOf(image_rui), temp);
                }
            }
        });


    }

    private void uploadData(String title, String description, String uri, String t) {
        pd.setMessage("Publishing Post...");
        pd.show();

        //
        String timestamp = String.valueOf(System.currentTimeMillis());
        String filePathAndName = "Posts/"+"post_"+timestamp;
        if(!uri.equals("noImage")){
            //
            StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathAndName);
            ref.putFile(Uri.parse(uri)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful());

                    String downloadUri = uriTask.getResult().toString();
                    if (uriTask.isSuccessful()) {
                        //

                        HashMap<Object, String> hashMap = new HashMap<>();
                        //
                        hashMap.put("uid", uid);
                        hashMap.put("uName", name);
                        hashMap.put("uEmail", email);
                        // hashMap.put("uDp", dp);
                        hashMap.put("pId", timestamp);
                        hashMap.put("pTitle", title);
                        hashMap.put("pDescr", description);
                        hashMap.put("pRating", temp);
                        hashMap.put("pImage", downloadUri);
                        hashMap.put("pTime", timestamp);
                        hashMap.put("pLikes", "0");
                        hashMap.put("pComments", "0");


                        //
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
                        //
                        ref.child(timestamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                //
                                pd.dismiss();
                                Toast.makeText(AddReviewActivity.this, "Post published!", Toast.LENGTH_SHORT).show();
                                //
                                titlep.setText("");
                                descriptionp.setText("");
                                imageIv.setImageURI(null);
                                image_rui=null;
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //
                                pd.dismiss();
                                Toast.makeText(AddReviewActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                          pd.dismiss();
                            Toast.makeText(AddReviewActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


        }
        else {
            //
            HashMap<Object, String> hashMap = new HashMap<>();
            //
            hashMap.put("uid", uid);
            hashMap.put("uName", name);
            hashMap.put("uEmail", email);
            // hashMap.put("uDp", dp);
            hashMap.put("pId", timestamp);
            hashMap.put("pTitle", title);
            hashMap.put("pDescr", description);
            hashMap.put("pRating",temp); // Eta mile nai dekhe ashe nai etokkhon!
            hashMap.put("pImage", "noImage");
            hashMap.put("pTime", timestamp);
            hashMap.put("pLikes", "0");
            hashMap.put("pComments", "0");

            //
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
            //
            ref.child(timestamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    //
                    pd.dismiss();
                    Toast.makeText(AddReviewActivity.this, "Post published!", Toast.LENGTH_SHORT).show();
                    titlep.setText("");
                    descriptionp.setText("");
                    imageIv.setImageURI(null);
                    image_rui=null;

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //
                    pd.dismiss();
                    Toast.makeText(AddReviewActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void showImagePickDialogue() {
        String [] options = { "Camera", "Gallery"};

        // dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // set options to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                //item click handle
                if (which==0){
                    //camera clicked
                    if(!checkCameraPermision()){
                        requestCameraPermission();
                    }
                    else {
                        pickFromCamera();
                    }
                }
                if (which==1){
                    //gallery clicked
                    if(!checkStoragePermision()){
                        requestStoragePermission();
                    }
                    else{
                        pickFromGallery();
                    }
                }
            }
        });

    }

    private void pickFromGallery() {
        //
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);

    }

    private void pickFromCamera() {
        //
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE, "Temp Pick");
        cv.put(MediaStore.Images.Media.DESCRIPTION, "TEMO Descr");
        image_rui = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_rui);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }

    private  boolean checkStoragePermision(){
        //
        //
        //
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission(){
        //
        ActivityCompat.requestPermissions(this,storagePermissions, STORAGE_REQUEST_CODE);
    }

    private  boolean checkCameraPermision(){
        //
        //
        //
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)==(PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission(){
        //
        ActivityCompat.requestPermissions(this,cameraPermissions, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUserStatus();
    }
    protected  void onResume(){
        super.onResume();
        checkUserStatus();
    }


 private void checkUserStatus() {
     //get current user
     FirebaseUser user = firebaseAuth.getCurrentUser();
     if (user != null) {
         //user is signed in stay here
         //set email of logged in user
         //mProfileTv.setText(user.getEmail());
         email = user.getEmail();
         uid = user.getUid();
         DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
         referenceProfile.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                 if (readUserDetails != null) {
                     name = readUserDetails.fullname;


                 }

             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });

     }
 }
    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();//goto previous activity
        return super.onSupportNavigateUp();
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.common_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    } + blah blah

    //handle permission results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case CAMERA_REQUEST_CODE:{
                if ((grantResults.length>0)){
                    boolean cameraACcepted = grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraACcepted && storageAccepted){
                        pickFromCamera();
                    }
                    else{
                        Toast.makeText(this, "Camera & Storgae both permissions are necessary", Toast.LENGTH_SHORT).show();
                    }
                }
                else{

                  if(grantResults.length>0){
                      boolean storageAccepted = grantResults[0]== PackageManager.PERMISSION_GRANTED;
                      if (storageAccepted){
                          pickFromGallery();
                      }
                      else{
                          Toast.makeText(this, "Storgae both permissions are necessary", Toast.LENGTH_SHORT).show();
                      }
                  }
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //
        if (requestCode==RESULT_OK){
            if (requestCode==IMAGE_PICK_GALLERY_CODE){
                //
              image_rui = data.getData();
              //
                imageIv.setImageURI(image_rui);
            }
            else if (requestCode==IMAGE_PICK_CAMERA_CODE){

                imageIv.setImageURI(image_rui);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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

//        } else if(id == R.id.menu_change_password){
//            Intent intent = new Intent(UserAcitivity.this, ChangePasswordActivity.class);
//            startActivity(intent);

        } else if(id == R.id.menu_flower_classification){
            Intent intent = new Intent(AddReviewActivity.this, FlowerClassification.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_watch_video){
            Intent intent = new Intent(AddReviewActivity.this, VideoActivity.class);
            startActivity(intent);
        }

        else if(id == R.id.menu_location){
            Intent intent = new Intent(AddReviewActivity.this, LocationActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_reviews){
            Intent intent = new Intent(AddReviewActivity.this, AddReviewActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_user_panel){
            Intent intent = new Intent(AddReviewActivity.this, ShowPostActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_user_list){
            Intent intent = new Intent(AddReviewActivity.this, UserListActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_flower_library){
            Intent intent = new Intent(AddReviewActivity.this, FlowerLibraryActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_flower_dictionary){
            Intent intent = new Intent(AddReviewActivity.this, FlowerDicActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.menu_contact_us) {
            Intent intent = new Intent(AddReviewActivity.this, ContactUs.class);
            startActivity(intent);
        }
        else if (id == R.id.menu_know_plant) {
            Intent intent = new Intent(AddReviewActivity.this, GraphQLMain.class);
            startActivity(intent);
        }
        else if (id == R.id.menu_report) {
            Intent intent = new Intent(AddReviewActivity.this, GeneratePdfActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_sign_out){
            authProfile.signOut();
            Toast.makeText(AddReviewActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AddReviewActivity.this, MainActivity.class);

            // Clear stack to prevent user coming back to UserActivity on pressing back button signing out
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Closer UserActivity after signing out
        } else{
            Toast.makeText(AddReviewActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}