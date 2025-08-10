package com.example.recipebook;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NewIngredientsAdapter extends RecyclerView.Adapter<NewIngredientsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> newIngredients;
    /*The view model for the rotation*/

    public NewIngredientsAdapter(Context context) {
        this.context = context;
        newIngredients = new ArrayList<>();
        newIngredients.add(new String());
    }

    @NonNull
    @Override
    public NewIngredientsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.add_ingredient, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewIngredientsAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return newIngredients.size();
    }

    public ArrayList<String> getNewIngredients() {
        return newIngredients;
    }

    public void addIngredient() {
        newIngredients.add(new String());
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView ing;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            ing = itemView.findViewById(R.id.add_new_ingredient);

            ing.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    CharSequence text = "Must enter Ingredient";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    if (!ing.getText().toString().isEmpty()) {
                        newIngredients.remove(getAdapterPosition());
                        newIngredients.add(getAdapterPosition(), ing.getText().toString());
                        Log.i("The adapter Position", ""+getAdapterPosition());
                    }
                    else
                        toast.show();
                }
            });
        }
    }
}
