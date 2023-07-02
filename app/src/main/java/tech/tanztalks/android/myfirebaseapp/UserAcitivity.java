package tech.tanztalks.android.myfirebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import kotlinx.coroutines.flow.Flow;

public class UserAcitivity extends AppCompatActivity {

    private TextView textViewWelcome, textViewFullName, textViewEmail, textViewDoB, textViewGender, textViewMobile;
    private ProgressBar progressBar;
    private String fullName, email, doB, gender, mobile;
    private ImageView imageView;
    private FirebaseAuth authProfile;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_acitivity);

        getSupportActionBar().setTitle("User Profile");

        textViewWelcome=findViewById(R.id.textView_show_welcome);
        textViewFullName= findViewById(R.id.textView_show_full_name);
        textViewEmail=findViewById(R.id.textView_show_email);
        textViewDoB=findViewById(R.id.textView_show_dob);
        textViewGender=findViewById(R.id.textView_show_gender);
        textViewMobile=findViewById(R.id.textView_show_mobile);
        progressBar=findViewById(R.id.progressBar);
        imageView=findViewById(R.id.imageView_profile_dp);

        // Set onClickListener on Imageview to open UploadProfilePictureActivity
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserAcitivity.this,UploadProfilePicture.class );
                startActivity(intent);
            }
        });
        authProfile=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if(firebaseUser == null){
            Toast.makeText(UserAcitivity.this, "Something went wrong! User details are not available at the moment", Toast.LENGTH_SHORT).show();

        } else{
            checkIfEmailVerified(firebaseUser);
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }



    }

    private void checkIfEmailVerified(FirebaseUser firebaseUser) {
        if(!firebaseUser.isEmailVerified()){
            showAlertDialog();
        }
    }
// Users comming to see UserActivity after successful registration
    private void showAlertDialog() {
        //Set up the Alert Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(UserAcitivity.this);
        builder.setTitle("Email not verified");
        builder.setMessage("Please verify your email now. You can not login without email verification next time");

        // open email app if user clicks/taps Continue button
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        //Create the AlertDialog
        AlertDialog alertDialog = builder.create();

        // Show the alertDialog
        alertDialog.show();
    }

    // Creating Action bar menu
    public boolean onCreateOptionMenu(Menu menu){
        //inflate menu items
        getMenuInflater().inflate(R.menu.common_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
       String userID= firebaseUser.getUid();
       // Extracting User Reference from db for registered users

        DatabaseReference referenceProfile= FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if(readUserDetails!=null){
                    fullName=readUserDetails.fullname;

                    email= firebaseUser.getEmail();
                    doB=readUserDetails.doB;
                    gender= readUserDetails.gender;
                    mobile= readUserDetails.mobile;

                    textViewWelcome.setText("Welcome, " + fullName + "!");
                    textViewFullName.setText(fullName);
                    textViewEmail.setText(email);
                    textViewDoB.setText(doB);
                    textViewGender.setText(gender);
                    textViewMobile.setText(mobile);

                    //Set Yser DP (After user has uploaded)
                    Uri uri = firebaseUser.getPhotoUrl();

                    //ImageViewer setImageURI() should not be used with regular URIS. So we are using Picasso.

                    Picasso.with(UserAcitivity.this).load(uri).into(imageView);

                } else {
                    Toast.makeText(UserAcitivity.this, "Something went wrong!",Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(UserAcitivity.this,"Something went wrong!",Toast.LENGTH_LONG).show();
            }
        });
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

        } else if(id == R.id.menu_flower_classification){
            Intent intent = new Intent(UserAcitivity.this, FlowerClassification.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_watch_video){
            Intent intent = new Intent(UserAcitivity.this, VideoActivity.class);
            startActivity(intent);
        }

        else if(id == R.id.menu_location){
            Intent intent = new Intent(UserAcitivity.this, LocationActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_reviews){
            Intent intent = new Intent(UserAcitivity.this, AddReviewActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_user_panel){
            Intent intent = new Intent(UserAcitivity.this, ShowPostActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_user_list){
            Intent intent = new Intent(UserAcitivity.this, UserListActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_flower_library){
            Intent intent = new Intent(UserAcitivity.this, FlowerLibraryActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_flower_dictionary){
            Intent intent = new Intent(UserAcitivity.this, FlowerDicActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_contact_us){
            Intent intent = new Intent(UserAcitivity.this, ContactUs.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_know_plant){
            Intent intent = new Intent(UserAcitivity.this, GraphQLMain.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_report){
            Intent intent = new Intent(UserAcitivity.this, GeneratePdfActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_sign_out){
            authProfile.signOut();
            Toast.makeText(UserAcitivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UserAcitivity.this, MainActivity.class);

            // Clear stack to prevent user coming back to UserActivity on pressing back button signing out
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Closer UserActivity after signing out
        } else{
            Toast.makeText(UserAcitivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }

       return super.onOptionsItemSelected(item);
    }
}