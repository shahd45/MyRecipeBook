package com.example.recipebook;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RecipeLocalStore {
    private static final String PREFS = "recipes_store";
    private static final String KEY = "recipes_json";
    private SharedPreferences sp;
    private Gson gson = new Gson();
    private Type listType = new TypeToken<List<Recipe>>(){}.getType();

    public RecipeLocalStore(Context ctx) {
        sp = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public void save(List<Recipe> list) {
        sp.edit().putString(KEY, gson.toJson(list)).apply();
    }

    public List<Recipe> load() {
        String json = sp.getString(KEY, "[]");
        List<Recipe> list = gson.fromJson(json, listType);
        return list != null ? list : new ArrayList<>();
    }
}
