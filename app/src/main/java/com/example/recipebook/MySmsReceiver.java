package com.example.recipebook;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class MySmsReceiver extends BroadcastReceiver {
    //private static final String TAG = MySmsReceiver.class.getSimpleName();
    public static final String SMS_BUNDLE = "pdus";
    private String[] recipe;
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();
        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            String smsMessageStr = "";
            for (int i = 0; i < sms.length; ++i) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);

                String smsBody = smsMessage.getMessageBody().toString();
                String address = smsMessage.getOriginatingAddress();

                //smsMessageStr += "SMS From: " + address + "\n";
                smsMessageStr += smsBody;

                // Toast.makeText(context, smsMessageStr, Toast.LENGTH_SHORT).show();


            }
            Recipe smsRecipe = new Recipe();
            String time = "", servings = "";
            recipe = smsMessageStr.split("\n");
            //Toast.makeText(context, recipe[1], Toast.LENGTH_SHORT).show();
            if (recipe[0].compareTo("Recipe:") == 0) {
                // the message is a recipe add it to the user recipes
                smsRecipe.setName(recipe[2]);
                if (recipe[3].contains("Time"))
                    time = recipe[4];
                smsRecipe.setTime(time);
                if (recipe[5].contains("Servings"))
                    servings = recipe[6];
                smsRecipe.setServings(servings);
                int ing = smsMessageStr.indexOf("Ingredients");
                Log.i("ingre spl", ""+ing);
                int ins = smsMessageStr.indexOf("Instructions");
                Log.i("ingre spl", ""+ins);
                ing += 12;
                String ingre = smsMessageStr.substring(ing, ins);
                Log.i("ingre spl", ""+ing);
                ArrayList<String> ingrdients = new ArrayList<>();
                String[] splitited = ingre.split("\n");
                for (String str : splitited)
                    ingrdients.add(str);
                smsRecipe.setIngredients(ingrdients);
                ins += 13;

                smsRecipe.setInstructions(smsMessageStr.substring(ins));

                MyDatabaseHelper myDB = new MyDatabaseHelper(context);
                //add the received recipe to database
                myDB.addRecipe(smsRecipe.getName(), smsRecipe.getTime(), smsRecipe.getServings(), ingre, smsRecipe.getInstructions());
                Toast.makeText(context, "You Have Recipe added to you Recipes", Toast.LENGTH_SHORT).show();
            }

            //  Toast.makeText(context, "You Have Recipe added to you Recipes", Toast.LENGTH_SHORT).show();

        }

    }
}
