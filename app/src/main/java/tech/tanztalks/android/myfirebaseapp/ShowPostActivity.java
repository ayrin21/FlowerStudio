package tech.tanztalks.android.myfirebaseapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ShowPostActivity extends AppCompatActivity {
    private FirebaseAuth authProfile;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_post);

        //getSupportActionBar().setTitle("Post");
        actionBar = getSupportActionBar();
        actionBar.setTitle("Post");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

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
            Intent intent = new Intent(ShowPostActivity.this, FlowerClassification.class);
            startActivity(intent);

        }
        else if(id == R.id.menu_watch_video){
            Intent intent = new Intent(ShowPostActivity.this, VideoActivity.class);
            startActivity(intent);
        }

        else if(id == R.id.menu_location){
            Intent intent = new Intent(ShowPostActivity.this, LocationActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_reviews){
            Intent intent = new Intent(ShowPostActivity.this, AddReviewActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_user_panel){
            Intent intent = new Intent(ShowPostActivity.this, ShowPostActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_user_list){
            Intent intent = new Intent(ShowPostActivity.this, UserListActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_flower_library){
            Intent intent = new Intent(ShowPostActivity.this, FlowerLibraryActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_flower_dictionary){
            Intent intent = new Intent(ShowPostActivity.this, FlowerDicActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.menu_contact_us) {
            Intent intent = new Intent(ShowPostActivity.this, ContactUs.class);
            startActivity(intent);
        }
        else if (id == R.id.menu_know_plant) {
            Intent intent = new Intent(ShowPostActivity.this, GraphQLMain.class);
            startActivity(intent);
        }
        else if (id == R.id.menu_report) {
            Intent intent = new Intent(ShowPostActivity.this, ReportActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_sign_out){
            authProfile.signOut();
            Toast.makeText(ShowPostActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ShowPostActivity.this, MainActivity.class);

            // Clear stack to prevent user coming back to UserActivity on pressing back button signing out
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Closer UserActivity after signing out
        } else{
            Toast.makeText(ShowPostActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

}