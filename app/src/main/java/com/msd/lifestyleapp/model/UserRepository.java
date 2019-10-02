package com.msd.lifestyleapp.model;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class UserRepository {

    private UserDao userDao;
    private LiveData<List<User>> allUsers;
    private LiveData<User> activeUser;

    UserRepository(Application application){
        UserRoomDatabase db = UserRoomDatabase.getDatabase(application);
        userDao = db.userDao();
        allUsers = userDao.getAll();
    }

    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }

    public LiveData<List<String>> getAllUserNames(){
        List<String> names = new ArrayList<>();
        if(allUsers.getValue() != null) {
            for (User user : allUsers.getValue()) {
                names.add(user.getName());
            }
        }
        return new MutableLiveData(names);
    }

    public LiveData<Boolean> nameAlreadyExists(String name){
        return new MutableLiveData<>(getAllUserNames().getValue().contains(name));
    }

    public LiveData<Boolean> usersExist(){
        List<User> users = allUsers.getValue();
        if(users == null) return new MutableLiveData<>(false);
        else return new MutableLiveData<>(!allUsers.getValue().isEmpty());
    }

    LiveData<User> getActiveUser(String name){
        activeUser = userDao.findByName(name);
        return activeUser;
    }

    public void insert (User user) {
//        userDao.insert(user);
        new insertAsyncTask(userDao, this).execute(user);
    }

    public void update(User user) {
        userDao.update(user);
    }

    private static class insertAsyncTask extends AsyncTask<User, Void, Void> {

        private UserDao mAsyncTaskDao;
        private WeakReference<UserRepository> userRepositoryWeakReference;


        insertAsyncTask(UserDao dao, UserRepository repository) {
            mAsyncTaskDao = dao;
            userRepositoryWeakReference = new WeakReference<>(repository);

        }

        @Override
        protected Void doInBackground(final User... params) {
            System.out.println("IN BACKGROUND: " + params[0].toString());
            mAsyncTaskDao.insert(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            userRepositoryWeakReference.get().allUsers = mAsyncTaskDao.getAll();
            String daoGetAll = mAsyncTaskDao.getAll().getValue().toString();
            String users = userRepositoryWeakReference.get().allUsers.getValue().toString();
            System.out.printf("UPDATED allUSERS: " + users);
        }
    }

}
