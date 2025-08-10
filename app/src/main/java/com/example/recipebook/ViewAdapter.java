package com.example.recipebook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder> {

    protected Context context;
    private ViewAdapterListener listener;
    protected ArrayList<Recipe> allRecipes;
    protected boolean grid;
    protected SharedPreferences sp;
    private RecipeViewModel recipeViewModel;
    private int selectPos = -1;
    private boolean home;

    public ViewAdapter(Context context, RecipeViewModel recipeViewModel, ViewAdapterListener listener, boolean home) {
        this.context = context;
        this.listener = listener;
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        this.home = home;
        //add all the MVVM DONE***********
        this.recipeViewModel = recipeViewModel;
        this.recipeViewModel.setHome(home);
        //add the observers
        recipeViewModel.getRecipes(context).observe((LifecycleOwner) context, recipeObserver);
        recipeViewModel.getSelectedRecipe().observe((LifecycleOwner) context, observeSelectedIndex);

    }

    Observer<ArrayList<Recipe>> recipeObserver = new Observer<ArrayList<Recipe>>() {
        @Override
        public void onChanged(ArrayList<Recipe> recipes) {
            //after the get retuned here
            Log.i("ViewAdapter", "Get2");
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
    public ViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView;
        grid = sp.getBoolean("grid", true);
        if (grid)
            itemView = inflater.inflate(R.layout.recipe_grid_item, parent, false);
        else
            itemView = inflater.inflate(R.layout.recipe_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewAdapter.ViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return allRecipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

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
            if(!home)
                itemView.setOnLongClickListener(this);
            if (grid) {
                imageView = itemView.findViewById(R.id.recipe_grid_image);
                tvName = itemView.findViewById(R.id.recipe_grid_name);

            } else {
                imageView = itemView.findViewById(R.id.recipe_image);
                tvName = itemView.findViewById(R.id.recipe_name);
                tvTime = itemView.findViewById(R.id.recipe_time);
                tvServings = itemView.findViewById(R.id.recipe_servings);
            }
            /*listener of the adapter*/

        }

        public void bindData(final int position) {
            final Recipe recipe = allRecipes.get(position);
            tvName.setText(recipe.getName());
            if (!grid) {
                if ((!home && recipe.getTime() != null) || home)
                    tvTime.setText(recipe.getTime());
                if(recipe.getTime().isEmpty())
                    tvTime.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                if ((!home && recipe.getServings() != null) || home)
                    tvServings.setText(recipe.getServings());
                if(recipe.getServings().isEmpty())
                    tvServings.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

            }
            int imageResource;
            if (home)
                imageResource = context.getResources().getIdentifier(recipe.getImage(), "drawable", context.getPackageName());
            else
                imageResource = context.getResources().getIdentifier("cooking", "drawable", context.getPackageName());

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

        @Override
        public boolean onLongClick(View view) {
            /*the confirm dialog*/
            confirmDeletionDialog(getAdapterPosition());

            return false;
        }

        private void confirmDeletionDialog(int position){
            //if yes delete from database (recipeViewModel.deleteRecipe)
            new AlertDialog.Builder(context, R.style.MyDialogTheme)
                    .setTitle("Delete Recipe")
                    .setMessage("Are you sure you want to delete this recipe?")
                    .setIcon(R.drawable.delete64)
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Recipe recipe = allRecipes.get(position);
                            allRecipes.remove(recipe);
                            recipeViewModel.setRecipes(allRecipes);
                            recipeViewModel.deleteRecipe(recipe);
                            notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            //((MainActivity)getActivity()).doNegativeClick();
                        }
                    })
                    .create().show();
        }
    }

    public interface ViewAdapterListener {
        void onRecipeClick(int position);
    }
}
