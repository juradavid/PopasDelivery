package com.example.popasdelivery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Comenzi#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Comenzi extends Fragment {

    public RecyclerView recyclerView;
    public GridLayoutManager gridLayoutManager;
    public CustomAdapterOrder adapter;
    public List<MyClientData> myClientData;
    public List<MyOrderData> myOrderData;
    public List<MyData> myData;
    public List<MyProductsOrder> myProductsOrders;
    public String email;
    public List<MyOrderDetails> myOrderDetails;
    public int client_id;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Comenzi() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Comenzi.
     */
    // TODO: Rename and change types and number of parameters
    public static Comenzi newInstance(String param1, String param2) {
        Comenzi fragment = new Comenzi();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Register_Login.SHARED_PREFS, MODE_PRIVATE);
        View v = inflater.inflate(R.layout.fragment_comenzi, container, false);
        myClientData = new ArrayList<>();
        myOrderData = new ArrayList<>();
        myOrderDetails = new ArrayList<>();
        myProductsOrders = new ArrayList<>();
        myData = new ArrayList<>();
        email = sharedPreferences.getString("email","");
        getClientId(email);
        getOrder(myClientData);
        getProduts(0);
        getProdutsOrder(0);
        recyclerView = v.findViewById(R.id.comenziRW);
        gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new CustomAdapterOrder(getContext(), myOrderData,myData,myOrderDetails);
        LayoutAnimationController lac = new LayoutAnimationController(AnimationUtils.loadAnimation(recyclerView.getContext(),R.anim.fade_in_recycler));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutAnimation(lac);
            }
        },500);
        return v;
    }

    public void sortMyOrderDetails(List<MyProductsOrder> myProductsOrders){
        int lastId = myProductsOrders.get(0).getComandaId();
        List<Integer> produse = new ArrayList<>();
        List<Integer> cantitate = new ArrayList<>();
        int x = 0;
        for(int i = 0; i < myProductsOrders.size(); i++){
            if(myProductsOrders.get(i).getComandaId() == lastId){
                produse.add(myProductsOrders.get(i).getProdusId());
                cantitate.add(myProductsOrders.get(i).getCantitate());
                System.out.println("mersi");
            }
            if(myProductsOrders.get(i).getComandaId() != lastId){
                MyOrderDetails data = new MyOrderDetails(lastId, produse, cantitate);
                myOrderDetails.add(data);
                lastId = myProductsOrders.get(i).getComandaId();
                x = 1;
            }
            if(i == myProductsOrders.size() - 1){
                MyOrderDetails data = new MyOrderDetails(lastId, produse, cantitate);
                myOrderDetails.add(data);
            }
            if(x == 1){
                x = 0;
                produse = new ArrayList<>();
                cantitate = new ArrayList<>();
                produse.add(myProductsOrders.get(i).getProdusId());
                cantitate.add(myProductsOrders.get(i).getCantitate());
            }
        }
    }

    public void getProdutsOrder(final int comanda_id){
        AsyncTask<Integer, Void, Void> task = new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... integers) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://167.99.246.91/getproductsorder.php?comanda_id="+comanda_id)
                        .build();
                try {
                    Response response = client.newCall(request).execute();

                    JSONArray array = new JSONArray(response.body().string());

                    for(int i = 0; i < array.length(); i++){
                        JSONObject object = array.getJSONObject(i);

                        MyProductsOrder data = new MyProductsOrder(object.getInt("id"),object.getInt("comanda_id"),object.getInt("produs_id"), object.getInt("cantitate"), object.getString("data_creata"), object.getString("ora_creata"));
                        myProductsOrders.add(data);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e){
                    System.out.println("End of content");
                }
                try {
                    sortMyOrderDetails(myProductsOrders);
                }catch (Exception e){
                    System.out.println(e);
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                adapter.notifyDataSetChanged();
            }
        };
        task.execute(comanda_id);
    }

    public void getProduts(final int id){
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
                            myData.add(data);
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
            @Override
            protected void onPostExecute(Void aVoid) {
                adapter.notifyDataSetChanged();
            }
        };
        task.execute(email);
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
                    startActivity(new Intent(getContext(),Dashboard.class));
                }
                        return null;
                    }
            @Override
            protected void onPostExecute(Void aVoid) {
                adapter.notifyDataSetChanged();
            }
        };
        task.execute(myClientData);
    }
}