package com.example.jpeykar.bam;


import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
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

public class FavoritesAdapter extends ArrayAdapter<Recipe> {

    int resource;

    public FavoritesAdapter(Context ctx, int res, List<Recipe> items)
    {
        super(ctx, res, items);
        resource = res;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout RecipeView;
        final Recipe recipe = getItem(position);

        if (convertView == null) {
            RecipeView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(resource, RecipeView, true);
        } else {
            RecipeView = (LinearLayout) convertView;
        }

        ImageView image = (ImageView) RecipeView.findViewById(R.id.layout_image);
        TextView name = (TextView) RecipeView.findViewById(R.id.layout_name);
        final Button favorite = (Button) RecipeView.findViewById(R.id.layout_favorite);
        favorite.setBackgroundResource(R.drawable.heartfilled);
        //favorite.setChecked(true);

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(Favorites.favIds.indexOf(recipe.getIdNumber()) >= 0) {
                        Favorites.favIds.remove(Favorites.favIds.indexOf(recipe.getIdNumber()));
                        favorite.setBackgroundResource(R.drawable.blankheart);
                        Favorites.saveFavorites();
                        final int postion = Favorites.listView.getPositionForView(v);
                        Favorites.recipeArray.remove(postion);

                        ((BaseAdapter) Favorites.listView.getAdapter()).notifyDataSetChanged();

                        System.out.println(Favorites.favIds);
                        Context context = getContext();
                        CharSequence text = recipe.getName() + " removed from favorites!";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                    }
//
            }
        });

//        favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (!isChecked) {
//                    if(Favorites.favIds.indexOf(recipe.getIdNumber()) >= 0) {
//                        Favorites.favIds.remove(Favorites.favIds.indexOf(recipe.getIdNumber()));
//                        Favorites.saveFavorites();
//                        ((BaseAdapter) Favorites.listView.getAdapter()).notifyDataSetChanged();
//                        System.out.println(Favorites.favIds);
//                    }
//                }
//            }
//        });

        name.setText(recipe.getName());
        new DownloadImageTask((ImageView) RecipeView.findViewById(R.id.layout_image))
                .execute(recipe.getImage());

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