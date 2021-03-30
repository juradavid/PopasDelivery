package com.example.popasdelivery;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.muddzdev.styleabletoast.StyleableToast;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private Context context;
    private List<MyData> my_data;
    private TextView valueCartIndicator;
    private static List<MyOrder> order_list;
    private boolean verify;
    private static List<MyOrder> theOrder;
    private static List<MyQuantity> cantitate;
    int cantHelp;
    public int totalCan;
    public CustomAdapter(Context context, List<MyData> my_data, TextView valueCartIndicator, List<MyOrder> order_list) {
        this.context = context;
        this.my_data = my_data;
        this.valueCartIndicator = valueCartIndicator;
        this.order_list = order_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_list_item_layout,parent,false);

        return new ViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        theOrder = new ArrayList<>();
        cantitate = new ArrayList<>();
        SharedPreferences sharedPreferences = context.getSharedPreferences(Register_Login.SHARED_PREFS, context.MODE_PRIVATE);
        holder.pret.setText(my_data.get(position).getPret()+",00 RON");
        holder.nume.setText(my_data.get(position).getNume());
        totalCan = sharedPreferences.getInt("totalCan", 0);
        Glide.with(context).load(my_data.get(position).getImagine()).into(holder.imageView);
        verify = sharedPreferences.getBoolean("verify", false);
        if(verify == true){
            String jsonString = sharedPreferences.getString("theOrders","");
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<MyOrder>>() {}.getType();
            List<MyOrder> list = gson.fromJson(jsonString, type);
            theOrder = list;
            jsonString = sharedPreferences.getString("cantitate","");
            gson = new Gson();
            type = new TypeToken<ArrayList<MyQuantity>>() {}.getType();
            List<MyQuantity> list_int = gson.fromJson(jsonString, type);
            cantitate = list_int;
            if(theOrder.size() != 0){
                valueCartIndicator.setVisibility(View.VISIBLE);
                valueCartIndicator.setText(""+totalCan);
            }
        }
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!theOrder.isEmpty()){
                    int idPosition = my_data.get(position).getId();
                    for(int i = 0; i < theOrder.size(); i++){
                        if(theOrder.get(i).getId() == idPosition){
                            cantHelp = cantitate.get(i).getCantitate();
                            cantHelp++;
                            totalCan++;
                            cantitate.get(i).setCantitate(cantHelp);
                            valueCartIndicator.setVisibility(View.VISIBLE);
                            valueCartIndicator.setText(""+totalCan);
                            Gson gson = new Gson();
                            String jsonString =  gson.toJson(theOrder);
                            SharedPreferences.Editor editor = context.getSharedPreferences(Register_Login.SHARED_PREFS, Context.MODE_PRIVATE).edit();
                            editor.putString("theOrders", jsonString);
                            editor.putBoolean("verify", true);
                            gson = new Gson();
                            jsonString =  gson.toJson(cantitate);
                            editor.putString("cantitate", jsonString);
                            editor.putInt("totalCan", totalCan);
                            editor.commit();
                            break;
                        }
                        if(i == theOrder.size() - 1){
                            cantHelp = 1;
                            totalCan++;
                            MyOrder order = new MyOrder(my_data.get(position).getId(),my_data.get(position).getNume(),my_data.get(position).getPret(),my_data.get(position).getImagine());
                            theOrder.add(order);
                            MyQuantity quantity = new MyQuantity(my_data.get(position).getId(), cantHelp);
                            cantitate.add(quantity);
                            valueCartIndicator.setVisibility(View.VISIBLE);
                            valueCartIndicator.setText(""+totalCan);
                            Gson gson = new Gson();
                            String jsonString =  gson.toJson(theOrder);
                            SharedPreferences.Editor editor = context.getSharedPreferences(Register_Login.SHARED_PREFS, Context.MODE_PRIVATE).edit();
                            editor.putString("theOrders", jsonString);
                            editor.putBoolean("verify", true);
                            gson = new Gson();
                            jsonString =  gson.toJson(cantitate);
                            editor.putString("cantitate", jsonString);
                            editor.putInt("totalCan", totalCan);
                            editor.commit();
                            break;
                        }
                    }
                }else{
                    totalCan++;
                    MyOrder order = new MyOrder(my_data.get(position).getId(),my_data.get(position).getNume(),my_data.get(position).getPret(),my_data.get(position).getImagine());
                    theOrder.add(order);
                    cantHelp = 1;
                    MyQuantity quantity = new MyQuantity(my_data.get(position).getId(), cantHelp);
                    cantitate.add(quantity);
                    valueCartIndicator.setVisibility(View.VISIBLE);
                    valueCartIndicator.setText(""+totalCan);
                    Gson gson = new Gson();
                    String jsonString =  gson.toJson(theOrder);
                    SharedPreferences.Editor editor = context.getSharedPreferences(Register_Login.SHARED_PREFS, Context.MODE_PRIVATE).edit();
                    editor.putString("theOrders", jsonString);
                    editor.putBoolean("verify", true);
                    gson = new Gson();
                    jsonString =  gson.toJson(cantitate);
                    editor.putString("cantitate", jsonString);
                    editor.putInt("totalCan", totalCan);
                    editor.commit();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public TextView pret,nume;
        public ImageButton plus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nume = itemView.findViewById(R.id.product_title);
            pret = itemView.findViewById(R.id.product_price);
            imageView = itemView.findViewById(R.id.product_image);
            plus = itemView.findViewById(R.id.imageView10);
        }
    }

    public void doChange(List<MyQuantity> quantity){
        this.notifyDataSetChanged();
        try {
            if (quantity.isEmpty()) {
                valueCartIndicator.setVisibility(View.INVISIBLE);
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }

}
