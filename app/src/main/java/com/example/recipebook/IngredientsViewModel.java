package com.example.recipebook;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

public class IngredientsViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<Recipe>> recipes;


    public IngredientsViewModel(@NonNull Application application) {
        super(application);
    }
}
