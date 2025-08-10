package com.example.recipebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {
    private ArrayList<String> allIngredients;
    private Context context;

    public IngredientsAdapter(Context context, ArrayList<String> ingredients) {
        this.context = context;
        allIngredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.ingredient_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return allIngredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        View itemView;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            this.itemView=itemView;
            itemView.setClickable(true);
            checkBox=itemView.findViewById(R.id.ingredient);
        }

        public void bindData(final int position){
            final String ingredient = allIngredients.get(position);
            checkBox.setText(ingredient);
        }
    }
}
