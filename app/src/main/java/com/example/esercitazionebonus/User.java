package com.example.esercitazionebonus;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    String username;
    String password;
    String city;
    String birthday;
    String photoUri;
    boolean isAdmin;

    public User(String username, String password, String city, String birthday, boolean isAdmin) {
        this.isAdmin = isAdmin;
        this.password = password;
        this.username = username;
        this.city = city;
        this.birthday = birthday;
        photoUri = "";
    }

    protected User(Parcel in) {
        username = in.readString();
        password = in.readString();
        city = in.readString();
        birthday = in.readString();
        photoUri = in.readString();
        isAdmin = in.readByte() != 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(city);
        dest.writeString(birthday);
        dest.writeString(photoUri);
        dest.writeByte((byte) (isAdmin ? 1 : 0));
    }
}
