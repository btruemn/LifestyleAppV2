package com.msd.lifestyleapp.model;

import android.app.Application;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;

public class UserRepository {

    private UserDao userDao;
    private LiveData<List<User>> allUsers;
    private LiveData<User> activeUser;

    UserRepository(Application application){
        UserRoomDatabase db = UserRoomDatabase.getDatabase(application);
        userDao = db.userDao();
        allUsers = userDao.getAll();
    }

    LiveData<List<User>> getAllUsers() {
        return allUsers;
    }

    LiveData<List<String>> getAllUserNames(){
        List<String> names = new ArrayList<>();
        for(User user: allUsers.getValue()){
            names.add(user.getName());
        }
        return (LiveData<List<String>>) names;
    }

    LiveData<User> getActiveUser(String name){
        activeUser = userDao.findByName(name);
        return activeUser;
    }

    public void insert (User user) {
        new insertAsyncTask(userDao).execute(user);
    }

    public void update(User user) {
        userDao.update(user);
    }

    private static class insertAsyncTask extends AsyncTask<User, Void, Void> {

        private UserDao mAsyncTaskDao;

        insertAsyncTask(UserDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final User... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

}
