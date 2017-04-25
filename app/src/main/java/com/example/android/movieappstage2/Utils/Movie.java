package com.example.android.movieappstage2.Utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mina essam on 23-Apr-17.
 */

public class Movie implements Parcelable {
    private String title,description, posterPath,date,language;
    private float vote,movieID;
    private boolean adult;


    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public float getMovieID() {
        return movieID;
    }

    public void setMovieID(float movieID) {
        this.movieID = movieID;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }
    public void setAdult(String adult){
        if(adult.equals("true")){
            this.adult=true;
        }
        else {
            this.adult=false;
        }
    }

    public Movie(String title, String description, float vote, String posterPath, String date, String language, float movieID,
                 String adult){
        this.title=title;
        this.description=description;
        this.vote=vote;
        this.posterPath = posterPath;
        this.date=date;
        this.language=language;
        this.movieID=movieID;
        setAdult(adult);

    }

    public String getDate() {
        return date;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getVote() {
        return vote;
    }

    public void setVote(float vote) {
        this.vote = vote;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.title);
        parcel.writeString(this.description);
        parcel.writeFloat(this.vote);
        parcel.writeString(this.posterPath);
        parcel.writeString(this.date);
        parcel.writeString(this.language);
        parcel.writeFloat(this.movieID);
        boolean[] arr={this.adult};
        parcel.writeBooleanArray(arr);

    }

    private Movie(Parcel parcel){
        this.title=parcel.readString();
        this.description=parcel.readString();
        this.vote=parcel.readFloat();
        this.posterPath=parcel.readString();
        this.date=parcel.readString();
        this.language=parcel.readString();
        this.movieID=parcel.readFloat();
        boolean[] arr=new boolean[1];
        parcel.readBooleanArray(arr);
        this.adult=arr[0];
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

}
