package com.thkru.gamedroid.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Game implements Parcelable {

    private final int id;  //correct me if I'm wrong, but this one is unique and can be used as primary key...?
    private final String name;
    private final String releaseDate;
    private final String text;
    private final String dev;

    public Game(int id, String name, String releaseDate, String text, String developer) {
        this.id = id;
        this.name = name;
        this.releaseDate = releaseDate;
        this.text = text;
        this.dev = developer;
    }

    // Parcelable stuff
    @Override
    public void writeToParcel(Parcel parcel, int i) { //could have used Android Parceler lib instead
        parcel.writeInt(this.id);
        parcel.writeString(this.name);
        parcel.writeString(this.releaseDate);
        parcel.writeString(this.text);
        parcel.writeString(this.dev);
    }

    private Game(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.releaseDate = in.readString();
        this.text = in.readString();
        this.dev = in.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    @Override
    public String toString() {
        return name;
    }

    //getters

    @Override
    public int describeContents() {
        return 0;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getText() {
        return text;
    }

    public String getDev() {
        return dev;
    }

}
