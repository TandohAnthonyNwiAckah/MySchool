package com.tanacom.myschool.ui.school;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SchoolViewModelFactory implements ViewModelProvider.Factory {

    private Application mApplication;


    public SchoolViewModelFactory(Application application) {
        mApplication = application;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new SchoolViewModel(mApplication);
    }

}