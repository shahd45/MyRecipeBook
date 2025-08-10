package com.example.recipebook;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

public class RecipeViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<Recipe>> recipes;
    private MutableLiveData<ArrayList<Recipe>> userRecipes;
    private MutableLiveData<Integer> selectedRecipe;
    private MutableLiveData<ArrayList<String>> ing;
    private Context context;
    private boolean home;
    MyDatabaseHelper myDB;
    //private ArrayList<Recipe> userRecipes= new ArrayList<>();

    public RecipeViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<ArrayList<Recipe>> getRecipes(Context context) {
        this.context = context;
        if (recipes == null)
            recipes = new MutableLiveData<>();
        loadRecipes();
        return recipes;
    }
//    public MutableLiveData<ArrayList<Recipe>> getUserRecipes(Context context) {
//        this.context = context;
//        if (userRecipes == null)
//            userRecipes = new MutableLiveData<>();
//        loadRecipes();
//        return userRecipes;
//    }

    public void setRecipes(ArrayList<Recipe> recipes) {
        this.recipes.setValue(recipes);
    }

    public MutableLiveData<Integer> getSelectedRecipe() {
        if (selectedRecipe == null) {
            selectedRecipe = new MutableLiveData<>();
            selectedRecipe.setValue(-1);
        }
        return selectedRecipe;
    }

    private void loadRecipes() {
        ArrayList<Recipe> allRecipes = new ArrayList<>();
        if (recipes != null) {
            if (home) {
                Log.i("load Home", "Get Home");
                allRecipes = RecipesXMLPareser.parseRecipes(context);
                //recipes.setValue(allRecipes);
            } else {
                /*getRecipes from database*/
                myDB = new MyDatabaseHelper(context);
                Cursor cursor = myDB.readAllData();
                //checking if there is no data
                if (cursor.getCount() == 0) {
                    //layout.setBackground(ContextCompat.getDrawable(context, R.drawable.ready));
                    // Toast.makeText(context, "No Recipes", Toast.LENGTH_SHORT).show();
                } else {
                    while (cursor.moveToNext()) {
                        Recipe recipe = new Recipe();
                        ArrayList<String> ing = new ArrayList<>();
                        recipe.setName(cursor.getString(0));//0 is the number of column
                        recipe.setTime(cursor.getString(1));
                        if(!cursor.getString((2)).isEmpty())
                            recipe.setServings(cursor.getString(2) + " servings");
                        else
                            recipe.setServings(cursor.getString(2));
                        String[] ingredients = cursor.getString(3).split("\n");
                        for (String str : ingredients)
                            ing.add(str);
                        recipe.setIngredients(ing);
                        recipe.setInstructions(cursor.getString(4));
//                        if(!allRecipes.contains(recipe))
                        allRecipes.add(recipe);
                    }
                    Log.i("load User", "Get User");

                }

            }

            recipes.setValue(allRecipes);
        }
    }

    public void setSelectedRecipe(Integer position) {
        selectedRecipe.setValue(position);
    }

    public Recipe getRecipe() {
        return recipes.getValue().get(selectedRecipe.getValue());
    }

    public void setHome(boolean home) {
        this.home = home;
    }

    public boolean getHome() {
        return home;
    }

    public void deleteRecipe(Recipe recipe) {
        MyDatabaseHelper myDB = new MyDatabaseHelper(context);
        myDB.deleteRecipe(recipe.getName());
    }

    public MutableLiveData<ArrayList<String>> getIng() {
        this.context = context;
        if (ing == null)
            ing = new MutableLiveData<>();
        return ing;
    }
}
