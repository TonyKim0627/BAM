package com.example.jpeykar.bam;


import android.widget.ArrayAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.jpeykar.bam.Recipe;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SearchRecipeAdapter extends ArrayAdapter<Recipe> {

    int resource;


    public SearchRecipeAdapter(Context ctx, int res, List<Recipe> items)
    {
        super(ctx, res, items);
        resource = res;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout RecipeView;
        Recipe recipe = getItem(position);

        if (convertView == null) {
            RecipeView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(resource, RecipeView, true);
        } else {
            RecipeView = (LinearLayout) convertView;
        }

        ImageView image = (ImageView) RecipeView.findViewById(R.id.recipe_image);
        TextView name = (TextView) RecipeView.findViewById(R.id.recipe_name);
//


        name.setText(recipe.getName());
        //image.setimage(recipe.getImage());


        /*ImageView image = (ImageView) RecipeView.findViewById(R.id.)
        TextView hours = (TextView) RecipeView.findViewById(R.id.layout_);
        TextView date = (TextView) RecipeView.findViewById(R.id.layout_date);
        TextView light = (TextView) RecipeView.findViewById(R.id.layout_light);
        TextView type = (TextView) RecipeView.findViewById(R.id.layout_type);
        TextView weather = (TextView) RecipeView.findViewById(R.id.layout_weather);



        hours.setText(ls.getHours());
        date.setText(ls.getDate());
        light.setText(ls.getLight());
        type.setText(ls.getType());
        weather.setText(ls.getWeather());*/

        return RecipeView;
    }





}