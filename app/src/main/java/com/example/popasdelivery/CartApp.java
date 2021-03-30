package com.example.popasdelivery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.muddzdev.styleabletoast.StyleableToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CartApp extends AppCompatActivity {

    public TextView textTva;
    public static TextView totalPrice;
    public List<MyOrder> theOrders;
    public List<MyQuantity> myQuantity;
    public List<MyOrderData> myOrderData;
    public List<Integer> counterHtlp;
    int totalValue = 0;
    int totalCan;
    boolean help = false;
    boolean helpem;
    int temp = 0;
    Button registerComanda;
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    CustomAdapterCart adapter;
    ImageButton buttonBack;
    List<MyClientData> myClientData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_app);
        textTva = findViewById(R.id.textView17);
        registerComanda = findViewById(R.id.button6);
        totalPrice = findViewById(R.id.textView18);
        SharedPreferences sharedPreferences = getSharedPreferences(Register_Login.SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = getSharedPreferences(Register_Login.SHARED_PREFS, MODE_PRIVATE).edit();
        String email = sharedPreferences.getString("email","");
        theOrders = new ArrayList<>();
        myQuantity = new ArrayList<>();
        String jsonString = sharedPreferences.getString("theOrders","");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<MyOrder>>() {}.getType();
        List<MyOrder> list = gson.fromJson(jsonString, type);
        theOrders = list;
        jsonString = sharedPreferences.getString("cantitate","");
        gson = new Gson();
        type = new TypeToken<ArrayList<MyQuantity>>() {}.getType();
        List<MyQuantity> listCant = gson.fromJson(jsonString, type);
        myQuantity = listCant;

        try{
            for (int i = 0; i < theOrders.size(); i++) {
                    int cantitate = myQuantity.get(i).getCantitate();
                    int pret = Integer.parseInt(theOrders.get(i).getPret());
                    totalValue += cantitate * pret;
            }
        }catch(Exception e){
            System.out.println(e);
        }

        totalPrice.setText(Integer.toString(totalValue) + ",00 RON");
        editor.putInt("totalValue", totalValue);
        editor.commit();

        myClientData = new ArrayList<>();
        getClientId(email);


        registerComanda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalValue = sharedPreferences.getInt("totalValue", 0);
                if (totalValue != 0) {
                    int totalPricePayed = totalValue + 20;
                    StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, "http://167.99.246.91/registerorder.php", new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("trimis")) {
                                myOrderData = new ArrayList<>();
                                getOrder(myClientData);
                                helpem = true;
                            }
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(CartApp.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("client", Integer.toString(myClientData.get(0).getId()));
                            params.put("total", Integer.toString(totalPricePayed));
                            params.put("produse", Integer.toString(totalValue));
                            params.put("livrare", "20");
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(CartApp.this);
                    requestQueue.add(stringRequest);
                }else{
                    StyleableToast.makeText(getApplicationContext(), "Cosul este gol!", Toast.LENGTH_LONG, R.style.popUp).show();
                }
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(helpem){
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    for (int i = 0; i < theOrders.size(); i++) {
                                        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, "http://167.99.246.91/registerorderproducts.php", new com.android.volley.Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                if (response.equals("trimis")) {
                                                    if (temp == theOrders.size()) {
                                                        temp = 0;
                                                        totalCan = 0;
                                                        totalValue = 0;
                                                        myQuantity.clear();
                                                        theOrders.clear();
                                                        editor.putInt("totalCan", 0);
                                                        editor.putInt("totalValue", 0);
                                                        Gson gson = new Gson();
                                                        String jsonString = gson.toJson(myQuantity);
                                                        editor.putString("cantitate", jsonString);
                                                        jsonString = gson.toJson(theOrders);
                                                        editor.putString("theOrders", jsonString);
                                                        editor.putBoolean("verify", false);
                                                        editor.commit();
                                                        changeText(totalValue);
                                                        adapter.notifyDataSetChanged();
                                                    }
                                                }
                                            }
                                        }, new com.android.volley.Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(CartApp.this, error.toString(), Toast.LENGTH_LONG).show();
                                            }
                                        }) {
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                Map<String, String> params = new HashMap<String, String>();
                                                params.put("comanda", Integer.toString(myOrderData.get(myOrderData.size() - 1).getId()));
                                                params.put("produs", Integer.toString(theOrders.get(temp).getId()));
                                                System.out.println(theOrders.get(temp).getId());
                                                params.put("cantitate", Integer.toString(myQuantity.get(temp).getCantitate()));
                                                System.out.println(myQuantity.get(temp).getCantitate());
                                                temp++;
                                                return params;
                                            }
                                        };
                                        RequestQueue requestQueue = Volley.newRequestQueue(CartApp.this);
                                        requestQueue.add(stringRequest);
                                    }
                                }
                            }, 400);
                        }
                    }
                }, 1000);
            }
        });

        String priceGr = totalPrice.getText().toString();
        SpannableString tp = new SpannableString(priceGr);

        StyleSpan styleSpanTotal = new StyleSpan(Typeface.BOLD);
        tp.setSpan(styleSpanTotal, 0, totalPrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        RelativeSizeSpan sizeSpanTotal = new RelativeSizeSpan(1.5f);
        tp.setSpan(sizeSpanTotal,0,totalPrice.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        totalPrice.setText(tp);

        String textGr = "Total (incl. TVA)";
        SpannableString ss = new SpannableString(textGr);

        ForegroundColorSpan fGray = new ForegroundColorSpan(Color.GRAY);
        ss.setSpan(fGray, 6,17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        RelativeSizeSpan sizeSpan = new RelativeSizeSpan(0.8f);
        ss.setSpan(sizeSpan, 6,17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        StyleSpan styleSpan1 = new StyleSpan(Typeface.BOLD);
        ss.setSpan(styleSpan1, 6,17,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        RelativeSizeSpan sizeSpan1 = new RelativeSizeSpan(1.5f);
        ss.setSpan(sizeSpan1, 0,5,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
        ss.setSpan(styleSpan, 0,5,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textTva.setText(ss);
        totalCan = sharedPreferences.getInt("totalCan",0);

        recyclerView = findViewById(R.id.cartView);

        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new CustomAdapterCart(getApplicationContext(),theOrders, myQuantity);
        recyclerView.setAdapter(adapter);

        buttonBack = findViewById(R.id.back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlankFragment.adapter.doChange(CustomAdapterCart.counterHtlp);
                onBackPressed();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void getClientId(String email){
        AsyncTask<String, Void, Void> task = new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... strings) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://167.99.246.91/getclient.php?email="+email)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    JSONArray array = new JSONArray(response.body().string());
                    for(int i = 0; i < array.length(); i++){
                        JSONObject object = array.getJSONObject(i);
                        MyClientData data = new MyClientData(object.getInt("id"),object.getString("email"),object.getString("password"),object.getString("nume"),object.getString("telefon"));
                        myClientData.add(data);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e){
                    System.out.println("End of content");
                }
                return null;
            }
        };
        task.execute(email);
    }

    public static void changeText(int value){
        totalPrice.setText(Integer.toString(value)+",00 RON");

        String priceGr = totalPrice.getText().toString();
        SpannableString tp = new SpannableString(priceGr);

        StyleSpan styleSpanTotal = new StyleSpan(Typeface.BOLD);
        tp.setSpan(styleSpanTotal, 0, totalPrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        RelativeSizeSpan sizeSpanTotal = new RelativeSizeSpan(1.5f);
        tp.setSpan(sizeSpanTotal,0,totalPrice.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        totalPrice.setText(tp);
    }

    public void getOrder(final List<MyClientData> myClientData){
        AsyncTask<List<MyClientData>, Void, Void> task = new AsyncTask<List<MyClientData>, Void, Void>() {
            @Override
            protected Void doInBackground(List<MyClientData>... lists) {
                OkHttpClient client = new OkHttpClient();
                try{
                    Request request = new Request.Builder().url("http://167.99.246.91/getorder.php?client_id="+myClientData.get(0).getId())
                            .build();
                    try{
                        Response response = client.newCall(request).execute();
                        JSONArray array = new JSONArray(response.body().string());
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            MyOrderData data = new MyOrderData(object.getInt("id"), object.getInt("client_id"), object.getInt("pret_total"), object.getInt("pret_produse"), object.getInt("pret_livrare"),
                                    object.getString("data_creare"), object.getString("ora_creare"), object.getString("data_livrare"), object.getString("ora_livrare"));
                            myOrderData.add(data);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        System.out.println("End of content");
                    }
                }catch (Exception e){
                    startActivity(new Intent(CartApp.this,Dashboard.class));
                }
                return null;
            }
        };
        task.execute(myClientData);
    }
}