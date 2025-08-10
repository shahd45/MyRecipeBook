package com.example.recipebook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class AddRecipeActivity extends All {
    Context context;
    /*check if empty "Name" "Ingredients" "Instructions" */
    private EditText name;
    private EditText time;
    private EditText servings;/*check the entered number*/
    private RecyclerView rvIng;
    private Button addIngredient;
    private EditText ingredients;
    private EditText instructions;
    private Button addRecipe;
    private NewIngredientsAdapter newIngredientsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_recipe);
        name = findViewById(R.id.new_name);
        time = findViewById(R.id.new_time);
        servings = findViewById(R.id.new_servings);

       // addIngredient = findViewById(R.id.add_ingredient);
        ingredients = findViewById(R.id.new_ingredients);
        instructions = findViewById(R.id.new_instructions);
        addRecipe = findViewById(R.id.addRecipe);

        addRecipe.setOnClickListener(addHandler);
        //rvIng = findViewById(R.id.rvIng);
        context = this;
//        newIngredientsAdapter = new NewIngredientsAdapter(context);
//
//        addIngredient.setOnClickListener(new View.OnClickListener() {
//            /*On Click for the add ingredient*/
//            @Override
//            public void onClick(View view) {
//                newIngredientsAdapter.addIngredient();
//            }
//        });
//        rvIng.setAdapter(newIngredientsAdapter);
//        rvIng.setLayoutManager(new LinearLayoutManager(this));

    }

    private View.OnClickListener addHandler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean nflag, tflag, sflag = true, ingflag = false, insflag;
            EditText editText;
            String[] sTime;
            String rName = name.getText().toString();
            String rTime = time.getText().toString();
            String rServings = servings.getText().toString();
            //ArrayList<String> allIngredients = newIngredientsAdapter.getNewIngredients();
            String rIngredients = ingredients.getText().toString();
            String rInstructions = instructions.getText().toString();
            int hrs, mins;

            Intent intent = new Intent(context, MainActivity.class);
            /*check all the input*/

            /* time */
            if (!rTime.isEmpty()) {
                if (!rTime.contains(":")) {
                    tflag = false;
                    time.setError("Time should be 20:30(20 hrs, 30 mins)");
                } else {
                    sTime = rTime.split(":");
                    hrs = Integer.parseInt(sTime[0]);
                    mins = Integer.parseInt(sTime[1]);
                    if (!isNumeric(sTime[0]) && !isNumeric(sTime[1]) && (hrs < 0 || hrs > 23 || mins < 0 || mins > 59)) {
                        time.setError("Invalid hours or minutes hours(0-23) minutes(0-59)");
                        tflag = false;
                    } else {
                        rTime = sTime[0] + " hrs " + sTime[1] + " mins";
                        tflag = true;
                    }
                }
            } else tflag = true;

            /* servings */
            if (!rServings.isEmpty() && !isNumeric(rServings)) {
                sflag = false;
                servings.setError("Must be number");
            } else if (rServings.isEmpty())
                sflag = true;
            else if (!rServings.isEmpty() && isNumeric(rServings))
                sflag = true;

            /* Name */
            if (rName.isEmpty()) {
                name.setError("Please Enter the recipe name");
                nflag = false;
            } else nflag = true;

            /* Instructions */
            if (rInstructions.isEmpty()) {
                instructions.setError("Please Enter the recipe instructions");
                insflag = false;
            } else
                insflag = true;

            /* Ingredients */
            if(rIngredients.isEmpty()) {
                ingflag = false;
                ingredients.setError("Please enter the empty ingredient or remove it");
            }else{
                ingflag=true;
            }


//            for (String str : allIngredients) {
//                editText = findViewById(R.id.add_new_ingredient);
//                if (str.isEmpty()) {
//                    editText.setError("Please enter the empty ingredient or remove it");
//                    ingflag = false;
//                } else {
//                    rIngredients += str + "\n";
//                    ingflag = true;
//                }
//            }

            /*the time and the servings isn't nassacerly*/
            if (nflag && tflag && sflag && ingflag && insflag) {

                MyDatabaseHelper myDB = new MyDatabaseHelper(AddRecipeActivity.this);
                //Toast.makeText(AddRecipeActivity.this, "Succeed", Toast.LENGTH_LONG).show();

                /*Insert data to the database*/
                //return to the previous activity with the result (true/ false) activity if(succeeded)
                myDB.addRecipe(rName, rTime, rServings, rIngredients, rInstructions);
                setResult(RESULT_OK, intent);
                onBackPressed();
                finish();
            }
        }
    };

    private static boolean isNumeric(String num){
        int intValue;
        try{
           intValue = Integer.parseInt(num);
           return true;
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        return false;
    }


}