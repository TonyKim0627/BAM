package com.example.jpeykar.bam;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TypefaceSpan;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment favorites;
    private Fragment newRecipe;
    private Fragment recentRecipes;
    private Fragment mainMenu;
    private FragmentTransaction transaction;
    Toolbar mActionBarToolbar;
    static FragmentManager manager;
    static SharedPreferences mSharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if (savedInstanceState == null) {
//            MainMenu test = new MainMenu();
//            test.setArguments(getIntent().getExtras());
//            getSupportFragmentManager().beginTransaction().replace(android.R.id.content, test, "tag").commit();
//        } else {
//            MainMenu test = (MainMenu) getSupportFragmentManager().findFragmentByTag("tag");
//        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mSharedPreference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        favorites = new Favorites();
        newRecipe = new search();
        recentRecipes = new RecentRecipes();
        mainMenu = new MainMenu();
        Favorites.favIds = new ArrayList<Integer>();
        Favorites.loadFavorites();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, newRecipe).commit();
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);


        mActionBarToolbar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                closeKeyboard();
            }
        });


        Typeface font2 = Typeface.createFromAsset(getAssets(), "cookies.ttf");
        SpannableStringBuilder SS = new SpannableStringBuilder("MY Actionbar Tittle");
        SS.setSpan(new CustomTypefaceSpan("Search", font2), 0, SS.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(SS);

        getSupportActionBar().setTitle("Search");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        closeKeyboard();
        //super.onBackPressed();
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        Fragment fragment = manager.findFragmentById(R.id.fragment_container);
//        String fragmentName = fragment.getClass().getSimpleName();
//
//        if(fragmentName.equals("search_results")){
//            this.getFragmentManager().beginTransaction().addToBackStack(null);
//            return;
//        }
//
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        closeKeyboard();
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if(this.getCurrentFocus() != null && this.getCurrentFocus() instanceof EditText){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        int id = item.getItemId();
        closeKeyboard();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent a = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(a);
            return true;
        } else if (id == R.id.action_search) {
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, newRecipe);
            transaction.addToBackStack(null);
            transaction.commit();
            closeKeyboard();
            mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
            getSupportActionBar().setTitle("Search");
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        closeKeyboard();
        if (id == R.id.main_menu) {
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, mainMenu);
            transaction.addToBackStack(null);
            transaction.commit();
            closeKeyboard();
            mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
            getSupportActionBar().setTitle("Main Menu");
        } else if (id == R.id.favorites) {
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, favorites);
            transaction.addToBackStack(null);
            transaction.commit();
            closeKeyboard();
            mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
            getSupportActionBar().setTitle("Favorites");
//        } else if (id == R.id.recent_recipes) {
//            transaction = getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.fragment_container, recentRecipes);
//            transaction.addToBackStack(null);
//            transaction.commit();
//            closeKeyboard();
//            mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
//            getSupportActionBar().setTitle("Recent Recipes");
        } else if (id == R.id.new_recipe) {
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, newRecipe);
            transaction.addToBackStack(null);
            transaction.commit();
            closeKeyboard();
            mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
            getSupportActionBar().setTitle("New Recipe");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }


}
