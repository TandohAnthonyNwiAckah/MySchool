package com.tanacom.myschool.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tanacom.myschool.R;
import com.tanacom.myschool.databinding.ActivityMainBinding;
import com.tanacom.myschool.ui.school.SchoolActivity;
import com.tanacom.myschool.ui.school.SchoolAdapter;
import com.tanacom.myschool.ui.school.SchoolViewModel;
import com.tanacom.myschool.ui.school.SchoolViewModelFactory;
import com.tanacom.myschool.ui.school.model.SchoolData;

import java.util.ArrayList;

import static com.tanacom.myschool.util.Utils.checkPermissions;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding = null;
    private static final String TAG = MainActivity.class.getSimpleName();
    private SchoolViewModel viewModel;
    private SchoolAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        init();
    }

    private void init() {
        setupToolbar();
        setupRecyclerViewAdapter();
        setupViewModel();
        setupClickListeners();


        // Check permission
        checkPermissions(this);


    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Home");
    }

    private void setupRecyclerViewAdapter() {
        adapter = new SchoolAdapter(MainActivity.this, getSupportFragmentManager(), this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this, new SchoolViewModelFactory(getApplication())).get(SchoolViewModel.class);

        viewModel.getSchoolList().observe(this, schools -> {
            adapter.updateList((ArrayList<SchoolData>) schools);
            hideEmptyCommunityList();
        });
    }

    private void setupClickListeners() {
        binding.fab.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SchoolActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshList();

    }

    public void refreshList() {
        adapter.updateList((ArrayList<SchoolData>) viewModel.getSchoolList().getValue());
        hideEmptyCommunityList();
    }

    public void hideEmptyCommunityList() {
        if (adapter.getItemCount() == 0) {
            binding.animationView.setVisibility(View.VISIBLE);
            binding.swipeContainer.setVisibility(View.GONE);
        } else {
            binding.animationView.setVisibility(View.GONE);
            binding.swipeContainer.setVisibility(View.VISIBLE);
        }
    }


}