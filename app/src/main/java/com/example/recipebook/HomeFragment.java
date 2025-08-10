package com.example.recipebook;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeFragment extends FragmentWithSittings implements RecipeAdapter.RecipeAdapterListener {
    private RecyclerView rvRecipes;
    private RecipeAdapter recipeAdapter;
    private RecipeViewModel recipeViewModel;
    private RecipeSelectListener listener;

    public HomeFragment(){
        // require a empty public constructor
    }


    @Override
    public void onAttach(@NonNull Context context) {
        if(context instanceof RecipeSelectListener)
            listener = (RecipeSelectListener) context;
        else
            throw new ClassCastException(context.toString()+
                    " must implements the interface 'RecipeSelectListener'");
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rvRecipes = (RecyclerView) view.findViewById(R.id.rvRecipes);
        //add the MVVM
        recipeViewModel = new ViewModelProvider(requireActivity()).get(RecipeViewModel.class);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        SharedPreferences sp;
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null){
            listener = (RecipeSelectListener) getActivity();
        }
        recipeAdapter = new RecipeAdapter(requireActivity(), recipeViewModel, this);
        rvRecipes.setAdapter(recipeAdapter);
        sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        if(sp.getBoolean("grid", true))
            rvRecipes.setLayoutManager(new GridLayoutManager(requireActivity(),2));
        else
            rvRecipes.setLayoutManager(new LinearLayoutManager(requireActivity()));
        //rvRecipes.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
    }

    @Override
    public void onRecipeClick(int position) {
        listener.onRecipeSelected(position);
    }

    public interface RecipeSelectListener{
        void onRecipeSelected(int position);
    }
}