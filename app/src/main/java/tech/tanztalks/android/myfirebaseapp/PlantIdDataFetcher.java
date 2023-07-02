package tech.tanztalks.android.myfirebaseapp;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
// Retrofit2Model tried for graphql
public class PlantIdDataFetcher {
    private static final String API_URL = "https://api.plant.id/v2/identify";
    private static final String API_KEY = "ZMmNBikU6Q8AW35k6yfW7Xuz5wxWKMQLJugrIx2JYUiuCdVq5x";

    public static void identifyPlant(Bitmap imageBitmap, PlantIdentificationListener listener) {
        new IdentifyPlantTask(listener).execute(imageBitmap);
    }

    private static class IdentifyPlantTask extends AsyncTask<Bitmap, Void, Plant> {
        private final PlantIdentificationListener listener;

        public IdentifyPlantTask(PlantIdentificationListener listener) {
            this.listener = listener;
        }

        @Override
        protected Plant doInBackground(Bitmap... bitmaps) {
            Bitmap imageBitmap = bitmaps[0];
            try {
                // Convert the Bitmap to a Base64 encoded string
                String imageBase64 = convertBitmapToBase64(imageBitmap);

                // Create the request body JSON
                JSONObject requestBodyJson = new JSONObject();
                requestBodyJson.put("images", new JSONArray().put(imageBase64));
                requestBodyJson.put("identification_timeout", 80); // Set the identification timeout to 30 seconds

                // Create the HTTP client
                OkHttpClient client = new OkHttpClient();

                // Create the HTTP request
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestBodyJson.toString());
                Request request = new Request.Builder()
                        .url(API_URL)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Api-Key", API_KEY)
                        .post(requestBody)
                        .build();

                // Execute the request and get the response
                Response response = client.newCall(request).execute();

                // Check if the request was successful (HTTP 200)
                if (response.isSuccessful()) {
                    // Parse the response JSON
                    String responseString = response.body().string();
                    JSONObject jsonResponse = new JSONObject(responseString);

                    // Extract the identification result
                    JSONArray suggestionsArray = jsonResponse.getJSONArray("suggestions");
                    if (suggestionsArray.length() > 0) {
                        JSONObject suggestion = suggestionsArray.getJSONObject(0);
                        String plantName = suggestion.getString("plant_name");
                        String plantDescription = suggestion.getString("plant_details");
                        return new Plant(plantName, plantDescription);
                    }
                }
                else {
                    Log.d("PlantIdDataFetcher", "Response code: " + response.code());


                }
            } catch (IOException | JSONException e) {
                Log.e("PlantIdDataFetcher", "Error identifying plant: " + e.getMessage());
            }

            // Return a default identification result if there was an error
            return new Plant("Plant Identification Failed", "");
        }

        @Override
        protected void onPostExecute(Plant result) {
            if (listener != null) {
                listener.onPlantIdentified(result);
            }
        }
    }

    private static String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public interface PlantIdentificationListener {
        void onPlantIdentified(Plant plant);
    }
}
