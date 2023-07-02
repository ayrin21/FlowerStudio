package tech.tanztalks.android.myfirebaseapp;

public class Plant {
    // Retrofit2Model tried for graphql

    private String name;
    private String description;

    public Plant(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
    }


