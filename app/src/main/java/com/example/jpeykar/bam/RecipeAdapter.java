package com.example.jpeykar.bam;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.widget.ArrayAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.jpeykar.bam.Recipe;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.List;

public class RecipeAdapter extends ArrayAdapter<Recipe> {

    int resource;
    public boolean[] checked;
    View PreviousView;
    int PreviousPosition;

    public RecipeAdapter(Context ctx, int res, List<Recipe> items)
    {
        super(ctx, res, items);
        resource = res;
        checked = new boolean[20];
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        final LinearLayout RecipeView;
        final Recipe recipe = getItem(position);


        if (convertView == null) {
            RecipeView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(resource, RecipeView, true);
        } else {
            RecipeView = (LinearLayout) convertView;
        }
        new DownloadImageTask((ImageView) RecipeView.findViewById(R.id.layout_image))
                .execute(recipe.getImage());


        //ImageView image = (ImageView) RecipeView.findViewById(R.id.layout_image);

        TextView name = (TextView) RecipeView.findViewById(R.id.layout_name);
        final Button favorite = (Button) RecipeView.findViewById(R.id.layout_favorite);


        //final int pos = MainMenu.listView.getPositionForView(favorite.getRootView());
        //System.out.println(pos);


        /*favorite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//                System.out.println("here");
//                final int pos = search.lv.getPositionForView(v);
//                System.out.println(pos);
                *//*Favorites.favIds.remove(postion);
                ((BaseAdapter) Favorites.listView.getAdapter()).notifyDataSetChanged();
                System.out.println(search.ingredients);*//*
            }
        });
*/



        if(Favorites.favIds.indexOf(recipe.getIdNumber()) >= 0) {
            recipe.Clicked = true;
        }

//        if (checked[position]) {
//            favorite.setBackgroundResource(R.drawable.heartfilled);
//        }

        favorite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                recipe.Clicked = !recipe.Clicked;

                notifyDataSetChanged();

                final int pos;
                if (MainMenu.listView.getAdapter() == RecipeAdapter.this) {
                    pos =  MainMenu.listView.getPositionForView(v);
                } else {
                    pos = search_results.lv.getPositionForView(v);
                }
                
                checked[pos] = !checked[pos];
                if (checked[pos] == false) {
                    if (Favorites.favIds.indexOf(recipe.getIdNumber()) >= 0) {

                        Favorites.favIds.remove(Favorites.favIds.indexOf(recipe.getIdNumber()));

                        Favorites.saveFavorites();
                        System.out.println(Favorites.favIds);
                        Context context = getContext();
                        CharSequence text = recipe.getName() + " removed from favorites!";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                } else if (checked[pos] == true) {
                    if (Favorites.favIds.indexOf(recipe.getIdNumber()) >= 0) {
                        return;
                    }
                    Favorites.favIds.add(recipe.getIdNumber());
                    Favorites.saveFavorites();
                    Context context = getContext();
                    CharSequence text = recipe.getName() + " added to favorites!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }


        });

        if(!recipe.Clicked){
            favorite.setBackgroundResource(R.drawable.blankheart);
        }else{
            favorite.setBackgroundResource(R.drawable.heartfilled);
        }

        name.setText(recipe.getName());

        return RecipeView;
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            bmImage.setScaleType(ImageView.ScaleType.FIT_XY);
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setScaleType(ImageView.ScaleType.FIT_XY);
            bmImage.setImageBitmap(result);
        }
    }


}