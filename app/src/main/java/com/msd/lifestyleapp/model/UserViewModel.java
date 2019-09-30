package com.msd.lifestyleapp.model;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class UserViewModel extends AndroidViewModel {

    private UserRepository mRepository;
    private LiveData<List<User>> allUsers;

    public UserViewModel(Application application) {
        super(application);
        mRepository = new UserRepository(application);
        allUsers = mRepository.getAllUsers();
    }

    LiveData<List<User>> getAllUsers() {
        return allUsers;
    }

    public void insert(User user) {
        mRepository.insert(user);
    }
}
