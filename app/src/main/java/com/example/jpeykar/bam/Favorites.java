package com.example.jpeykar.bam;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.mashape.relocation.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class Favorites extends Fragment {

    private FragmentActivity myContext;
    private static View view;
    protected static FavoritesAdapter aa;
    protected static ArrayList<Recipe> recipeArray;
    protected static ArrayList<Integer> favIds;
    protected static ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favorites, container, false);
        setupListData();
        new CallMashapeAsync().execute();


        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void setupListData() {

        //setup data
        listView = (ListView) view.findViewById(R.id.listViewFavorites);

        recipeArray = new ArrayList<Recipe>();
        aa = new FavoritesAdapter(getActivity(), R.layout.recipe_layout, recipeArray);
        listView.setAdapter(aa);
        listView.setLongClickable(true);

        getRecpiesFromIds();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int pos, long id) {
                String name = recipeArray.get(pos).getName();
                int id1 = recipeArray.get(pos).getIdNumber();
                String URL = recipeArray.get(pos).getImage();

                Intent a = new Intent(getActivity(), RecipeActivity.class);
                a.putExtra("ID", id1);
                a.putExtra("URL", URL);
                a.putExtra("NAME", name);
                startActivity(a);

            }
        });

    }

    public void getRecpiesFromIds() {
        for (int i = 0; i < favIds.size(); i++) {

        }
    }

    public static void loadFavorites() {
        System.out.println("loading favs");
        SharedPreferences mSharedPreference1 = MainActivity.mSharedPreference;
        favIds.clear();
        int size = mSharedPreference1.getInt("Status_size", 0);
        System.out.println("size: " + size);

        for (int i = 0; i < size; i++) {
            favIds.add(Integer.parseInt(mSharedPreference1.getString("Status_" + i, null)));
        }
       // getRecpiesFromIds();
    }

    public static void saveFavorites() {
        SharedPreferences sp = MainActivity.mSharedPreference;
        SharedPreferences.Editor mEdit1 = sp.edit();

        mEdit1.putInt("Status_size", favIds.size());

        for (int i = 0; i < favIds.size(); i++) {
            mEdit1.remove("Status_" + i);
            mEdit1.putString("Status_" + i, favIds.get(i) + "");
        }

        mEdit1.commit();
    }

    public static void clearFavorites() {
        SharedPreferences sp = MainActivity.mSharedPreference;
        SharedPreferences.Editor mEdit1 = sp.edit();
        mEdit1.clear();
        mEdit1.commit();
    }

    private class CallMashapeAsync extends AsyncTask<String, Integer, com.mashape.unirest.http.HttpResponse<JsonNode>> {

        protected com.mashape.unirest.http.HttpResponse<JsonNode> doInBackground(String... msg) {

            com.mashape.unirest.http.HttpResponse<JsonNode> request = null;
            try {
                // These code snippets use an open-source library. http://unirest.io/java
                for (int i = 0; i < favIds.size(); i++) {
                    request = Unirest.get("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/" + favIds.get(i) + "/information?includeNutrition=false")
                            .header("X-Mashape-Key", "4MNAx9buaFmshV1xvL6oVsZHiwHSp1ryEyBjsnEbOO4Q04tT0K")
                            .asJson();
                    System.out.println(favIds.get(i));


                    JSONArray jsonarr = request.getBody().getArray();
                    String jsonStr = jsonarr.toString();

                    try {
                        //JSONArray jsonArray = new JSONArray(jsonStr);
                        for (int j = 0; j < jsonarr.length(); j++) {
                            JSONObject c = jsonarr.getJSONObject(j);
                            int ID = c.getInt("id");
                            String title = c.getString("title");
                            String photoURL = c.getString("image");
                            System.out.println(title);
                            Recipe recTemp = new Recipe(title, "", photoURL);

                            recTemp.setId(ID);
                            recipeArray.add(recTemp);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            } catch (UnirestException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return request;

        }

        protected void onProgressUpdate(Integer...integers) {

        }

        protected void onPostExecute(com.mashape.unirest.http.HttpResponse<JsonNode> response) {
            //String answer = response.getBody().toString();
            //System.out.println(answer);
            //TextView txtView = (TextView) getActivity().findViewById(R.id.textView1);
            //txtView.setText(answer);

            //listView = (ListView) view.findViewById(R.id.listView);



            //aa = new RecipeAdapter(getActivity(), R.layout.recipe_layout, recipeArray);
            //listView.setAdapter(aa);

            ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();




        }

    }

}