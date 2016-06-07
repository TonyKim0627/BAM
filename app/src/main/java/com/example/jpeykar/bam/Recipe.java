package com.example.jpeykar.bam;

import android.media.Image;

/**
 * Created by ggoma on 16. 4. 19..
 */
public class Recipe {
    private String name;
    private int id;
    private String image;
    private String ingredients;
    protected boolean Clicked;

    public Recipe(String name, String ingredients, String image) {
        this.name = name;
        this.ingredients = ingredients;
        this.image = image;
        this.Clicked = false;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {

        return name;
    }

    public String getIngredients() {
        return this.ingredients;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdNumber() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString(){
        return this.name;
    }
}
