package com.tanacom.myschool.ui.school;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tanacom.myschool.R;
import com.tanacom.myschool.databinding.ItemSchoolBinding;
import com.tanacom.myschool.persistence.DatabaseHelper;
import com.tanacom.myschool.ui.MainActivity;
import com.tanacom.myschool.ui.school.model.SchoolData;

import java.util.ArrayList;

import static com.tanacom.myschool.util.Utils.base64ToBitmap;

public class SchoolAdapter extends RecyclerView.Adapter<SchoolAdapter.SchoolViewHolder> {

    private ArrayList<SchoolData> schoolList = new ArrayList<>();
    private final Context context;
    private String id = "";
    MainActivity activity;
    ItemSchoolBinding binding;
    FragmentManager fm;
    DatabaseHelper databaseHelper;


    public SchoolAdapter(Context context, FragmentManager fm, AppCompatActivity activity) {
        this.context = context;
        this.fm = fm;
        this.activity = (MainActivity) activity;
        this.databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public SchoolViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.item_school, viewGroup, false);
        return new SchoolViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SchoolViewHolder holder, int i) {

        SchoolData school = schoolList.get(i);

        holder.bind(school);

        binding.ripple.setOnClickListener(v -> {
            showPopupMenu(holder.itemView, school.getSchoolId(), holder.getAdapterPosition());
        });

    }

    @Override
    public int getItemCount() {
        return schoolList != null ? schoolList.size() : 0;
    }

    public void setSchoolList(ArrayList<SchoolData> schoolList) {
        this.schoolList = schoolList;
    }

    public void updateList(ArrayList<SchoolData> newList) {
        this.schoolList = newList;
        notifyDataSetChanged();
    }

    static class SchoolViewHolder extends RecyclerView.ViewHolder {

        private ItemSchoolBinding binding;

        public SchoolViewHolder(@NonNull ItemSchoolBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(SchoolData school) {

            binding.schoolName.setText(school.getSchoolName());
            binding.schoolRegion.setText(school.getSchoolRegion());
            binding.schoolStatus.setText(school.getSchoolStatus());
            binding.schoolCountry.setText(school.getSchoolCountry());
            binding.photo.setImageBitmap(base64ToBitmap(school.getSchoolImage()));

            binding.executePendingBindings();
        }

    }

    private void showPopupMenu(View view, int localid, int position) {
        // inflate menu
        id = String.valueOf(localid);
        PopupMenu popup = new PopupMenu(view.getContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_school_popup, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupClickListener());
        popup.show();
    }

    class PopupClickListener implements PopupMenu.OnMenuItemClickListener {

        public PopupClickListener() {
        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {

                case R.id.action_edit:
                    SchoolFragment.newInstance(id).show(fm, "School Fragment");
                    return true;

                case R.id.action_delete:
                    openDialog();
                    return true;


                default:
            }
            return false;
        }

    }

    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete")
                .setMessage("Are you sure you want to delete School?")
                .setPositiveButton("yes", (dialog, which) -> {
                    databaseHelper.deleteSchoolDB(id);
                    activity.refreshList();
                })
                .setNegativeButton("no", (dialog, which) -> {
                    dialog.dismiss();
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}