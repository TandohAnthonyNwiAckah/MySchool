package com.tanacom.myschool.ui.school;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.tanacom.myschool.R;
import com.tanacom.myschool.databinding.FragmentSchoolBinding;
import com.tanacom.myschool.persistence.DatabaseHelper;
import com.tanacom.myschool.ui.MainActivity;
import com.tanacom.myschool.ui.school.model.SchoolData;
import com.tanacom.myschool.ui.school.model.SchoolPojo;
import com.tanacom.myschool.util.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import br.com.ilhasoft.support.validation.Validator;

import static android.app.Activity.RESULT_OK;
import static com.tanacom.myschool.util.Utils.bitmapToBase64;


public class SchoolFragment extends DialogFragment {


    public static String TAG = SchoolFragment.class.getSimpleName();
    static final int REQUEST_CAMERA = 100;
    private static final String ARG_PARAM1 = "param1";
    private String localID = "";
    private String imageString = "";
    private SchoolData model;
    private Validator validator;
    private SchoolPojo schoolPojo;
    FragmentSchoolBinding binding;
    private DatePickerDialog.OnDateSetListener date;
    private Calendar calendar;
    private DatabaseHelper db;

    public SchoolFragment() {
    }

    public static SchoolFragment newInstance(String param1) {
        SchoolFragment fragment = new SchoolFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            localID = getArguments().getString(ARG_PARAM1);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_school, container, false);

        init();

        return binding.getRoot();
    }

    private void init() {

        validator = new Validator(binding);

        schoolPojo = new SchoolPojo();

        model = new SchoolData();

        db = new DatabaseHelper(getActivity());

        binding.setSchool(setSchoolData(db.getSchoolDB(localID)));

        binding.schoolPhoto.setImageBitmap(Utils.base64ToBitmap(imageString));

        Objects.requireNonNull(getDialog()).setTitle("Edit School Information");

        setDate();

        setOnClickListeners();

    }

    // Set OnClickListeners.
    private void setOnClickListeners() {
        binding.submitButton.setOnClickListener(v -> updateSchool());

        binding.fotoBkg.setOnClickListener(v -> setCamera());

        binding.schoolStatus.setOnClickListener(this::openSchoolStatus);

        binding.dateFounded.setOnClickListener(v -> pickerDate());

        binding.schoolRegion.setOnClickListener(v -> {

        });

        binding.schoolCountry.setOnClickListener(v -> {

        });

    }

    // Open School Status Dialog.
    public void openSchoolStatus(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
        new DatePickerDialog(getContext(), date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
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

    private void setCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(intent, REQUEST_CAMERA);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(), "Camera Unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            binding.schoolPhoto.setImageBitmap(imageBitmap);
            imageString = bitmapToBase64(imageBitmap);
        }
    }

    private SchoolPojo setSchoolData(SchoolData model) {

        imageString = model.getSchoolImage();

        schoolPojo.schoolName.set(model.getSchoolName());
        schoolPojo.schoolRegion.set(model.getSchoolRegion());
        schoolPojo.schoolCountry.set(model.getSchoolCountry());
        schoolPojo.schoolDateFounded.set(model.getDateFounded());
        schoolPojo.schoolStatus.set(model.getSchoolStatus());
        schoolPojo.schoolLatitude.set(String.valueOf(model.getSchoolLatitude()));
        schoolPojo.schoolLogitude.set(String.valueOf(model.getSchoolLongitude()));

        return schoolPojo;
    }

    //Update model method.
    public void updateSchool() {

        model.setSchoolImage(imageString);


        if (validator.validate() && !imageString.isEmpty()) {


            model.setSchoolName(Objects.requireNonNull(binding.schoolName.getText()).toString());
            model.setSchoolRegion(Objects.requireNonNull(binding.schoolRegion.getText()).toString());
            model.setSchoolCountry(Objects.requireNonNull(binding.schoolCountry.getText()).toString());
            model.setDateFounded(Objects.requireNonNull(binding.dateFounded.getText()).toString());
            model.setSchoolStatus(Objects.requireNonNull(binding.schoolStatus.getText()).toString());
            model.setSchoolLatitude(Double.valueOf(Objects.requireNonNull(binding.schoolLatitude.getText()).toString()));
            model.setSchoolLongitude(Double.valueOf(Objects.requireNonNull(binding.schoolLogitude.getText()).toString()));

            db.updateSchoolDB(model);

            startActivity(new Intent(getContext(), MainActivity.class));
            getActivity().finish();


        } else {

            Toast.makeText(getContext(), "All fields required", Toast.LENGTH_SHORT).show();

        }


    }

}