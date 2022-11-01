package com.example.sportislife.ui.weight;

import android.app.Application;
import android.graphics.Color;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sportislife.repository.WeightRepository;
import com.example.sportislife.repository.model.Weight;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class WeightTrackingViewModel extends ViewModel {

    private WeightRepository repository;

    private MutableLiveData<String> currentDate;
    public MutableLiveData<String> getCurrentDate() {
        return currentDate;
    }
    public void setCurrentDate() {
        currentDate.setValue(initCurrentDate());
    }

    private MutableLiveData<LineData> weightData;
    public MutableLiveData<LineData> getWeightData() {
        return weightData;
    }
    public void setWeightData() {
        weightData.setValue(initWeightData());
    }

    public MutableLiveData<String> inputDate;
    //public void setInputDate(String date) { inputDate.setValue(date); }
    public MutableLiveData<String> inputWeight;
    //public void setInputWeight(String weight) { inputDate.setValue(weight); }

    private MutableLiveData<Boolean> errorWeight;
    public MutableLiveData<Boolean> getErrorWeight() {
        return errorWeight;
    }
    public void doneErrorWeight() {
        errorWeight.setValue(false);
    }

    public WeightTrackingViewModel(WeightRepository repository, Application application) {
        this.repository = repository;

        currentDate = new MutableLiveData<>();
        weightData = new MutableLiveData<>();

        inputDate = new MutableLiveData<>();
        inputWeight = new MutableLiveData<>();

        errorWeight = new MutableLiveData<>();

        updateData();
    }

    private void updateData() {
        setCurrentDate();
        setWeightData();
    }
    private String initCurrentDate() {
        return new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());
    }
    private LineData initWeightData() {
        List<Weight> weights = repository.getAll();
        List<Entry> weightsData = new ArrayList<>();

        if (weights == null) return null;

        int counter = 1;
        for (Weight weight : weights) {
            weightsData.add(new Entry(counter++, weight.getWeight()));
        }

        LineDataSet lineDataSet = new LineDataSet(weightsData, "Вес");
        lineDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        lineDataSet.setValueTextColor(Color.BLACK);
        lineDataSet.setValueTextSize(16f);

        return new LineData(lineDataSet);
    }

    public void submitButton() {
        try {
            if (!Objects.equals(inputDate.getValue(), "") && !Objects.equals(inputWeight.getValue(), "")) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
                Date date = simpleDateFormat.parse(Objects.requireNonNull(inputDate.getValue()));
                Float weight = Float.parseFloat(Objects.requireNonNull(inputWeight.getValue()));

                Weight weightEntity = new Weight();
                weightEntity.setDate(date);
                weightEntity.setWeight(weight);

                insert(weightEntity);

                inputWeight.setValue(null);

                updateData();
            } else {
                errorWeight.setValue(true);
            }
        } catch (ParseException exception) {
            exception.printStackTrace();
        }
    }
    public void insert(Weight weight) {
        repository.insert(weight);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}