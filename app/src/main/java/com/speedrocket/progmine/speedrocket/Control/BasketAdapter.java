package com.speedrocket.progmine.speedrocket.Control;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
 * Created by Ibrahim on 7/31/2018.
 */

public class BasketAdapter extends  RecyclerView.Adapter<BasketAdapter.myViewHolder> {
     private Context context ;
     private List<BasketItem> basket ;
     private  onClickListener listener ;


     public BasketAdapter(Context context , List<BasketItem> Basket , onClickListener listener){
         this.context = context ;
         this.basket  = Basket;
         this.listener = listener ;

         if (basket.isEmpty()){
             Log.e("appUtils" , "adabter basket Empty") ;

         }
     }

    public interface  onClickListener{
     public void onClick(int position);
     public void onSpinnerItemSelected(int position, int selectedNum);
    }

    public class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView productName,
                 productDescription,
                 companyName,
                 productPrice,
                 restDayes,
                 wordDays ;


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
            wordDays = itemView.findViewById(R.id.wordDays);

            countDayesLayout = itemView.findViewById(R.id.counterDaysLayout);

            Remove  = itemView.findViewById(R.id.remove);
            Remove.setOnClickListener(this);

            number = itemView.findViewById(R.id.number);

            number.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    listener.onSpinnerItemSelected(getAdapterPosition() , Integer.valueOf(number.getSelectedItem().toString()));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });



            productImage= itemView.findViewById(R.id.productimage);

        }

        @Override
        public void onClick(View view) {
            listener.onClick(getAdapterPosition());
        }


    }
    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

         View ItemView = LayoutInflater.from(parent.getContext()).
                 inflate(R.layout.item_basket , parent ,false);
        return new BasketAdapter.myViewHolder(ItemView);
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
         final BasketItem Item = basket.get(position);
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




        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                context, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.number.setAdapter(adapter);


        if (Item.getType() == 1){
            Picasso.with(context).load("https://speed-rocket.com/upload/offers/"+Item.getImage())
                    .fit().centerCrop().into(holder.productImage);
            holder.countDayesLayout.setVisibility(View.VISIBLE);
            holder.restDayes.setText(""+Item.getDays_remain());
            int rest_Days = 7 - Item.getDays_remain();
            Boolean b = rest_Days < 0 ;
            Log.e("restDayes##" , "offerDayes  : "+Item.getDays_remain());
            Log.e("restDayes##" , "restDayes  : "+rest_Days);
            Log.e("restDayes##" , "expeired or not   : "+b);
            Log.e("restDayes##" , "*********************************************");

            if(rest_Days == 0)
            {
                holder.restDayes.setText(R.string.winner_latest);
                holder.wordDays.setText(R.string.winner_day);
                holder.restDayes.setTextColor(Color.parseColor("#ffd700"));
                holder.wordDays.setTextColor(Color.parseColor("#ffd700"));
            }
            else if (rest_Days < 0 )
            {


                holder.restDayes.setVisibility(View.INVISIBLE);
                holder.wordDays.setText(R.string.winner_expired);
                holder.wordDays.setTextColor(Color.parseColor("#ff0000"));
            }
            else if (rest_Days > 0)
            {
                holder.restDayes.setVisibility(View.VISIBLE);
                holder.wordDays.setText(R.string.days);
                holder.restDayes.setText(rest_Days+"");
                holder.restDayes.setTextColor(Color.parseColor("#000000"));
                holder.wordDays.setTextColor(Color.parseColor("#000000"));
            }
            int number = Item.getNumber()== 0 ? 1:Item.getNumber() ;
            Log.e("number##" , ""+number);

                adapter.add(""+1);
                adapter.notifyDataSetChanged();

        }else{
            Picasso.with(context).load("https://speed-rocket.com/upload/products/"+Item.getImage())
                    .fit().centerCrop().into(holder.productImage);
            for (int i =1 ; i<=Item.getItem_qty() ; i++){
                adapter.add(""+i);
                adapter.notifyDataSetChanged();
            }
        }




    }

    @Override
    public int getItemCount() {
        return basket.size();
    }


}
