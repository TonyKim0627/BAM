package com.example.jpeykar.bam;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.mashape.unirest.http.*;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.ArrayList;
import java.util.Collections;

public class search extends Fragment {


    private FragmentActivity myContext;
    protected static View view;
    protected static ListView lv;
    EditText inputSearch;

    protected static IngredientAdapter aa;
    protected static ArrayList<String> ingredients = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_search, container, false);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ingredients.clear();

        lv = (ListView) view.findViewById(R.id.list_view);
        inputSearch = (EditText) view.findViewById(R.id.inputSearch);

        Button b = (Button) view.findViewById(R.id.add_button);
        Button s = (Button) view.findViewById(R.id.search);




        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = inputSearch.getText().toString();
                if (text.equals("")){
                    Snackbar.make(v, "Please enter the ingredient!", Snackbar.LENGTH_LONG)
                            .setAction("CLOSE", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                            .show();
                    return;
                }
                String replaced = "";
                replaced = text.replace(' ', '+');
                ingredients.add(replaced);
                Collections.sort(ingredients, String.CASE_INSENSITIVE_ORDER);
                ((BaseAdapter) lv.getAdapter()).notifyDataSetChanged();
                inputSearch.setText("");

            }
        });

        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ingredients.isEmpty()){
                    Snackbar.make(v, "Please add the ingredient(s) first! :)", Snackbar.LENGTH_LONG)
                            .setAction("CLOSE", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                            .show();
                    return;
                }

                getActivity().getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                Fragment frag = new search_results();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, frag);
                ft.addToBackStack(null);
                ft.commit();



            }
        });


        aa = new IngredientAdapter(getActivity(), R.layout.search_ingredient_layout, ingredients);
        lv.setAdapter(aa);
        lv.setLongClickable(true);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int pos, long id) {
                System.out.println("clicked");
                final int postion = lv.getPositionForView(arg1);
                ingredients.remove(pos);
                ((BaseAdapter) lv.getAdapter()).notifyDataSetChanged();
                System.out.println(ingredients);

            }
        });




        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                inputSearch.setHint("");
                System.out.println(cs);
                /*if(cs.toString().indexOf(" ") > 0) {
                    ingredients.add(cs.toString().substring(0,cs.length()-1));
                    Collections.sort(ingredients, String.CASE_INSENSITIVE_ORDER);
                    ((BaseAdapter) lv.getAdapter()).notifyDataSetChanged();
                    inputSearch.setText("");
                }*/
                System.out.println(ingredients);


                //search.this.aa.getFilter().filter(cs.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });





        return view;

    }


    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onResume(){
        super.onResume();
    }


}