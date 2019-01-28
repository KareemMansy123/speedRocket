package com.speedrocket.progmine.speedrocket.Control;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.speedrocket.progmine.speedrocket.Model.CompanyOrder;
import com.speedrocket.progmine.speedrocket.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Ibrahim on 8/28/2018.
 */

public class CompanyOrderAdapter  extends  RecyclerView.Adapter<CompanyOrderAdapter.myViewHolder> {

    Context context ;
    List<CompanyOrder> orders ;


    public CompanyOrderAdapter(Context context , List<CompanyOrder> orders ){
        this.context = context ;
        this.orders  = orders;

    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView productName,
                productDescription,
                companyName,
                productPrice,
                date,
                qty ;

        ImageView productImage ;



        public myViewHolder(View itemView) {
            super(itemView);
            productName   = itemView.findViewById(R.id.productname);
            productDescription  = itemView.findViewById(R.id.productDescription);
            companyName    = itemView.findViewById(R.id.companNameProduct);
            productPrice   = itemView.findViewById(R.id.priceProduct);
            date   = itemView.findViewById(R.id.date);
            productImage= itemView.findViewById(R.id.productimage);
            qty = itemView.findViewById(R.id.count);

        }

    }


    @Override
    public CompanyOrderAdapter.myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_company_order, parent, false);
      /*  Toast.makeText(itemView.getContext(), "hi", Toast.LENGTH_SHORT).show();*/

        return new CompanyOrderAdapter.myViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
        final CompanyOrder Order = orders.get(position) ;

        holder.qty.setText(""+Order.getCount_items());
        if (context.getSharedPreferences("MyPref",0).getString("langa","").equalsIgnoreCase("ar")){
            holder.productName.setText(Order.getProdTitleAr());
            holder.productDescription.setText(Order.getProdTitleAr());
            holder.date.setText(Order.getCreated_at());
        }else{
            holder.productName.setText(Order.getProdTitleEn());
            holder.productDescription.setText(Order.getProdTitleEn());
            holder.date.setText(Order.getCreated_at());
        }

        holder.productPrice.setText(""+Order.getPrice());


        if (Order.getDetecate() == 0 ){

            Picasso.with(context).load("https://speed-rocket.com/upload/products/"
                    +Order.getImage()).
                    fit().centerCrop().into(holder.productImage);
        }else{

            Picasso.with(context).load("https://speed-rocket.com/upload/offers/"
                    +Order.getImage()).
                    fit().centerCrop().into(holder.productImage);
        }
    }


    @Override
    public int getItemCount() {
        return orders.size();
    }
}
