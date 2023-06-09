package tech.tanztalks.android.myfirebaseapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.tanztalks.android.myfirebaseapp.Api.RetrofitClient;
import tech.tanztalks.android.myfirebaseapp.models.Wallpaper;
import tech.tanztalks.android.myfirebaseapp.models.WallpaperAdapter;
import tech.tanztalks.android.myfirebaseapp.models.WallpaperResponse;

public class GalleryActivity extends AppCompatActivity {

    private RecyclerView imageRecyclerview;
    private final String API_KEY = "brWPOKvR3n0rwzoukUDaBCMrLziUPEhKfGbuU9gHKJ3bhBHd8hEUhdBP";
    private int pageCount = 1;
    private static int perPage =80;
    private List<Wallpaper> wallpaperList= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        //getSupportActionBar().setTitle("Gallery");
        getSupportActionBar().hide();
        initRecyclerview();
    }

    private void initRecyclerview() {
        imageRecyclerview = findViewById(R.id.recycler);
        imageRecyclerview.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        imageRecyclerview.setLayoutManager(gridLayoutManager);
        fetchData(pageCount);
    }

    private void fetchData(int pageCount) {
        Call<WallpaperResponse> call = RetrofitClient.getInstance().getApi().getWallpaper(API_KEY, pageCount, perPage);
        call.enqueue(new Callback<WallpaperResponse>() {
            @Override
            public void onResponse(Call<WallpaperResponse> call, Response<WallpaperResponse> response) {
                WallpaperResponse wallpaperResponse = response.body();
                if (response.isSuccessful() && null != wallpaperResponse) {
                    wallpaperList.addAll(wallpaperResponse.getPhotosList());
                    WallpaperAdapter wallpaperAdapter = new WallpaperAdapter(getApplicationContext(), wallpaperList);
                    imageRecyclerview.setAdapter(wallpaperAdapter);
                    wallpaperAdapter.notifyDataSetChanged();
                }
                else
                    Toast.makeText(GalleryActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<WallpaperResponse> call, Throwable t) {
                Toast.makeText(GalleryActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });


 }
}