package com.example.recipebook;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Recipe> allRecipes;
    private int selectPos = -1;
    private RecipeViewModel recipeViewModel;
    private RecipeAdapterListener listener;
    private SharedPreferences sp;
    private boolean grid;

    public RecipeAdapter(Context context, RecipeViewModel recipeViewModel, RecipeAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        //add all the MVVM DONE***********
        this.recipeViewModel = recipeViewModel;
        this.recipeViewModel.setHome(true);
        //add the observers
        recipeViewModel.getRecipes(context).observe((LifecycleOwner) context, recipeObserver);
        recipeViewModel.getSelectedRecipe().observe((LifecycleOwner) context, observeSelectedIndex);

    }

    Observer<ArrayList<Recipe>> recipeObserver = new Observer<ArrayList<Recipe>>() {
        @Override
        public void onChanged(ArrayList<Recipe> recipes) {
            //after the get retuned here
            Log.i("RecipeAdapter", "Get1");
            allRecipes = recipes;
        }
    };

    // OBSERVE
    // Here we will observe and update the selected row
    Observer<Integer> observeSelectedIndex = new Observer<Integer>() {
        @Override
        public void onChanged(Integer index) {
            selectPos = index;
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView;
        /*Check if Grid checked*/
        grid = sp.getBoolean("grid", true);
        if (grid)
            itemView = inflater.inflate(R.layout.recipe_grid_item, parent, false);
        else
            itemView = inflater.inflate(R.layout.recipe_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return allRecipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View itemView;
        ImageView imageView;
        TextView tvName;
        TextView tvTime;
        TextView tvServings;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            itemView.setClickable(true);
            /*setOnclickListener for the itemView*/
            itemView.setOnClickListener(this);
            if (grid) {
                imageView = itemView.findViewById(R.id.recipe_grid_image);
                tvName = itemView.findViewById(R.id.recipe_grid_name);

            } else {
                imageView = itemView.findViewById(R.id.recipe_image);
                tvName = itemView.findViewById(R.id.recipe_name);
                tvTime = itemView.findViewById(R.id.recipe_time);
                tvServings = itemView.findViewById(R.id.recipe_servings);
            }
            tvName.setClickable(true);
            /*listener of the adapter*/
        }

        public void bindData(final int position) {
            final Recipe recipe = allRecipes.get(position);
            tvName.setText(recipe.getName());
            if (!grid) {
                tvTime.setText(recipe.getTime());
                tvServings.setText(recipe.getServings());
            }
            int imageResource = context.getResources().getIdentifier(recipe.getImage(), "drawable", context.getPackageName());
            imageView.setImageResource(imageResource);
            /*set item selected Highlighting*/
        }

        @Override
        public void onClick(View view) {
            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;
            // Updating old as well as new positions
            notifyItemChanged(selectPos);
            selectPos = getAdapterPosition();
            recipeViewModel.setSelectedRecipe(selectPos);
            notifyDataSetChanged();
            listener.onRecipeClick(selectPos);
        }
    }

    public interface RecipeAdapterListener {
        void onRecipeClick(int position);
    }
}
