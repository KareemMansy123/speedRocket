package com.speedrocket.progmine.speedrocket.Control;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.speedrocket.progmine.speedrocket.Model.BasketItem;
import com.speedrocket.progmine.speedrocket.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by progmine on 8/19/2018.
 */

public class YourOrdersAdapter extends  RecyclerView.Adapter<YourOrdersAdapter.myViewHolder> {

    private Context context ;
    private List<BasketItem> orders ;
    private BasketAdapter.onClickListener listener ;


    public YourOrdersAdapter(Context context , List<BasketItem> orders ){
        this.context = context ;
        this.orders  = orders;

    }



    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView productName,
                productDescription,
                companyName,
                productPrice,
                restDayes,
                wordDayes ;

        Button Remove ;

        Spinner number ;

        ImageView productImage ;

        LinearLayout countDayesLayout ;

        public myViewHolder(View itemView) {
            super(itemView);
            productName   = itemView.findViewById(R.id.productname);
            productDescription  = itemView.findViewById(R.id.productDescription);
            companyName    = itemView.findViewById(R.id.companNameProduct);
            productPrice   = itemView.findViewById(R.id.priceProduct);
            restDayes   = itemView.findViewById(R.id.restDays);
            wordDayes = itemView.findViewById(R.id.wordDays);
            countDayesLayout = itemView.findViewById(R.id.counterDaysLayout);
            productImage= itemView.findViewById(R.id.productimage);

        }

    }

    @Override
    public YourOrdersAdapter.myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View ItemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_order , parent ,false);
        return new YourOrdersAdapter.myViewHolder(ItemView);
    }

    @Override
    public void onBindViewHolder(YourOrdersAdapter.myViewHolder holder, int position) {
        final BasketItem Item = orders.get(position);
        if (context.getSharedPreferences("MyPref", 0).getString("langa" ,"en").equalsIgnoreCase("ar")){
            holder. productName.setText(Item.getAr_title());
            holder.productDescription.setText(Item.getDescription_ar());
            holder.companyName.setText(Item.getCompany_name_ar());
        }else{
            holder. productName.setText(Item.getEn_title());
            holder.productDescription.setText(Item.getDescription_en());
            holder.companyName.setText(Item.getCompany_name());
        }
//        Log.e("appUtils" ,Item.getDescription_en());
        //  Log.e("appUtils" ,Item.getDescription_ar());
        holder. productPrice.setText(Item.getPrice());



        if (Item.getType() == 1){
            Picasso.with(context).load("https://speed-rocket.com/upload/offers/"+Item.getImage())
                    .fit().centerCrop().into(holder.productImage);
            holder.countDayesLayout.setVisibility(View.VISIBLE);
            holder.restDayes.setText(""+Item.getDays_remain());
            if (Item.getDays_remain() == 0){

                holder.restDayes.setText(R.string.expiry_date);
            }


        }else{
            Picasso.with(context).load("https://speed-rocket.com/upload/products/"+Item.getImage())
                    .fit().centerCrop().into(holder.productImage);
        }
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }
}
