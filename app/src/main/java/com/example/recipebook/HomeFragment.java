package com.example.recipebook;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends FragmentWithSittings implements RecipeAdapter.RecipeAdapterListener {

    public interface RecipeSelectListener {
        void onRecipeSelected(int position);
    }

    private RecipeSelectListener listener;

    private RecyclerView rvRecipes;
    private RecipeAdapter recipeAdapter;
    private RecipeViewModel recipeViewModel;

    private RecipeLocalStore recipeLocalStore;
    private com.android.volley.RequestQueue requestQueue;

    private SharedPreferences sp;
    private boolean grid = true;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof RecipeSelectListener) {
            listener = (RecipeSelectListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement HomeFragment.RecipeSelectListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvRecipes = view.findViewById(R.id.rvRecipes);

        recipeViewModel = new ViewModelProvider(requireActivity()).get(RecipeViewModel.class);

        sp = androidx.preference.PreferenceManager.getDefaultSharedPreferences(requireContext());
        grid = sp.getBoolean("grid", true);

        // إعداد الـ RecyclerView
        if (grid) {
            rvRecipes.setLayoutManager(new androidx.recyclerview.widget.GridLayoutManager(requireContext(), 2));
        } else {
            rvRecipes.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(requireContext()));
        }

        recipeAdapter = new RecipeAdapter(requireContext(), recipeViewModel, this);
        rvRecipes.setAdapter(recipeAdapter);

        // التخزين المحلي + Volley
        recipeLocalStore = new RecipeLocalStore(requireContext());
        requestQueue = VolleySingleton.getInstance(requireContext()).getQueue();

        // جلب البيانات من الـ API
        fetchRecipesFromAPI();
    }

    // يستدعيه الـ Adapter عند الضغط على عنصر
    @Override
    public void onRecipeClick(int position) {
        if (listener != null) {
            listener.onRecipeSelected(position);
        }
    }

    private void fetchRecipesFromAPI() {
        String url = "https://dummyjson.com/recipes";

        com.android.volley.toolbox.JsonObjectRequest req = new com.android.volley.toolbox.JsonObjectRequest(
                com.android.volley.Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        Log.d("API_RESPONSE", response.toString());

                        org.json.JSONArray arr = response.optJSONArray("recipes");
                        java.util.List<Recipe> apiRecipes = new java.util.ArrayList<>();
                        com.google.gson.Gson gson = new com.google.gson.Gson();

                        if (arr != null) {
                            java.lang.reflect.Type listType =
                                    new com.google.gson.reflect.TypeToken<java.util.List<Recipe>>() {}.getType();
                            apiRecipes = gson.fromJson(arr.toString(), listType);
                        } else {
                            // احتياط إذا تغيّر شكل الـ JSON
                            RecipesResponse rr = gson.fromJson(response.toString(), RecipesResponse.class);
                            if (rr != null && rr.getRecipes() != null) {
                                apiRecipes = rr.getRecipes();
                            }
                        }

                        // دمج مع الوصفات المحلية (localOnly)
                        java.util.List<Recipe> merged = new java.util.ArrayList<>();
                        java.util.List<Recipe> local = recipeLocalStore.load();
                        for (Recipe r : local) {
                            if (r.isLocalOnly()) merged.add(r);
                        }
                        merged.addAll(apiRecipes);

                        // تحديث الـ Adapter و الـ ViewModel
                        recipeAdapter.setData(new java.util.ArrayList<>(merged));
                        recipeViewModel.setRecipes(new java.util.ArrayList<>(merged));

                        // حفظ نسخة محلياً
                        recipeLocalStore.save(merged);

                    } catch (Exception e) {
                        Log.e("API_PARSE_ERROR", "Parsing failed: " + e.getMessage(), e);
                    }
                },
                error -> Log.e("API_ERROR",
                        "Volley error: " + (error.getMessage() == null ? "no message" : error.getMessage()))
        );

        requestQueue.add(req);
    }
}
