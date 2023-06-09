package tech.tanztalks.android.myfirebaseapp.models;

import com.google.gson.annotations.SerializedName;

public class ImageDimensions {

        @SerializedName("medium")
        private String medium;

        @SerializedName("large")
        private String large;

        public ImageDimensions(String medium, String large) {
            this.medium = medium;
            this.large = large;
        }

        public String getMedium() {
            return medium;
        }

        public void setMedium(String medium) {
            this.medium = medium;
        }

        public String getLarge() {
            return large;
        }

        public void setLarge(String large) {
            this.large = large;
        }

}
