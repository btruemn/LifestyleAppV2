package com.msd.lifestyleapp.model;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class UserRepository {

    private UserDao userDao;
    private LiveData<List<String>> userNames;
    private LiveData<User> currentUser;
//    private LiveData<Boolean> usersExist;

    UserRepository(Application application) {
        UserRoomDatabase db = UserRoomDatabase.getDatabase(application);
        userDao = db.userDao();
        userNames = userDao.getNames();
    }


    public LiveData<List<String>> getAllUserNames() {
        return userNames;
    }

//    public LiveData<Boolean> nameAlreadyExists(String name) {
//        userNames = userDao.getNames();
//        return new MutableLiveData<>(userNames.getValue().contains(name));
//    }

//    public LiveData<Boolean> usersExist(){
//        List<String> users = userNames.getValue();
//        if(users == null) return new MutableLiveData<>(false);
//        else return new MutableLiveData<>(!userNames.getValue().isEmpty());
//    }

    LiveData<User> getCurrentUser(String name) {
        currentUser = userDao.findByName(name);
        return currentUser;
    }

    public void insert(User user) {
//        userDao.insert(user);
        new insertAsyncTask(userDao, this).execute(user);
    }

    public void update(User user) {
        new updateAsyncTask(userDao, this).execute(user);
//        userDao.update(user);
    }

    public void delete(User currentUser) {
//        userDao.delete(currentUser);
        new deleteAsyncTask(userDao, this).execute(currentUser);
    }

    public void deleteByUserName(String username) {
        new deleteByUserNameAsyncTask(userDao, this).execute(username);
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
//            System.out.println("IN BACKGROUND: " + params[0].toString());
            mAsyncTaskDao.insert(params[0]);
            return null;
        }

//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            userRepositoryWeakReference.get().userNames = mAsyncTaskDao.getNames();
//        }
    }

    private class updateAsyncTask extends AsyncTask<User, Void, Void> {

        private UserDao mAsyncTaskDao;

        updateAsyncTask(UserDao dao, UserRepository repository) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(User... users) {
            mAsyncTaskDao.update(users[0]);
            return null;
        }
    }

    private class deleteAsyncTask extends AsyncTask<User, Void, Void>{
        private UserDao mAsyncTaskDao;

        deleteAsyncTask(UserDao dao, UserRepository repository) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(User... users) {
            mAsyncTaskDao.delete(users[0]);
            return null;
        }
    }

    private class deleteByUserNameAsyncTask extends AsyncTask<String, Void, Void>{
        private UserDao mAsyncTaskDao;

        public deleteByUserNameAsyncTask(UserDao mAsyncTaskDao, UserRepository repository) {
            this.mAsyncTaskDao = mAsyncTaskDao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            mAsyncTaskDao.deleteByUserName(strings[0]);
            return null;
        }
    }
}
