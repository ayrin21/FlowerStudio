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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

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
}