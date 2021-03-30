package com.example.popasdelivery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.Response;

public class WorkPage extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    View navController;
    public TextView valueCartIndicator;
    public RecyclerView recyclerView;
    public GridLayoutManager gridLayoutManager;
    public CustomAdapter adapter;
    public static List<MyOrder> order_list;
    public static List<MyData> data_list;
    String url = "http://167.99.246.91/getproduct.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_page);
        bottomNavigationView = findViewById(R.id.bottomnavig);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        loadFragment(new BlankFragment());
        /**valueCartIndicator = findViewById(R.id.textView14);
        ImageButton btnSignOut = findViewById(R.id.imagebutton5);
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**SaveSharedPreference.clearUserLogin(WorkPage.this);
                Intent back = new Intent(WorkPage.this, Dashboard.class);
                back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(back);**/
                /**Intent cart = new Intent(WorkPage.this, CartApp.class);
                startActivity(cart);
            }
        });
        recyclerView = findViewById(R.id.recycler_view);
        order_list = new ArrayList<>();
        data_list = new ArrayList<>();
        on_load_from_server(1);

        gridLayoutManager = new GridLayoutManager(WorkPage.this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new CustomAdapter(WorkPage.this, data_list, valueCartIndicator, order_list);
        recyclerView.setAdapter(adapter);

        /**recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if(gridLayoutManager.findLastCompletelyVisibleItemPosition() == data_list.size()-1){
                    on_load_from_server(data_list.get(data_list.size()-1).getId());
                }
            }
        });**/
        /**bottomNavigationView = findViewById(R.id.bottomnavig);

        bottomNavigationView.setSelectedItemId(R.id.workPage);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.workPage:
                        Toast.makeText(WorkPage.this, "La multi ani", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.orders:
                        Intent intentOrders = new Intent(WorkPage.this, Orders.class);
                        finish();
                        startActivity(intentOrders);
                        break;
                    case R.id.account:
                        Intent intentAccount = new Intent(WorkPage.this, Account.class);
                        startActivity(intentAccount);
                        break;
                }
                return true;
            }
        });**/
    }

    public boolean loadFragment(Fragment fragment){
       if(fragment!=null){
           getSupportFragmentManager().beginTransaction()
                   .replace(R.id.fragment_layout,fragment)
                   .commit();
       }
       return true;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;
        switch(item.getItemId()){
            case R.id.workpage:
                if(bottomNavigationView.getSelectedItemId() != R.id.workpage){
                    selectedFragment = new BlankFragment();
                }
                break;
            case R.id.orders:
                if(bottomNavigationView.getSelectedItemId() != R.id.orders){
                    selectedFragment = new Comenzi();
                }
                break;
            case R.id.account:
                if(bottomNavigationView.getSelectedItemId() != R.id.account){
                    selectedFragment = new Cont();
                }
                break;
        }
        if(selectedFragment != null){
            return loadFragment(selectedFragment);
        }
        else{
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        if(bottomNavigationView.getSelectedItemId() == R.id.workpage){
            super.onBackPressed();
            finish();
        }
        else{
            bottomNavigationView.setSelectedItemId(R.id.workpage);
        }
    }

    /**public void on_load_from_server(final int id) {
        AsyncTask<Integer, Void, Void> task = new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... integers) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://167.99.246.91/getproduct.php?id="+id)
                        .build();
                try {
                    Response response = client.newCall(request).execute();

                    JSONArray array = new JSONArray(response.body().string());

                    for(int i = 0; i < array.length(); i++){
                        JSONObject object = array.getJSONObject(i);

                        MyData data = new MyData(object.getInt("id"),object.getString("nume"),object.getString("pret"),object.getString("imagine"),object.getString("descriere"));
                        data_list.add(data);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e){
                    System.out.println("End of content");
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                adapter.notifyDataSetChanged();
            }
        };
        task.execute(id);
    }**/
}