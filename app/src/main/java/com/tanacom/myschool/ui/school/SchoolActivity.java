package com.tanacom.myschool.ui.school;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.tanacom.myschool.R;
import com.tanacom.myschool.databinding.ActivitySchoolBinding;
import com.tanacom.myschool.persistence.DatabaseHelper;
import com.tanacom.myschool.services.LocationService;
import com.tanacom.myschool.ui.MainActivity;
import com.tanacom.myschool.ui.school.model.SchoolData;
import com.tanacom.myschool.ui.school.model.SchoolPojo;
import com.tanacom.myschool.util.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import br.com.ilhasoft.support.validation.Validator;

import static com.tanacom.myschool.util.Utils.bitmapToBase64;
import static com.tanacom.myschool.util.Utils.isLocation;

public class SchoolActivity extends AppCompatActivity {


    public static String TAG = SchoolActivity.class.getSimpleName();
    static final int REQUEST_CAMERA = 100;
    private ActivitySchoolBinding binding;
    private DatePickerDialog.OnDateSetListener date;
    private Calendar calendar;
    private String imageString = "";
    private Validator validator;
    private SchoolData model;
    private DatabaseHelper db;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

    }

    @Override
    protected void onStart() {
        super.onStart();

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                binding.schoolLogitude.setText(intent.getStringExtra(Constants.COMMUNITY_LONGITUDE));
                binding.schoolLatitude.setText(intent.getStringExtra(Constants.COMMUNITY_LATITUDE));
            }
        };

        registerReceiver(broadcastReceiver, new IntentFilter(LocationService.BROADCAST_ACTION));

    }

    // init method.
    private void init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_school);

        model = new SchoolData();

        validator = new Validator(binding);

        db = new DatabaseHelper(this);

        SchoolPojo pojo = new SchoolPojo();

        binding.setSchool(pojo);

        setDate();

        setOnClickListeners();

    }

    // setOnClickListeners.
    private void setOnClickListeners() {

        binding.fotoBkg.setOnClickListener(v -> setCamera());

        binding.schoolStatus.setOnClickListener(this::openSchoolStatus);

        binding.dateFounded.setOnClickListener(v -> pickerDate());

        binding.schoolCountry.setOnClickListener(v -> {
        });

        binding.schoolRegion.setOnClickListener(v -> {
        });


        binding.submitButton.setOnClickListener(v -> saveData());

    }

    //  save Data to Sqlite.
    private void saveData() {
        binding.progressBar.setVisibility(View.VISIBLE);

        model.setSchoolImage(imageString);

        if (validator.validate() && !imageString.isEmpty()) {

            model.setSchoolName(Objects.requireNonNull(binding.schoolName.getText()).toString());
            model.setSchoolRegion(Objects.requireNonNull(binding.schoolRegion.getText()).toString());
            model.setSchoolCountry(Objects.requireNonNull(binding.schoolCountry.getText()).toString());
            model.setDateFounded(Objects.requireNonNull(binding.dateFounded.getText()).toString());
            model.setSchoolStatus(Objects.requireNonNull(binding.schoolStatus.getText()).toString());
            model.setSchoolLatitude(Double.valueOf(Objects.requireNonNull(binding.schoolLatitude.getText()).toString()));
            model.setSchoolLongitude(Double.valueOf(Objects.requireNonNull(binding.schoolLogitude.getText()).toString()));

            db.insertSchoolDB(model);

            startActivity(new Intent(this, MainActivity.class));
            finish();

        } else {

            binding.progressBar.setVisibility(View.GONE);

            Toast.makeText(getApplicationContext(), "All fields required", Toast.LENGTH_LONG).show();

        }


    }


    // Open School Status Dialog.
    public void openSchoolStatus(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.school_status)
                .setItems(R.array.schoolStatus, (dialog, which) -> {

                    if (which == 0) {
                        model.setSchoolStatus("PRIVATE");
                        binding.schoolStatus.setText("PRIVATE");
                    } else if (which == 1) {
                        model.setSchoolStatus("GOVERNMENT");
                        binding.schoolStatus.setText("GOVERNMENT");
                    } else {
                        model.setSchoolStatus("NGO");
                        binding.schoolStatus.setText("NGO");

                    }

                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //  Pick Date method.
    private void pickerDate() {
        new DatePickerDialog(this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (isLocation(this)) {
            Intent locintent = new Intent(this, LocationService.class);
            startService(locintent);

            Toast.makeText(getApplicationContext(), "Getting location in background, Please wait", Toast.LENGTH_LONG).show();


        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Enable Permission")
                    .setPositiveButton("Location services turned off.\n Click me to turn on location services.", (dialogInterface, i) -> {
                        {
                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(myIntent);

                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();

        }
    }

    // Set Date Method.
    private void setDate() {
        calendar = Calendar.getInstance();
        date = (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
            binding.dateFounded.setText(sdf.format(calendar.getTime()));

        };
    }

    // Set Camera
    private void setCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(intent, REQUEST_CAMERA);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "Camera Unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            binding.schoolPhoto.setImageBitmap(imageBitmap);
            imageString = bitmapToBase64(imageBitmap);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
            broadcastReceiver = null;
        }


    }

}