package com.msd.lifestyleapp.model;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class UserViewModel extends AndroidViewModel {

    private UserRepository mRepository;
    private LiveData<List<String>> userNames;
//    private User activeUser;

    public UserViewModel(Application application) {
        super(application);
        mRepository = new UserRepository(application);
    }

    public LiveData<User> getActiveUser(String name){
        return mRepository.getActiveUser(name);
    }

    public void insert(User user) {
        mRepository.insert(user);
    }

    public void update(User user){
        mRepository.update(user);
    }

    public LiveData<List<String>> getAllUserNames(){
        return mRepository.getAllUserNames();
    }

//    public LiveData<Boolean> usersExist(){
//        return mRepository.usersExist();
//    }

    public LiveData<Boolean> nameAlreadyExists(String name){
        return mRepository.nameAlreadyExists(name);
    }

}
