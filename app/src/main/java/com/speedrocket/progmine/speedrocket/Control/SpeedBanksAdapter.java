package com.speedrocket.progmine.speedrocket.Control;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.speedrocket.progmine.speedrocket.Model.SpeedRocketBanks;
import com.speedrocket.progmine.speedrocket.R;

import java.util.List;

/**
 * Created by Ibrahim on 9/19/2018.
 */

public class SpeedBanksAdapter  extends RecyclerView.Adapter<SpeedBanksAdapter.MyViewHolder> {


     Context context ;
    List<SpeedRocketBanks> banks ;

    public  SpeedBanksAdapter(List<SpeedRocketBanks> banks , Context context)
    {

        this.context = context;
        this.banks = banks ;

    } // constructor



    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView bankNumber  , accountName , bankName  ;


        public MyViewHolder(View itemView) {
            super(itemView);

           bankName = itemView.findViewById(R.id.accountName);
           bankNumber = itemView.findViewById(R.id.accountNumber);
           accountName = itemView.findViewById(R.id.bankName);
        } // constructor
    } // class myholder


    @Override
    public SpeedBanksAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.speed_bank_item, parent, false);
      /*  Toast.makeText(itemView.getContext(), "hi", Toast.LENGTH_SHORT).show();*/

        return new SpeedBanksAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
         SpeedRocketBanks bank = banks.get(position);

         holder.accountName.setText(bank.getAccountName()) ;
         holder.bankNumber.setText(bank.getBankAccount());
         holder.bankName.setText(bank.getName());



    }


    @Override
    public int getItemCount() {
        return banks.size();
    }

    }


