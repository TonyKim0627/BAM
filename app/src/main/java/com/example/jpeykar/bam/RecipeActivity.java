package com.example.jpeykar.bam;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telecom.Call;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;

public class RecipeActivity extends AppCompatActivity {

    private Recipe curRecipe;
    private int ID;
    private String url = "";
    private String imgUrl = "";
    private ArrayList<String> ingredients;
    private String directions = "";
    private String name = "";
    private String ingredRet = "";
    CallbackManager callbackManager;
    ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
//        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
//
//        });
        setContentView(R.layout.activity_recipe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int id = extras.getInt("ID");
            this.ID = id;
            String URL = extras.getString("URL");
            this.imgUrl = URL;
            String NAME = extras.getString("NAME");
            this.name = NAME;
        }
        new CallMashapeAsync().execute();
        new CallMashapeAsync1().execute();

        //curRecipe = new Recipe("Steak", "flour, rice", R.drawable.ic_action_name);

        setTitle(name);

        //ImageView pic = (ImageView) findViewById(R.id.imageView2);
        new DownloadImageTask((ImageView) findViewById(R.id.imageView2))
                .execute(imgUrl);
        //pic.setImageResource(curRecipe.getImage());
        TextView tv1 = (TextView) findViewById(R.id.textView3);
        tv1.setMovementMethod(new ScrollingMovementMethod());
        final TextView descriptionText = tv1;
        final ToggleButton instructions = (ToggleButton) findViewById(R.id.button);
        //final ToggleButton ingredients = (ToggleButton) findViewById(R.id.button2);
        instructions.setText("Ingredients");
        instructions.setTextOn("Instructions");
        instructions.setTextOff("Ingredients");
//        ingredients.setText("Ingredients");
//        ingredients.setTextOn("Ingredients");
//        ingredients.setTextOff("Ingredients");
        descriptionText.setText("Select Ingredients or Instructions");
        instructions.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //ingredients.setChecked(false);
                    descriptionText.setText(directions);
                    if (directions.equals("null")) {
                        descriptionText.setText("Instructions unavailable");
                    }
                    System.out.println(directions);
                } else {
                    //ingredients.setChecked(true);
                    descriptionText.setText(ingredRet);
                }
            }
        });

//        ingredients.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    instructions.setChecked(false);
//                    descriptionText.setText(ingredRet);
//                } else {
//                    instructions.setChecked(true);
//                }
//            }
//        });



    }



    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
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

    private class CallMashapeAsync extends AsyncTask<String, Integer, HttpResponse<JsonNode>> {

        protected HttpResponse<JsonNode> doInBackground(String... msg) {

            HttpResponse<JsonNode> request = null;
            try {
                request = Unirest.get("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/" + ID + "/information?includeNutrition=false")
                        .header("X-Mashape-Key", "4MNAx9buaFmshV1xvL6oVsZHiwHSp1ryEyBjsnEbOO4Q04tT0K")
                        .asJson();
                JSONObject jsonObj = request.getBody().getObject();
                try {
                    url = jsonObj.getString("sourceUrl");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (UnirestException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return request;
        }

        protected void onProgressUpdate(Integer...integers) {

        }

        protected void onPostExecute(HttpResponse<JsonNode> response) {
            String answer = response.getBody().toString();
            System.out.println(answer);
            //TextView txtView = (TextView) findViewById(R.id.textView1);
            //txtView.setText(answer);
        }
    }

    private class CallMashapeAsync1 extends AsyncTask<String, Integer, HttpResponse<JsonNode>> {

        protected HttpResponse<JsonNode> doInBackground(String... msg) {

            HttpResponse<JsonNode> request = null;
            try {
                request = Unirest.get("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/extract?forceExtraction=false&url="+ url)
                        .header("X-Mashape-Key", "4MNAx9buaFmshV1xvL6oVsZHiwHSp1ryEyBjsnEbOO4Q04tT0K")
                        .asJson();


                JSONObject jsonObj = request.getBody().getObject();

                try {
                    JSONArray jsonArr = jsonObj.getJSONArray("extendedIngredients");
                    ingredients = new ArrayList<>();
                    for (int i = 0; i < jsonArr.length(); i++) {
                        JSONObject c = jsonArr.getJSONObject(i);
                        String ingred = c.getString("originalString");
                        System.out.println(ingred);
                        ingredients.add(ingred);
                    }
                    convert(ingredients);
                    directions = jsonObj.getString("text");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (UnirestException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return request;
        }

        protected void onProgressUpdate(Integer...integers) {
        }

        protected void onPostExecute(HttpResponse<JsonNode> response) {
            //String answer = response.getBody().toString();
            //System.out.println(answer);
            //TextView txtView = (TextView) findViewById(R.id.textView1);
            //txtView.setText(answer);








        }
    }

    public void convert(ArrayList<String> temp) {
        ingredRet = "";
        for (int i = 0; i < temp.size(); i++) {
            ingredRet += temp.get(i);
            ingredRet += "\n";
        }
    }

    public void shareToFacebook() {
        if (ShareDialog.canShow(ShareLinkContent.class) && url != null) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(name)
                    .setContentDescription(
                            "A recipe found on BAM!")
                    .setContentUrl(Uri.parse(url))
                    .build();

            shareDialog.show(linkContent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recipe_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_facebook:
                shareToFacebook();
                break;
            case android.R.id.home:
                // app icon in action bar clicked; go home
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }




}
