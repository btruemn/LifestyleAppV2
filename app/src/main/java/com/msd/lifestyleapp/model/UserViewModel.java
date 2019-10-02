package com.msd.lifestyleapp.model;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class UserViewModel extends AndroidViewModel {

    private UserRepository mRepository;
//    private LiveData<List<User>> allUsers;
//    private User activeUser;

    public UserViewModel(Application application) {
        super(application);
        mRepository = new UserRepository(application);
    }

    LiveData<List<User>> getAllUsers() {
        return mRepository.getAllUsers();
    }

    LiveData<User> getActiveUser(String name){
        return mRepository.getActiveUser(name);
    }

    public void insert(User user) {
        mRepository.insert(user);
    }

    public void update(User user){
        mRepository.update(user);
    }

}
