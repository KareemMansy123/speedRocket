package com.speedrocket.progmine.speedrocket.Control;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.speedrocket.progmine.speedrocket.Model.AppConfig;
import com.speedrocket.progmine.speedrocket.Model.CompanyMessage;
import com.speedrocket.progmine.speedrocket.Model.ResultModel;
import com.speedrocket.progmine.speedrocket.Model.UserApi;
import com.speedrocket.progmine.speedrocket.R;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Ibrahim on 9/10/2018.
 */

public class MessageAdabter  extends RecyclerView.Adapter<MessageAdabter.MyViewHolder> {



    List<CompanyMessage> messages ;
    Context context ;

    public  MessageAdabter(List<CompanyMessage> messages , Context context)
    {

        this.context = context;
        this.messages = messages ;

    } // constructor



    @Override
    public MessageAdabter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
      /*  Toast.makeText(itemView.getContext(), "hi", Toast.LENGTH_SHORT).show();*/

        return new MessageAdabter.MyViewHolder(itemView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView name , title , since   , dialog_Title , dialog_message , dialog_from;
        ImageView notify ;
        Button dialog_cancel ;
        Dialog dialog2 ;

        public MyViewHolder(View itemView) {
            super(itemView);


             dialog2 = new Dialog(context); // Context, this, etc.
            dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog2.setContentView(R.layout.message_details);
            dialog2.setCancelable(false);
            dialog2.setCanceledOnTouchOutside(false);

            dialog_from= dialog2.findViewById(R.id.dialog_from);
            dialog_message = dialog2.findViewById(R.id.dialog_message);
            dialog_Title = dialog2.findViewById(R.id.dialog_title);
            dialog_cancel = dialog2.findViewById(R.id.cancel);

            dialog_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog2.cancel();
                }
            });

           name = itemView.findViewById(R.id.from);
           title = itemView.findViewById(R.id.title);
           since = itemView.findViewById(R.id.since);
           notify = itemView.findViewById(R.id.notify);

        }


    }



    @Override
    public void onBindViewHolder(final MessageAdabter.MyViewHolder holder, int position) {
        final CompanyMessage message = messages.get(position);

     holder.itemView.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             holder.dialog2.show();
             setSeen(message.getId());
             holder.notify.setVisibility(View.INVISIBLE);
         }
     });
        holder.name.setText(message.getName());
        holder.title.setText(message.getTitle());
        holder.since.setText(message.getSince());

        if (message.getSeen() == 0)holder.notify.setVisibility(View.VISIBLE);
        else holder.notify.setVisibility(View.INVISIBLE);

      holder.dialog_from.setText(message.getName());
       holder. dialog_message.append(message.getMessage());
      holder.  dialog_Title.setText(message.getTitle());

    }


    public void setSeen(final int id){
        new Thread(){

            @Override
            public void run() {
                Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/",context);

                UserApi api = retrofit.create(UserApi.class);
                final Call<ResultModel> setSeen = api.setSeen(id);

                setSeen.enqueue(new Callback<ResultModel>() {
                    @Override
                    public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {

                    }

                    @Override
                    public void onFailure(Call<ResultModel> call, Throwable t) {
                        if (t instanceof IOException){
                            final Dialog   noInternet = AppConfig.InternetFaild(context);
                            final Button btn = noInternet.findViewById(R.id.Retry);
                            noInternet.show();
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    noInternet.cancel();
                                    setSeen(id);
                                }
                            });
                        }
                    }
                });

            }
        }.start();

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
