package com.msd.lifestyleapp.model;


import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    LiveData<List<User>> getAll();

    @Query("SELECT * FROM user WHERE name LIKE :name LIMIT 1")
    LiveData<User> findByName(String name);

    @Insert
    void insert(User user);

    @Update
    LiveData<User> update(User user);

    @Delete
    void delete(User user);

//    @Query("SELECT * FROM user WHERE name IN (:userIds)")
//    List<User> loadAllByIds(int[] userIds);

//    @Insert
//    void insertAll(User... users);


}
