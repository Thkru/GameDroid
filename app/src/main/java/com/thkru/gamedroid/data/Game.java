package com.thkru.gamedroid.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Game implements Parcelable {

    @SerializedName("id")
    @Expose
    public Integer id;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("slug")
    @Expose
    public String slug;

    @SerializedName("release_date")
    @Expose
    public String releaseDate;

    @SerializedName("cover")
    @Expose
    public String cover;

    @SerializedName("cover_id")
    @Expose
    public String coverId;
    private String hash;

    public String getHash() {
        return coverId;
    }

    public String getName() {
        return name;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public int getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return id;
    }

    // Parcel stuff

    @Override
    public void writeToParcel(Parcel parcel, int i) { //could have used Android Parceler lib instead
        parcel.writeInt(this.id);
        parcel.writeString(this.name);
        parcel.writeString(this.releaseDate);
        parcel.writeString(this.coverId);
    }

    private Game(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.releaseDate = in.readString();
        this.coverId = in.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

}
