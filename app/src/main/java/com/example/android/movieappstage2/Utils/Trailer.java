package com.example.android.movieappstage2.Utils;

/**
 * Created by mina essam on 24-Apr-17.
 */

public class Trailer {
    private String name;
    private String key;
    private String quality;
    public Trailer(String name,String key,String quality){
        this.name=name;
        this.key=key;
        this.quality=quality;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public String getQuality() {
        return quality;
    }
}
