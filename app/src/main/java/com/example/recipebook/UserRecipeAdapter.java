package com.example.recipebook;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.example.recipebook.Recipe;

import java.util.ArrayList;

public class UserRecipeAdapter extends ViewAdapter {

    public UserRecipeAdapter(Context context, RecipeViewModel recipeViewModel, ViewAdapterListener listener) {
        super(context, recipeViewModel,   listener, false);
    }

}
