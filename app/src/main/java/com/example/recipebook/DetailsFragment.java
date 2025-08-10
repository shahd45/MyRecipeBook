package com.example.recipebook;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

public class DetailsFragment extends Fragment {

    private Recipe recipe;
    private ImageView imageView;
    private TextView name;
    private TextView time;
    private TextView servings;
    private RecyclerView rvIngredients;
    private TextView instructions;
    private IngredientsAdapter ingredientsAdapter;
    /*add the MVVM*/
    private RecipeViewModel recipeViewModel;
    /*Add Observers*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        recipeViewModel = new ViewModelProvider(requireActivity()).get(RecipeViewModel.class);
        if(!recipeViewModel.getHome())
            setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        recipe = recipeViewModel.getRecipe();
        name = view.findViewById(R.id.recipe_name);
        time = view.findViewById(R.id.recipe_time);
        servings = view.findViewById(R.id.recipe_servings);
        rvIngredients = view.findViewById(R.id.rvIngredients);
        instructions = view.findViewById(R.id.instructions);
        imageView = view.findViewById(R.id.recipe_image);
        int imageResource;
        if (recipe.getImage() != null)
            imageResource = getActivity().getResources().getIdentifier(recipe.getImage(), "drawable", getActivity().getPackageName());
        else {
            imageResource = getActivity().getResources().getIdentifier("cooking", "drawable", getActivity().getPackageName());

        }
        imageView.setImageResource(imageResource);
        name.setText(recipe.getName());
        if (recipe.getTime() != null)
            time.setText(recipe.getTime());
        if (recipe.getTime().isEmpty())
            time.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        Log.i("DetailsFragment", recipe.getServings());
        if (recipe.getServings() != null)
            servings.setText(recipe.getServings());
        if (recipe.getServings().isEmpty())
            servings.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        instructions.setText(recipe.getInstructions());
        /*add the MVVM*/
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ingredientsAdapter = new IngredientsAdapter(requireActivity(), recipe.getIngredients());
        rvIngredients.setAdapter(ingredientsAdapter);
        rvIngredients.setLayoutManager(new LinearLayoutManager(requireActivity()));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.delete_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.delete:
                showDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDialog(){
        DialogFragment newFragment = DeleteDialog.newInstance();
        newFragment.show(getFragmentManager(), "dialog");
    }
}