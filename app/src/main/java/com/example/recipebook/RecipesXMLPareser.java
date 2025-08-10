package com.example.recipebook;

import android.content.Context;
import android.content.res.AssetManager;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class RecipesXMLPareser {
    final static String KEY_RECIPE="recipe";
    final static String KEY_NAME="name";
    final static String KEY_ANTHEM="anthem";
    final static String KEY_CATEGORY="category";
    final static String KEY_TIME="time";
    final static String KEY_SERVINGS="servings";
    final static String KEY_INGREDIENTS="ingredients";
    final static String KEY_INSTRUCTIONS="instructions";

    public static ArrayList<Recipe> parseRecipes(Context context){
        ArrayList<Recipe> data = null;
        InputStream in = openCountriesFile(context);
        XmlPullParserFactory xmlFactoryObject;
        try {
            xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser parser = xmlFactoryObject.newPullParser();

            parser.setInput(in, null);
            int eventType = parser.getEventType();
            Recipe currentRecipe = null;
            String inTag = "";
            String strTagText = null;

            while (eventType != XmlPullParser.END_DOCUMENT){
                inTag = parser.getName();
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        data = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        if (inTag.equalsIgnoreCase(KEY_RECIPE))
                            currentRecipe = new Recipe();
                        break;
                    case XmlPullParser.TEXT:
                        strTagText = parser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (inTag.equalsIgnoreCase(KEY_RECIPE))
                            data.add(currentRecipe);
                        else if (inTag.equalsIgnoreCase(KEY_NAME))
                            currentRecipe.setName(strTagText);
                        else if (inTag.equalsIgnoreCase(KEY_ANTHEM))
                            currentRecipe.setImage(strTagText);
                        else if (inTag.equalsIgnoreCase(KEY_CATEGORY))
                            currentRecipe.setCategory(strTagText);
                        else if (inTag.equalsIgnoreCase(KEY_TIME))
                            currentRecipe.setTime(strTagText);
                        else if (inTag.equalsIgnoreCase(KEY_SERVINGS))
                            currentRecipe.setServings(strTagText);
                        else if (inTag.equalsIgnoreCase(KEY_INGREDIENTS)) {
                            ArrayList<String> allIngredient = new ArrayList<>();
                            String[] ingredients = strTagText.split("\n");
                            for (String str : ingredients)
                                allIngredient.add(str);
                            currentRecipe.setIngredients(allIngredient);
                        }
                        else if (inTag.equalsIgnoreCase(KEY_INSTRUCTIONS)){
                            currentRecipe.setInstructions(strTagText);
                        }

                        inTag ="";
                        break;
                }//switch
                eventType = parser.next();
            }//while
        } catch (Exception e) {e.printStackTrace();}
        return data;
    }

    private static InputStream openCountriesFile(Context context){
        AssetManager assetManager = context.getAssets();
        InputStream in =null;
        try {
            in = assetManager.open("recipes.xml");
        } catch (IOException e) {e.printStackTrace();}
        return in;
    }
}
