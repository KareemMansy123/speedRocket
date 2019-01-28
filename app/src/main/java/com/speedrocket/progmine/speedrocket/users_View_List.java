package com.speedrocket.progmine.speedrocket;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.speedrocket.progmine.speedrocket.Control.UsersSearchAdapter;
import com.speedrocket.progmine.speedrocket.Model.ApiInterfaceUsersSearch;
import com.speedrocket.progmine.speedrocket.Model.ApiUsersSearch;
import com.speedrocket.progmine.speedrocket.Model.UsersSearchContent;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class users_View_List extends AppCompatActivity {
    public SearchView searchView;
    public RecyclerView recyclerView;
    private UsersSearchAdapter UserAdapter;
    private List<UsersSearchContent> UsersList;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users__view__list);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        UserAdapter = new UsersSearchAdapter();
        recyclerView.setAdapter(UserAdapter);

        UsersList = new ArrayList<>();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ApiInterfaceUsersSearch apiService = ApiUsersSearch.getClient().create(ApiInterfaceUsersSearch.class);
                Call<List<UsersSearchContent>> call = apiService.getMovies(query);

                call.enqueue(new Callback<List<UsersSearchContent>>() {
                    @Override
                    public void onResponse(Call<List<UsersSearchContent>> call, Response<List<UsersSearchContent>> response) {


                        UsersList = response.body();
                        Log.d("TAG","Response = "+UsersList);
                        UserAdapter.setMovieList(getApplicationContext(),UsersList);



                    }

                    @Override
                    public void onFailure(Call<List<UsersSearchContent>> call, Throwable t) {
                        Log.d("TAG","Response = "+t.toString());

                    }
                });
                UserAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                UserAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
