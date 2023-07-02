package tech.tanztalks.android.myfirebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tech.tanztalks.android.myfirebaseapp.adapters.FlowerAdapter;
import tech.tanztalks.android.myfirebaseapp.models.FlowerResponseModel;
import tech.tanztalks.android.myfirebaseapp.service.FlowerService;

public class FlowerLibraryActivity extends AppCompatActivity {
    public static final String BASE_URL = "https://services.hanselandpetal.com";
    private FlowerService service;
    ListView listView;
    private List<FlowerResponseModel> responseModels;
    private FlowerAdapter flowerAdapter;
    ActionBar actionBar;
    private FirebaseAuth authProfile;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flower_library);
        listView = findViewById(R.id.listView);
        responseModels = new ArrayList<>();

        actionBar = getSupportActionBar();
        actionBar.setTitle("Flower Library");

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);



        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        service = retrofit.create(FlowerService.class);
        Call<List<FlowerResponseModel>> apiCall = service.getAllFlowers();
        apiCall.enqueue(new Callback<List<FlowerResponseModel>>() {
            @Override
            public void onResponse(Call<List<FlowerResponseModel>> call, Response<List<FlowerResponseModel>> response) {
             if (response.code()==200){
                 responseModels = response.body();
                 Log.e("onResponse", responseModels.toString());
                 flowerAdapter = new FlowerAdapter(FlowerLibraryActivity.this, responseModels);
                 listView.setAdapter(flowerAdapter);
             }

            }

            @Override
            public void onFailure(Call<List<FlowerResponseModel>> call, Throwable t) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                FlowerResponseModel response = responseModels.get(position);
                startActivity(new Intent(FlowerLibraryActivity.this, DetailsActivity.class).putExtra("flowers", (response)));
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
            Intent intent = new Intent(FlowerLibraryActivity.this, FlowerClassification.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_watch_video){
            Intent intent = new Intent(FlowerLibraryActivity.this, VideoActivity.class);
            startActivity(intent);
        }

        else if(id == R.id.menu_location){
            Intent intent = new Intent(FlowerLibraryActivity.this, LocationActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_reviews){
            Intent intent = new Intent(FlowerLibraryActivity.this, AddReviewActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_user_panel){
            Intent intent = new Intent(FlowerLibraryActivity.this, ShowPostActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_user_list){
            Intent intent = new Intent(FlowerLibraryActivity.this, UserListActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_flower_library){
            Intent intent = new Intent(FlowerLibraryActivity.this, FlowerLibraryActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_flower_dictionary){
            Intent intent = new Intent(FlowerLibraryActivity.this, FlowerDicActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.menu_contact_us) {
            Intent intent = new Intent(FlowerLibraryActivity.this, ContactUs.class);
            startActivity(intent);
        }
        else if (id == R.id.menu_know_plant) {
            Intent intent = new Intent(FlowerLibraryActivity.this, GraphQLMain.class);
            startActivity(intent);
        }

        else if (id == R.id.menu_report) {
            Intent intent = new Intent(FlowerLibraryActivity.this, GeneratePdfActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_sign_out){
            authProfile.signOut();
            Toast.makeText(FlowerLibraryActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(FlowerLibraryActivity.this, MainActivity.class);

            // Clear stack to prevent user coming back to UserActivity on pressing back button signing out
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Closer UserActivity after signing out
        } else{
            Toast.makeText(FlowerLibraryActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}