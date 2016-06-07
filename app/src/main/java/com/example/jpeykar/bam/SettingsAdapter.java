package com.example.jpeykar.bam;


import android.widget.ArrayAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.jpeykar.bam.Recipe;

import org.w3c.dom.Text;

import java.util.List;

public class SettingsAdapter extends ArrayAdapter<String>{

    int resource;

    public SettingsAdapter(Context ctx, int res, List<String> items)
    {
        super(ctx, res, items);
        resource = res;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout RecipeView;

        if (convertView == null) {
            RecipeView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(resource, RecipeView, true);
        } else {
            RecipeView = (LinearLayout) convertView;
        }

        ImageView image = (ImageView) RecipeView.findViewById(R.id.settingsImageView);
        TextView name = (TextView) RecipeView.findViewById(R.id.settingsTextView);
        name.setText(getItem(position));
        if (position == 0) {
            image.setImageResource(R.drawable.facebook_icon);
        } else if (position == 1) {
            image.setImageResource(R.drawable.paper_plane);
        } else {
            image.setImageResource(R.drawable.eraser);
        }
        return RecipeView;
    }

}