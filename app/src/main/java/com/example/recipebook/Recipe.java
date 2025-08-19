package com.example.recipebook;

import java.util.ArrayList;

public class Recipe {
    private int id;   // رقم الوصفة (DummyJSON)
    private String image;
    private String name;
    private String category;
    private String time;
    private String servings;
    private ArrayList<String> ingredients;
    private String instructions;

    // علشان نميز إذا الوصفة محلية أو من API
    private boolean localOnly = false;

    // ==== Getters & Setters ====

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public String getServings() {
        return servings;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getInstructions() {
        return instructions;
    }

    public boolean isLocalOnly() {
        return localOnly;
    }

    public void setLocalOnly(boolean localOnly) {
        this.localOnly = localOnly;
    }
}
