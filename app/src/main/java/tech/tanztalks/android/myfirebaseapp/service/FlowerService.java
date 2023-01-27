package tech.tanztalks.android.myfirebaseapp.service;

import java.util.ArrayList;
import java.util.List;

import retrofit2.http.GET;
import retrofit2.Call;

import tech.tanztalks.android.myfirebaseapp.models.FlowerResponseModel;

public interface FlowerService {
    @GET("feeds/flowers.json")
     Call<List<FlowerResponseModel>> getAllFlowers();
}
