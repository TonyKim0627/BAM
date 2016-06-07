package com.example.jpeykar.bam;


import android.util.Log;
import android.widget.ArrayAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
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

public class IngredientAdapter extends ArrayAdapter<String> {

    int resource;


    public IngredientAdapter(Context ctx, int res, List<String> items)
    {
        super(ctx, res, items);
        resource = res;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout RecipeView;
        String ingredients = getItem(position);

        if (convertView == null) {
            RecipeView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(resource, RecipeView, true);
        } else {
            RecipeView = (LinearLayout) convertView;
        }

        TextView name = (TextView) RecipeView.findViewById(R.id.ingredient_name);
        Button button = (Button) RecipeView.findViewById(R.id.x_button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("here");
                final int postion = search.lv.getPositionForView(v);
                search.ingredients.remove(postion);
                ((BaseAdapter) search.lv.getAdapter()).notifyDataSetChanged();
                System.out.println(search.ingredients);
            }
        });

        name.setText(ingredients.replace('+', ' '));


        return RecipeView;
    }





}