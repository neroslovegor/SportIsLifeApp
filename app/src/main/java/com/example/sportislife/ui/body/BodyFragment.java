package com.example.sportislife.ui.body;

import android.app.AlertDialog;
import android.app.Application;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sportislife.R;
import com.example.sportislife.dao.DaoBody;
import com.example.sportislife.databinding.FragmentBodyBinding;
import com.example.sportislife.db.AppDatabase;
import com.example.sportislife.repository.BodyRepository;

import java.util.Calendar;

public class BodyFragment extends Fragment {

    private BodyViewModel viewModel;
    private FragmentBodyBinding binding;

    private EditText editTextName, editTextWeight, editTextHeight;
    private Button btnCalendar;
    private AutoCompleteTextView physicalActivity;

    private DatePickerDialog datePickerDialog;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Application application = this.requireActivity().getApplication();
        DaoBody dao = AppDatabase.getInstance(application).daoBody();
        BodyRepository repository = new BodyRepository(dao);
        BodyFactory factory = new BodyFactory(repository, application);

        viewModel = new ViewModelProvider(this, factory).get(BodyViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_body, container, false);
        binding.setBodyViewModel(viewModel);
        binding.setLifecycleOwner(this);

        editTextName = binding.editTextName;
        btnCalendar = binding.btnCalendar;
        editTextWeight = binding.editTextWeight;
        editTextHeight = binding.editTextHeight;
        physicalActivity = binding.physicalActivity;

        initDatePicker();
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker(view);
            }
        });

        ArrayAdapter<String> adapterPhysicalActivity = new ArrayAdapter<>(application.getApplicationContext(), R.layout.item_physical_activity, viewModel.getItemPhysicalActivity());
        physicalActivity.setAdapter(adapterPhysicalActivity);
        physicalActivity.setOnItemClickListener((parent, view, position, id) -> {
            Toast.makeText(application.getApplicationContext(), physicalActivity.getText().toString(), Toast.LENGTH_SHORT).show();
//            final int itemId = (int) weightTrackingAdapter.getItemId(position);
//            viewModel.deleteButton(itemId);
        });

        viewModel.getBodyData().observe(getViewLifecycleOwner(), bodyData -> {
            editTextName.setText(bodyData.getName());
            //btnCalendar.setText(bodyData.getDate());
            editTextWeight.setText(bodyData.getName());
            editTextHeight.setText(bodyData.getName());
            physicalActivity.setText(bodyData.getName());
        });

        viewModel.getError().observe(getViewLifecycleOwner(), hasError -> {
            if (hasError) {
                emptyField();
            }
        });

        return binding.getRoot();
    }

    private void emptyField() {
        if (editTextName.getText() != null) {
            editTextName.setError(getString(R.string.error_this_field_cannot_be_empty));
            viewModel.doneError();
        } else if (btnCalendar.getText() != null) {
            btnCalendar.setError(getString(R.string.error_this_field_cannot_be_empty));
            viewModel.doneError();
        } else if (editTextWeight.getText() != null) {
            editTextWeight.setError(getString(R.string.error_this_field_cannot_be_empty));
            viewModel.doneError();
        } else if (editTextHeight.getText() != null) {
            editTextHeight.setError(getString(R.string.error_this_field_cannot_be_empty));
            viewModel.doneError();
        } else if (physicalActivity.getText() != null) {
            physicalActivity.setError(getString(R.string.error_this_field_cannot_be_empty));
            viewModel.doneError();
        }
    }

    private String getBirthDate() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        month = month + 1;
        int year = calendar.get(Calendar.YEAR);

        return day + " " + month + " " + year;
    }
    public void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = day + "-" + month + "-" + year;
                btnCalendar.setText(date);
            }
        };

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(getContext(), style, dateSetListener, year, month, day);
    }
    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}