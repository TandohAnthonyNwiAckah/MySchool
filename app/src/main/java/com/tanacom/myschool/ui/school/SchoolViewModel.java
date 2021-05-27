package com.tanacom.myschool.ui.school;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tanacom.myschool.persistence.DatabaseHelper;
import com.tanacom.myschool.ui.school.model.SchoolData;

import java.util.List;

public class SchoolViewModel extends ViewModel {

    private MutableLiveData<List<SchoolData>> schoolList;
    private final Application app;
    DatabaseHelper db;


    public SchoolViewModel(@NonNull Application application) {
        app = application;
        schoolList = new MutableLiveData<>();
        db = new DatabaseHelper(application);
        loadSchoolList();
    }

    public LiveData<List<SchoolData>> getSchoolList() {
        db = new DatabaseHelper(app.getApplicationContext());
        loadSchoolList();
        if (schoolList == null) {
            schoolList = new MutableLiveData<>();
            loadSchoolList();
        }
        return schoolList;
    }

    public void loadSchoolList() {
        schoolList.setValue(db.getSchoolList());
    }

}
