package tech.tanztalks.android.myfirebaseapp.Api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import tech.tanztalks.android.myfirebaseapp.models.WallpaperResponse;

public interface Api {
    @GET("curated")
    Call<WallpaperResponse> getWallpaper(

            @Header("Authorization") String credentials,
            @Query("page") int pageCount,
            @Query("per_page") int perPage
    );




}
