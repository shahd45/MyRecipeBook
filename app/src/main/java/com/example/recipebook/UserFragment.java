package com.example.recipebook;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class UserFragment extends FragmentWithSittings implements ViewAdapter.ViewAdapterListener {

    private RecyclerView rvUserRecipes;
    private UserRecipeAdapter userRecipeAdapter;
    private UserRecipeSelected listener;
    private RecipeViewModel recipeViewModel;
    private View v;

    public UserFragment(){
        // require a empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof HomeFragment.RecipeSelectListener)
            listener = (UserRecipeSelected) context;
        else
            throw new ClassCastException(context.toString() +
                    " must implements the interface 'RecipeSelectListener'");
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_user, container, false);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rvUserRecipes = (RecyclerView) view.findViewById(R.id.rvUserRecipes);

        //MVVM
        recipeViewModel = new ViewModelProvider(requireActivity()).get(RecipeViewModel.class);
        recipeViewModel.getRecipes(getContext()).observe(getActivity(), userRecipesObserver);
        super.onViewCreated(view, savedInstanceState);
    }

    Observer<ArrayList<Recipe>> userRecipesObserver = new Observer<ArrayList<Recipe>>() {
        @Override
        public void onChanged(ArrayList<Recipe> recipes) {
            /*check if it's empty and put a background*/
            Log.i("onChange UserFragment", recipes.size()+"");
            if (recipes.size() == 0) {
                ((LottieAnimationView) v.findViewById(R.id.backgr)).setVisibility(View.VISIBLE);
                ((LottieAnimationView) v.findViewById(R.id.arrow)).setVisibility(View.VISIBLE);
                //v.setBackgroundResource(R.drawable.emptybackground);
                //Toast.makeText(getActivity(), "No Recipes", Toast.LENGTH_SHORT).show();
            }else {
               // v.setBackgroundResource(0);
                ((LottieAnimationView) v.findViewById(R.id.arrow)).setVisibility(View.GONE);
                ((LottieAnimationView) v.findViewById(R.id.backgr)).setVisibility(View.GONE);
            }
        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        SharedPreferences sp;
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null)
            listener = (UserRecipeSelected) getActivity();
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);

        userRecipeAdapter = new UserRecipeAdapter(requireActivity(), recipeViewModel, this);
//        if (recipeViewModel.getRecipes(getContext()).getValue().size() == 0) {
//            v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.empty1));
//            Toast.makeText(getContext(), "No Recipes", Toast.LENGTH_SHORT).show();
//        }
        rvUserRecipes.setAdapter(userRecipeAdapter);
        sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        if (sp.getBoolean("grid", true))
            rvUserRecipes.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
        else
            rvUserRecipes.setLayoutManager(new LinearLayoutManager(requireActivity()));

        rvUserRecipes.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fab.getVisibility() == View.VISIBLE) {
                    fab.hide();
                } else if (dy < 0 && fab.getVisibility() != View.VISIBLE) {
                    fab.show();
                }
            }
        });
    }



    @Override
    public void onRecipeClick(int position) {
        listener.onRecipeSelected(position);
    }


    public interface UserRecipeSelected {
        void onRecipeSelected(int position);
    }
}