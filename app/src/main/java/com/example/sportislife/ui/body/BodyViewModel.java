package com.example.sportislife.ui.body;

import android.app.Application;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.widget.DatePicker;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sportislife.repository.BodyRepository;
import com.example.sportislife.repository.model.Body;
import com.example.sportislife.repository.model.Weight;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class BodyViewModel extends ViewModel {

    private BodyRepository repository;
    private Application application;


    private MutableLiveData<Body> bodyData;
    public MutableLiveData<Body> getBodyData() {
        return bodyData;
    }
    private void setBodyData() {
        bodyData.setValue(initBodyData());
    }

    private final HashMap<String, Double> itemPhysicalActivity;
    private HashMap<String, Double> initItemPhysicalActivity() {
        return new HashMap<String, Double>() {{
            put("Физическая нагрузка отсутствует или минимальна", 1.2);
            put("Тренировки средней тяжести 3 раза в неделю", 1.38);
            put("Тренировки средней тяжести 5 раза в неделю", 1.46);
            put("Интенсивные тренировки 5 раза в неделю", 1.55);
            put("Тренировки каждый день", 1.64);
            put("Интенсивные тренировки каждый день или по 2 раза в день", 1.73);
            put("Ежедневная нагрузка и физическая работа", 1.9);
        }};
    }
    public String[] getItemPhysicalActivity() {
        return itemPhysicalActivity.keySet().toArray(new String[0]);
    }

    public MutableLiveData<String> inputName;
    public MutableLiveData<String> inputBirthDate;
    public MutableLiveData<String> inputWeight;
    public MutableLiveData<String> inputHeight;
    public MutableLiveData<String> inputGender;
    public MutableLiveData<String> inputPhysicalActivity;

    private MutableLiveData<Boolean> error;
    public MutableLiveData<Boolean> getError() {
        return error;
    }
    public void doneError() {
        error.setValue(false);
    }

    public BodyViewModel(BodyRepository repository, Application application) {
        this.repository = repository;
        this.application = application;

        bodyData = new MutableLiveData<>();

        itemPhysicalActivity = initItemPhysicalActivity();

        inputName = new MutableLiveData<>();
        inputBirthDate = new MutableLiveData<>();
        inputWeight = new MutableLiveData<>();
        inputHeight = new MutableLiveData<>();
        inputGender = new MutableLiveData<>();
        inputPhysicalActivity = new MutableLiveData<>();

        error = new MutableLiveData<>();

        updateData();
    }

    private void updateData() {
        setBodyData();
    }

    private Body initBodyData() {
        List<Body> bodies = repository.getAll();
        return bodies.get(0);
    }

    public void submitButton() {
        try {
            if (inputName.getValue() != null && inputBirthDate.getValue() != null &&
                    inputWeight.getValue() != null && inputHeight.getValue() != null &&
                    inputGender.getValue() != null && inputPhysicalActivity.getValue() != null) {

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

                String name = inputName.getValue();
                Date birthDate = simpleDateFormat.parse(Objects.requireNonNull(inputBirthDate.getValue()));
                Float weight = Float.parseFloat(Objects.requireNonNull(inputWeight.getValue()));
                Float height = Float.parseFloat(Objects.requireNonNull(inputHeight.getValue()));
                String gender = inputGender.getValue();
                String physicalActivity = inputPhysicalActivity.getValue();

                Body body = new Body();
                body.setName(name);
                body.setDate(birthDate);
                body.setWeight(weight);
                body.setHeight(height);
                body.setGender(gender);
                body.setPhysicalActivity(physicalActivity);

                repository.insert(body);

                updateData();
            } else {
                error.setValue(true);
            }
        } catch (ParseException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}