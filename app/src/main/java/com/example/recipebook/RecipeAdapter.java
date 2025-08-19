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

        this.recipeViewModel = recipeViewModel;
        this.recipeViewModel.setHome(true);

        // ملاحظة: نراقب التغييرات من الـ ViewModel
        recipeViewModel.getRecipes(context).observe((LifecycleOwner) context, recipeObserver);
        recipeViewModel.getSelectedRecipe().observe((LifecycleOwner) context, observeSelectedIndex);
    }

    // ملاحظة: لما تتغير الوصفات بالـ ViewModel
    Observer<ArrayList<Recipe>> recipeObserver = new Observer<ArrayList<Recipe>>() {
        @Override
        public void onChanged(ArrayList<Recipe> recipes) {
            Log.i("RecipeAdapter", "Recipes updated from ViewModel");
            allRecipes = recipes;
            notifyDataSetChanged();
        }
    };

    // ملاحظة: لما يتغير الإندكس المختار
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

        // إذا Grid أو List
        grid = sp.getBoolean("grid", true);
        if (grid) {
            itemView = inflater.inflate(R.layout.recipe_grid_item, parent, false);
        } else {
            itemView = inflater.inflate(R.layout.recipe_list_item, parent, false);
        }

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return (allRecipes == null) ? 0 : allRecipes.size();
    }

    // 🔹 دالة جديدة لتحديث البيانات من API أو أي مصدر خارجي
    public void setData(ArrayList<Recipe> newRecipes) {
        this.allRecipes = newRecipes;
        notifyDataSetChanged();
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
        }

        public void bindData(final int position) {
            final Recipe recipe = allRecipes.get(position);
            tvName.setText(recipe.getName());

            if (!grid) {
                tvTime.setText(recipe.getTime());
                tvServings.setText(recipe.getServings());
            }

            // تحميل الصورة من drawable
            int imageResource = context.getResources().getIdentifier(
                    recipe.getImage(),
                    "drawable",
                    context.getPackageName()
            );
            imageView.setImageResource(imageResource);
        }

        @Override
        public void onClick(View view) {
            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;

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
