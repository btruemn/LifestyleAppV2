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
    private User currentUser;
    private MutableLiveData<String> username;

    public UserViewModel(Application application) {
        super(application);
        mRepository = new UserRepository(application);
        username = new MutableLiveData<>("blah");
    }

    public MutableLiveData<String> getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username.setValue(username);
    }



    public LiveData<User> getCurrentUser(String name) {
        return mRepository.getCurrentUser(name);
    }

    public void insert(User user) {
        mRepository.insert(user);
    }

    public void update(User user) {
        mRepository.update(user);
    }

    public LiveData<List<String>> getAllUserNames() {
        return mRepository.getAllUserNames();
    }

    public void delete(User currentUser) {
        mRepository.delete(currentUser);
    }

    public void deleteByUserName(String username) {
        mRepository.deleteByUserName(username);
    }


//    public LiveData<Boolean> nameAlreadyExists(String name) {
//        return mRepository.nameAlreadyExists(name);
//    }

}
