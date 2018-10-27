package com.example.gurteksingh.themoviedb;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cast {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("character")
    @Expose
    private String character;

    @SerializedName("profile_path")
    @Expose
    private String profilePath;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String id) {
        this.character = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePath() {
        return "https://image.tmdb.org/t/p/w185"+ profilePath;
    }

    public void setProfilePath(String posterPath) {
        this.profilePath = posterPath;
    }

    @SerializedName("birthday")
    @Expose
    private String birthdate;

    @SerializedName("place_of_birth")
    @Expose
    private String birthplace;

    @SerializedName("biography")
    @Expose
    private String biography;

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String id) {
        this.birthdate = id;
    }

    public String getBirthplace() {
        return birthplace;
    }
    public void setBirthplace(String id) {
        this.birthplace = id;
    }


    public String getBiography() {
        return biography;
    }
    public void setBiography(String id) {
        this.biography = id;
    }

}

