package com.example.esercitazionebonus;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class AppSessione implements Parcelable{
    ArrayList<User> users;
    User actualUser;

    public AppSessione(ArrayList<User> users, User actualUser) {
        this.users = users;
        this.actualUser = actualUser;
    }

    public User getUser(String username) {
        for (User user : users) {
            if (user.username.equals(username)) {
                return user;
            }
        }
        return null;
    }


    public AppSessione() {
        if (users == null) {
            this.users = new ArrayList<>();
            users.add(new User("admin", "admin", "Cagliari", "0\\0\\0", true));

        }
    }

    protected AppSessione(Parcel in) {
        users = in.createTypedArrayList(User.CREATOR);
        actualUser = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<AppSessione> CREATOR = new Creator<AppSessione>() {
        @Override
        public AppSessione createFromParcel(Parcel in) {
            return new AppSessione(in);
        }

        @Override
        public AppSessione[] newArray(int size) {
            return new AppSessione[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(users);
        dest.writeParcelable(actualUser, flags);
    }
}
