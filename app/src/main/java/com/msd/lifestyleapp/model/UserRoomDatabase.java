package com.msd.lifestyleapp.model;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class UserRoomDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    private static volatile UserRoomDatabase userRoomInstance;


    //TODO: get existing db on device if it exists
    //create Singleton instance of DB
    public static UserRoomDatabase getDatabase(final Context context) {
        if (userRoomInstance == null) {
//            synchronized (userRoomInstance) {
                if (userRoomInstance == null) {
                    userRoomInstance = Room.databaseBuilder(context.getApplicationContext(), UserRoomDatabase.class, "user_database").build();
                }
            }
//        }
        return userRoomInstance;
    }


}

/* get an instance of the created db using the following code:
    AppDatabase db = Room.databaseBuilder(getApplicationContext(),
        AppDatabase.class, "database-name").build();
 */

/*
https://jacquessmuts.github.io/post/modularization_room/
https://medium.com/androiddevelopers/7-steps-to-room-27a5fe5f99b2
 */
