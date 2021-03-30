package com.example.popasdelivery;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
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
import com.muddzdev.styleabletoast.StyleableToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CustomAdapterOrder extends RecyclerView.Adapter<CustomAdapterOrder.ViewHolder> {

    private Context context;
    public List<MyOrderData> myClientOrder;
    public List<MyData> myData;
    public List<MyOrderDetails> myOrderDetails;
    public List<String> helper;

    public CustomAdapterOrder(Context context, List<MyOrderData> myClientOrder,List<MyData> myData, List<MyOrderDetails> myOrderDetails) {
        this.context = context;
        this.myClientOrder = myClientOrder;
        this.myData = myData;
        this.myOrderDetails = myOrderDetails;
    }

    @NonNull
    @Override
    public CustomAdapterOrder.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_list_layout,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapterOrder.ViewHolder holder, int position) {
        try {
            System.out.println(myOrderDetails.get(position).getCantitate());
        }catch(Exception e){
            System.out.println(e);
        }
        helper = new ArrayList<>();
        holder.order_title.setText("La Pod Popas Utvin\nComanda de pe "+myClientOrder.get(position).getDataCreare() + " la\n" + myClientOrder.get(position).getOraCreare());
        holder.totalPrice.setText(myClientOrder.get(position).getPretTotal() + ",00 RON");
        try{
            if(myOrderDetails.get(position).getProdusId().size() > 3) {
                for (int i = 0; i < myOrderDetails.get(position).getProdusId().size(); i++) {
                    for (int j = 0; j < myData.size(); j++) {
                        if (myData.get(j).getId() == myOrderDetails.get(position).getProdusId().get(i)) {
                            helper.add(myData.get(j).getNume());
                            break;
                        }
                    }
                }
                holder.product1.setText(myOrderDetails.get(position).getCantitate().get(0) + " x " + helper.get(0));
                holder.product2.setText(myOrderDetails.get(position).getCantitate().get(1) + " x " + helper.get(1));
                holder.product3.setText(myOrderDetails.get(position).getCantitate().get(2) + " x " + helper.get(2));
            }else{
                for (int i = 0; i < myOrderDetails.get(position).getProdusId().size(); i++) {
                    for (int j = 0; j < myData.size(); j++) {
                        if (myData.get(j).getId() == myOrderDetails.get(position).getProdusId().get(i)) {
                            helper.add(myData.get(j).getNume());
                            break;
                        }
                    }
                }
                if(myOrderDetails.get(position).getProdusId().size() == 1){
                    holder.product1.setText(myOrderDetails.get(position).getCantitate().get(0) + " x " + helper.get(0));
                    holder.product2.setText("");
                    holder.product2.setTextSize(0);
                    holder.product3.setText("");
                    holder.product3.setTextSize(0);
                    holder.product4.setText("");
                    holder.product4.setTextSize(0);
                }
                if(myOrderDetails.get(position).getProdusId().size() == 2){
                    holder.product1.setText(myOrderDetails.get(position).getCantitate().get(0) + " x " + helper.get(0));
                    holder.product2.setText(myOrderDetails.get(position).getCantitate().get(1) + " x " + helper.get(1));
                    holder.product3.setText("");
                    holder.product3.setTextSize(0);
                    holder.product4.setText("");
                    holder.product4.setTextSize(0);
                }
                if(myOrderDetails.get(position).getProdusId().size() == 3){
                    holder.product1.setText(myOrderDetails.get(position).getCantitate().get(0) + " x " + helper.get(0));
                    holder.product2.setText(myOrderDetails.get(position).getCantitate().get(1) + " x " + helper.get(1));
                    holder.product3.setText(myOrderDetails.get(position).getCantitate().get(2) + " x " + helper.get(2));
                    holder.product4.setText("");
                    holder.product4.setTextSize(0);
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }

    @Override
    public int getItemCount() {
        return myClientOrder.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView order_title;
        public TextView product1,product2,product3,product4;
        public TextView totalPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            order_title = itemView.findViewById(R.id.order_title);
            product1 = itemView.findViewById(R.id.product1);
            product2 = itemView.findViewById(R.id.product2);
            product3 = itemView.findViewById(R.id.product3);
            product4 = itemView.findViewById(R.id.product4);
            totalPrice = itemView.findViewById(R.id.totalprice);
        }
    }
}
