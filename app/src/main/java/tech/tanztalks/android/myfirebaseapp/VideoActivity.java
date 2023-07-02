package tech.tanztalks.android.myfirebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class VideoActivity extends AppCompatActivity {

    private YouTubePlayerView youTubePlayerView;
    private FirebaseAuth authProfile;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        //getSupportActionBar().setTitle("Youtube Video");
        actionBar = getSupportActionBar();
        actionBar.setTitle("Watch Video");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        youTubePlayerView = findViewById(R.id.activity_main_youtubePlayerView);
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = "i810CxN5Q6Q";
                youTubePlayer.cueVideo(videoId, 0);
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

//        } else if(id == R.id.menu_change_password){
//            Intent intent = new Intent(UserAcitivity.this, ChangePasswordActivity.class);
//            startActivity(intent);

        } else if(id == R.id.menu_flower_classification){
            Intent intent = new Intent(VideoActivity.this, FlowerClassification.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_watch_video){
            Intent intent = new Intent(VideoActivity.this, VideoActivity.class);
            startActivity(intent);
        }

        else if(id == R.id.menu_location){
            Intent intent = new Intent(VideoActivity.this, LocationActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_reviews){
            Intent intent = new Intent(VideoActivity.this, AddReviewActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_user_panel){
            Intent intent = new Intent(VideoActivity.this, ShowPostActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_user_list){
            Intent intent = new Intent(VideoActivity.this, UserListActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_flower_library){
            Intent intent = new Intent(VideoActivity.this, FlowerLibraryActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_flower_library){
            Intent intent = new Intent(VideoActivity.this, FlowerDicActivity.class);
            startActivity(intent);
        }  else if (id == R.id.menu_contact_us) {
            Intent intent = new Intent(VideoActivity.this, ContactUs.class);
            startActivity(intent);
        }
        else if (id == R.id.menu_know_plant) {
            Intent intent = new Intent(VideoActivity.this, GraphQLMain.class);
            startActivity(intent);
        }
        else if (id == R.id.menu_report) {
            Intent intent = new Intent(VideoActivity.this, GeneratePdfActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_sign_out){
            authProfile.signOut();
            Toast.makeText(VideoActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(VideoActivity.this, MainActivity.class);

            // Clear stack to prevent user coming back to UserActivity on pressing back button signing out
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Closer UserActivity after signing out
        } else{
            Toast.makeText(VideoActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }


}