package tech.tanztalks.android.myfirebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.jar.JarException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GalleryActivity extends AppCompatActivity {
    //Initilize
    NestedScrollView nestedScrollView;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    ArrayList<MainData> dataArrayList = new ArrayList<MainData>();
    MainAdapter adapter;
    int page = 1, limit = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        // Assign
        nestedScrollView = findViewById(R.id.scroll_view);
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progress_bar);
        //Initialize adapter
        adapter = new MainAdapter(GalleryActivity.this, dataArrayList);
        // set layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // set adapter
        recyclerView.setAdapter(adapter);

        //create get data method
        getData(page, limit);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                //check condition
                if (scrollY==v.getChildAt(0).getMeasuredHeight()-v.getMeasuredHeight()){
                    //when reach last item position
                    //increase page size
                    page++;
                            //show progress bar
                    progressBar.setVisibility(View.VISIBLE);
                    //call method
                    getData(page, limit);
                }
            }
        });

    }

    private void getData(int page, int limit) {
        //Initilize retrofit
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://picsum.photos/").addConverterFactory(GsonConverterFactory.create()).build();
        //create main interface
        MainInterface mainInterface = retrofit.create(MainInterface.class);
        Call<String> call = mainInterface.STRING_CALL(page, limit);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                // check condition
                if(response.isSuccessful() && response.body() != null){
                    // When response is succesfull and not empty
                    //Hide progress bar
                    progressBar.setVisibility(View.GONE);
                    try {
                        JSONArray jsonArray = new JSONArray(response.body());
                        //Parse json array
                        parseResult(jsonArray);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void parseResult(JSONArray jsonArray) {
        //use for loop
        for (int i = 0; i<jsonArray.length(); i++){
            try {
                //Initialize json object
                JSONObject object = jsonArray.getJSONObject(i);
                //Initialize main data
                MainData data = new MainData();
                //Set image
                data.setImage(object.getString("download_url"));
                //set name
                data.setName(object.getString("author"));
                //add data in array list
                dataArrayList.add(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //initialize adapter
            adapter = new MainAdapter(GalleryActivity.this, dataArrayList);
            recyclerView.setAdapter(adapter);

        }
    }
}