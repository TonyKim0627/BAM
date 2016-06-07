package com.example.jpeykar.bam;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.jpeykar.bam.R;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class search_results extends Fragment {

    private View view;
    protected static ListView lv;
    protected static ArrayList<Integer> recentSearches;
    protected static RecipeAdapter aa;
    protected static ArrayList<Recipe> search_array = new ArrayList<Recipe>();
    private String photoURL;
    protected static ArrayList<Recipe> recipeArray = new ArrayList<Recipe>();
    protected String ingredList = "";


    public search_results() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        view = inflater.inflate(R.layout.fragment_search_results, container, false);


        recentSearches = new ArrayList<Integer>();
        for (int i = 0; i < search.ingredients.size(); i++) {
            ingredList += search.ingredients.get(i);
            if (i < search.ingredients.size() - 1) {
                ingredList += "%2C";
            }
        }
        System.out.println(ingredList);
        recipeArray.clear();
        if(aa != null) {
            aa.clear();
        }
        new CallMashapeAsync().execute();
        loadSearches();


        showBackButton();

        populateListView();

        return view;
    }

    public void populateListView(){




        lv = (ListView) view.findViewById(R.id.search_listview);

        aa = new RecipeAdapter(getActivity(), R.layout.recipe_layout, search_array);
        lv.setAdapter(aa);

        //add the recipe objects into the recipe array
        //populating here


        System.out.println("Before: " + search_array);
        //new CallMashapeAsync().execute();
        System.out.println("After: " + search_array);


        ((BaseAdapter) lv.getAdapter()).notifyDataSetChanged();

        for (int i = 0; i < recipeArray.size(); i++) {
            aa.checked[i] = false;
            if(Favorites.favIds.indexOf(recipeArray.get(i).getIdNumber()) >= 0) {
                aa.checked[i] = true;
            }
        }



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int pos, long id) {
                String name = search_array.get(pos).getName();
                int id1 = search_array.get(pos).getIdNumber();
                String URL = search_array.get(pos).getImage();

                recentSearches.add(id1);
                saveSearches();

                Intent a = new Intent(getActivity(), RecipeActivity.class);
                a.putExtra("ID", id1);
                a.putExtra("URL", URL);
                a.putExtra("NAME", name);
                startActivity(a);


            }
        });
    }

    private class CallMashapeAsync extends AsyncTask<String, Integer, HttpResponse<JsonNode>> {

        protected HttpResponse<JsonNode> doInBackground(String... msg) {

            HttpResponse<JsonNode> request = null;
            try {
                System.out.println("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/findByIngredients?fillIngredients=false&ingredients=" +ingredList+"&limitLicense=false&number=5&ranking=1");
                request= Unirest.get("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/findByIngredients?fillIngredients=false&ingredients=" +ingredList+"&limitLicense=false&number=5&ranking=1")
                        .header("X-Mashape-Key", "4MNAx9buaFmshV1xvL6oVsZHiwHSp1ryEyBjsnEbOO4Q04tT0K")
                        .header("Accept", "application/json")
                        .asJson();


                JSONArray jsonarr = request.getBody().getArray();
                String jsonStr = jsonarr.toString();

                try {
                    //JSONArray jsonArray = new JSONArray(jsonStr);
                    for (int i = 0; i < jsonarr.length(); i++) {
                        JSONObject c = jsonarr.getJSONObject(i);
                        int ID = c.getInt("id");
                        String title = c.getString("title");
                        photoURL = c.getString("image");
                        System.out.println(title);
                        Recipe recTemp = new Recipe(title, "", photoURL);

                        recTemp.setId(ID);
                        search_array.add(recTemp);
                    }

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
            ((BaseAdapter) lv.getAdapter()).notifyDataSetChanged();





        }
    }

    public void loadSearches() {
        SharedPreferences mSharedPreference1 = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        recentSearches.clear();
        int size = mSharedPreference1.getInt("Recent_size", 0);

        for (int i = 0; i < size; i++) {
            recentSearches.add(Integer.parseInt(mSharedPreference1.getString("Recent_" + i, null)));
        }
    }

    public void saveSearches() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        SharedPreferences.Editor mEdit1 = sp.edit();

        mEdit1.putInt("Recent_size", recentSearches.size());

        for (int i = 0; i < recentSearches.size(); i++) {
            mEdit1.remove("Recent_" + i);
            mEdit1.putString("Recent_" + i, recentSearches.get(i) + "");
        }

        mEdit1.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int count = getFragmentManager().getBackStackEntryCount();

        if (count != 0) {
            getFragmentManager().popBackStack();
            return true;
        }
        return false;
    }

    public void showBackButton() {
        if (getActivity() instanceof ActionBarActivity) {
            ((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        search.ingredients = new ArrayList<String>();
        //this.getActivity().getFragmentManager().beginTransaction().addToBackStack(null);
    }



}
