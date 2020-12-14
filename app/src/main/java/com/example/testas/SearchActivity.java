package com.example.testas;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.corona.coronazp20t.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    public static final String Margarita_API = "https://www.thecocktaildb.com/api/json/v1/1/search.php?s=Margarita";

    private RecyclerView recyclerView;
    private Adapter adapter;

    private ArrayList<Margarita> margaritaList = new ArrayList<Margarita>();

    SearchView searchView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        AsyncFetch asyncFetch = new AsyncFetch();
        asyncFetch.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // adds item to action bar
        getMenuInflater().inflate(R.menu.search, menu);

        // Get Search item from action bar and Get Search service
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) SearchActivity.this.getSystemService(Context.SEARCH_SERVICE);
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(SearchActivity.this.getComponentName()));
            searchView.setIconified(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    // Every time when you press search button on keypad an Activity is recreated which in turn calls this function
    @Override
    protected void onNewIntent(Intent intent) {
        // Get search query
        super.onNewIntent(intent);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            if (searchView != null) {
                searchView.clearFocus();
            }

            // From all countries covid list creates specific list by searched country
            ArrayList<Margarita> margaritaListByName = JSON.getMargaritaByQuery(margaritaList, query);

            if (margaritaListByName.size() == 0) {
                Toast.makeText(this, getResources().getString(R.string.search_no_results) + query, Toast.LENGTH_SHORT).show();
            }

            // Setup and Handover data to recyclerview
            recyclerView = (RecyclerView) findViewById(R.id.margarita_list);
            adapter = new Adapter(SearchActivity.this, margaritaListByName);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        }
    }

    private class AsyncFetch extends AsyncTask<String, String, ArrayList<Margarita>> {
        ProgressDialog pdLoading = new ProgressDialog(SearchActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on UI thread
            pdLoading.setMessage(getResources().getString(R.string.search_loading_data));
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected ArrayList<Margarita> doInBackground(String... params) {
            try {
                JSONObject jsonObject = JSON.readJsonFromUrl(Margarita_API);

                    JSONArray jsonArray = null;
                    margaritaList = new ArrayList<Margarita>();
                    try {
                        jsonArray = JSON.getJSONArray(jsonObject);
                        margaritaList = JSON.getList(jsonArray);
                    } catch (JSONException e) {
                        Toast.makeText(
                                SearchActivity.this,
                                getResources().getText(R.string.search_error_reading_data) + e.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                    return margaritaList;
            } catch (JSONException | IOException e1) {
                Toast.makeText(
                        SearchActivity.this,
                        getResources().getText(R.string.search_error_reading_data) + e1.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
                return null;
            }
        }// doInBackground

        @Override
        protected void onPostExecute(ArrayList<Margarita> margaritaList) {
            //this method will be running on UI thread
            pdLoading.dismiss();

           if(margaritaList != null) {
               Toast.makeText(SearchActivity.this, getResources().getString(R.string.search_found_entries_from_api) + margaritaList.size(), Toast.LENGTH_SHORT).show();
           }
        }//onPostExecute
    }//AsyncFetch class
}
