package com.example.popasdelivery;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.muddzdev.styleabletoast.StyleableToast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapterCart extends RecyclerView.Adapter<CustomAdapterCart.ViewHolder> {

    public Context context;
    public List<MyOrder> myHelp;
    public static List<MyQuantity> counterHtlp;
    int totalCan;
    int totalValue;

    public CustomAdapterCart(Context context, List<MyOrder> myHelp, List<MyQuantity> counterHtlp) {
        this.context = context;
        this.myHelp = myHelp;
        this.counterHtlp = counterHtlp;
    }

    @NonNull
    @Override
    public CustomAdapterCart.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_layout,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapterCart.ViewHolder holder, int position) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Register_Login.SHARED_PREFS, Context.MODE_PRIVATE);
        totalValue = sharedPreferences.getInt("totalValue",0);
        totalCan =  sharedPreferences.getInt("totalCan",0);
        holder.produs_nume.setText(myHelp.get(position).getNume());
        holder.cantitate.setText(""+counterHtlp.get(position).getCantitate());
        int price = Integer.parseInt(myHelp.get(position).getPret()) * counterHtlp.get(position).getCantitate();
        holder.pret.setText(price + ",00 RON");
        Glide.with(context).load(myHelp.get(position).getImagine()).into(holder.image);
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalCan++;
                totalValue = totalValue + Integer.parseInt(myHelp.get(position).getPret());
                CartApp.changeText(totalValue);
                counterHtlp.get(position).setCantitate(counterHtlp.get(position).getCantitate() + 1);
                holder.cantitate.setText(""+counterHtlp.get(position).getCantitate());
                int price = Integer.parseInt(myHelp.get(position).getPret()) * counterHtlp.get(position).getCantitate();
                holder.pret.setText(price + ",00 RON");
                Gson gson = new Gson();
                String jsonString =  gson.toJson(counterHtlp);
                SharedPreferences.Editor editor = context.getSharedPreferences(Register_Login.SHARED_PREFS, Context.MODE_PRIVATE).edit();
                editor.putString("cantitate", jsonString);
                editor.putInt("totalCan", totalCan);
                editor.putInt("totalValue", totalValue);
                editor.commit();
            }
        });
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalCan--;
                totalValue = totalValue - Integer.parseInt(myHelp.get(position).getPret());
                CartApp.changeText(totalValue);
                counterHtlp.get(position).setCantitate(counterHtlp.get(position).getCantitate() - 1);
                holder.cantitate.setText(""+counterHtlp.get(position).getCantitate());
                int price = Integer.parseInt(myHelp.get(position).getPret()) * counterHtlp.get(position).getCantitate();
                holder.pret.setText(price + ",00 RON");
                Gson gson = new Gson();
                String jsonString =  gson.toJson(counterHtlp);
                SharedPreferences.Editor editor = context.getSharedPreferences(Register_Login.SHARED_PREFS, Context.MODE_PRIVATE).edit();
                editor.putString("cantitate", jsonString);
                editor.putInt("totalCan", totalCan);
                editor.putInt("totalValue", totalValue);
                editor.commit();
                System.out.println(counterHtlp);
                if(counterHtlp.get(position).getCantitate() == 0){
                    removeAt(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        try{
            return myHelp.size();
        }catch(Exception e){
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView produs_nume,cantitate,pret;
        ImageView image;
        ImageButton add,remove;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            add = itemView.findViewById(R.id.add);
            remove = itemView.findViewById(R.id.remove);
            produs_nume = itemView.findViewById(R.id.produs_nume);
            cantitate = itemView.findViewById(R.id.cantitate);
            pret = itemView.findViewById(R.id.totalprice);
            image = itemView.findViewById(R.id.image);
        }
    }

    private void removeAt(int position){
        myHelp.remove(position);
        counterHtlp.remove(position);
        Gson gson = new Gson();
        String jsonString =  gson.toJson(counterHtlp);
        SharedPreferences.Editor editor = context.getSharedPreferences(Register_Login.SHARED_PREFS, Context.MODE_PRIVATE).edit();
        editor.putString("cantitate", jsonString);
        gson = new Gson();
        jsonString = gson.toJson(myHelp);
        editor.putString("theOrders", jsonString);
        editor.putInt("totalCan", totalCan);
        editor.commit();
        notifyDataSetChanged();
    }
}
