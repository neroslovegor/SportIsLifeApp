package com.example.sportislife.ui.weight;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.sportislife.R;
import com.example.sportislife.dao.DaoWeight;
import com.example.sportislife.databinding.FragmentWeightTrackingBinding;
import com.example.sportislife.db.AppDatabase;
import com.example.sportislife.repository.WeightRepository;
import com.github.mikephil.charting.charts.LineChart;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class WeightTrackingFragment extends Fragment {

    private WeightTrackingViewModel viewModel;
    private FragmentWeightTrackingBinding binding;

    private EditText editTextDate, editTextWeight;
    private FloatingActionButton btnSaveWeight;
    private LineChart weightLineChart;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Application application = this.requireActivity().getApplication();
        DaoWeight dao = AppDatabase.getInstance(application).daoWeight();
        WeightRepository repository = new WeightRepository(dao);
        WeightTrackingFactory factory = new WeightTrackingFactory(repository, application);

        viewModel = new ViewModelProvider(this, factory).get(WeightTrackingViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_weight_tracking, container, false);
        binding.setMyViewModel(viewModel);
        binding.setLifecycleOwner(this);

        editTextDate = binding.editTextDate;
        editTextWeight = binding.editTextWeight;
        weightLineChart = binding.weightLineChart;

        viewModel.getCurrentDate().observe(getViewLifecycleOwner(), editTextDate::setText);

        viewModel.getWeightData().observe(getViewLifecycleOwner(), weightLineChart::setData);

        viewModel.getErrorWeight().observe(getViewLifecycleOwner(), hasError -> {
            if (hasError == true) {
                editTextWeight.setError(getString(R.string.error_this_field_cannot_be_empty));
                viewModel.doneErrorWeight();
            }
        });

//        editTextDate = binding.editTextDate;
//        viewModel.getCurrentDate().observe(getViewLifecycleOwner(), editTextDate::setText);
//
//        editTextWeight = binding.editTextWeight;
//
//        btnSaveWeight = binding.btnSaveWeight;
//        btnSaveWeight.setOnClickListener(this);
//
//        weightLineChart = binding.weightLineChart;
//        viewModel.getWeightData().observe(getViewLifecycleOwner(), weightLineChart::setData);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}