package com.jpujolji.testapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jpujolji.testapp.adapter.ItemListAdapter;
import com.jpujolji.testapp.R;
import com.jpujolji.testapp.Utils;
import com.jpujolji.testapp.model.ItemList;
import com.jpujolji.testapp.network.HttpClient;
import com.jpujolji.testapp.network.HttpInterface;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements HttpInterface, SwipeRefreshLayout.OnRefreshListener, ItemListAdapter.ItemInterface {

    RecyclerView rvItems;
    SwipeRefreshLayout swipeRefreshLayout;
    CoordinatorLayout coordinatorLayout;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        toolbar.setTitle(R.string.app_name);

        realm = Realm.getDefaultInstance();

        rvItems = (RecyclerView) findViewById(R.id.rvItems);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        rvItems.setHasFixedSize(true);
        rvItems.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        swipeRefreshLayout.setOnRefreshListener(this);

        if (savedInstanceState != null && savedInstanceState.getBoolean("save_state")) {
            loadData();
            return;
        }

        swipeRefreshLayout.setRefreshing(true);

        if (Utils.isOnline(MainActivity.this)) {
            HttpClient client = new HttpClient(this);
            client.httpRequest();
        } else {
            showSnackbar();
            loadData();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onProgress(long progress) {

    }

    @Override
    public void onSuccess(JSONObject response) {
        List<ItemList> items = new ArrayList<>();
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(response.toString());
        if (element.isJsonObject()) {
            JsonObject jsonObject = element.getAsJsonObject();
            JsonObject data = jsonObject.get("data").getAsJsonObject();
            JsonArray childrens = data.get("children").getAsJsonArray();

            for (int x = 0; x < childrens.size(); x++) {
                JsonObject children = childrens.get(x).getAsJsonObject();
                JsonObject item = children.get("data").getAsJsonObject();
                ItemList itemList = gson.fromJson(item, ItemList.class);
                Log.i("Depuracion", "title " + itemList.title);
                items.add(itemList);
            }
            saveItems(items);
        }
    }

    @Override
    public void onFailed(JSONObject errorResponse) {

    }

    public void saveItems(final List<ItemList> items) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(items);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                ItemListAdapter mAdapter = new ItemListAdapter(items, MainActivity.this, MainActivity.this);
                rvItems.setAdapter(mAdapter);
                rvItems.setItemAnimator(new DefaultItemAnimator());
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void loadData() {
        RealmResults<ItemList> items = realm.where(ItemList.class).findAll();
        ItemListAdapter mAdapter = new ItemListAdapter(items, MainActivity.this, this);
        rvItems.setAdapter(mAdapter);
        rvItems.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onRefresh() {
        if (Utils.isOnline(MainActivity.this)) {
            HttpClient client = new HttpClient(this);
            client.httpRequest();
        } else {
            swipeRefreshLayout.setRefreshing(false);
            showSnackbar();
        }
    }

    public void showSnackbar() {
        Snackbar.make(coordinatorLayout, "No hay conexión a internet", Snackbar.LENGTH_LONG)
                .setAction("Configuración", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                }).show();
    }

    @Override
    public void onItemClick(View view, String id) {
        Intent intent = new Intent(MainActivity.this, DetailItemActivity.class);
        intent.putExtra(DetailItemActivity.ID_ITEM, id);
        ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                MainActivity.this,
                new Pair<>(view.findViewById(R.id.ivIcon),
                        DetailItemActivity.VIEW_CITY));
        ActivityCompat.startActivity(MainActivity.this, intent, activityOptions.toBundle());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("save_state", true);
        super.onSaveInstanceState(outState);
    }
}