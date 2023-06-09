package tech.tanztalks.android.myfirebaseapp.models;

import com.google.gson.annotations.SerializedName;

public class Wallpaper {

    @SerializedName("src")
    private ImageDimensions src;

    public Wallpaper(ImageDimensions src) {
        this.src = src;

    }

    public ImageDimensions getSrc() {
        return src;
    }

    public void setSrc(ImageDimensions src) {
        this.src = src;
    }



}
