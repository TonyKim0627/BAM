package com.example.jpeykar.bam;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mashape.relocation.HttpEntity;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.*;

import java.util.ArrayList;

public class MainMenu extends Fragment{

    private FragmentActivity myContext;
    private View view;
    protected static RecipeAdapter aa;
    protected static ListView listView;
    protected static ArrayList<Recipe> recipeArray = new ArrayList<Recipe>();
    private static boolean done = true;
    private String photoURL = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_menu, container, false);

        if (done) {
            new CallMashapeAsync().execute();
            done = false;
        }
        setupListData();



        return view;
    }

    private class CallMashapeAsync extends AsyncTask<String, Integer, HttpResponse<JsonNode>> {

        protected HttpResponse<JsonNode> doInBackground(String... msg) {

            HttpResponse<JsonNode> request = null;
            try {
                request= Unirest.get("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/findByIngredients?fillIngredients=false&ingredients=cherry&limitLicense=false&number=5&ranking=1")
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
                        System.out.println(ID);
                        Recipe recTemp = new Recipe(title, "", photoURL);

                        recTemp.setId(ID);
                        recipeArray.add(recTemp);
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
            //TextView txtView = (TextView) getActivity().findViewById(R.id.textView1);
            //txtView.setText(answer);

            //listView = (ListView) view.findViewById(R.id.listView);



            //aa = new RecipeAdapter(getActivity(), R.layout.recipe_layout, recipeArray);
            //listView.setAdapter(aa);

            ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();




        }

    }


    private void setupListData() {

        //setup data
        listView = (ListView) view.findViewById(R.id.listView);


        aa = new RecipeAdapter(getActivity(), R.layout.recipe_layout, recipeArray);
        listView.setAdapter(aa);

        ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();

        for (int i = 0; i < recipeArray.size(); i++) {
            aa.checked[i] = false;
            if(Favorites.favIds.indexOf(recipeArray.get(i).getIdNumber()) >= 0) {
                aa.checked[i] = true;
            }
        }



//        Recipe temp = new Recipe("Recipe Number One", "asdf", "");
//        recipeArray.add(temp);
//        Recipe temp2 = new Recipe("Recipe Number Two", "asdf", "");
//        recipeArray.add(temp2);
//        Recipe temp3 = new Recipe("Recipe Number Three", "asdf", "");
//        recipeArray.add(temp3);





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

        //updateArray();

    }


    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onStop(){
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


}
