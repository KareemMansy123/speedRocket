package com.speedrocket.progmine.speedrocket.Control;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.speedrocket.progmine.speedrocket.Model.AppConfig;
import com.speedrocket.progmine.speedrocket.Model.BankAccount;
import com.speedrocket.progmine.speedrocket.Model.ResultModel;
import com.speedrocket.progmine.speedrocket.Model.UserApi;
import com.speedrocket.progmine.speedrocket.R;
import com.speedrocket.progmine.speedrocket.View.Activites.MyCashScreen;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;



public class BankAccountsAdapter extends  RecyclerView.Adapter<BankAccountsAdapter.myViewHolder>
{

    Context context ;
    List<BankAccount> bankAccountList ;

    private ProgressDialog progress;
    private Handler handler;
     bankInterface listener ;


    final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);

    public interface  bankInterface{
        public   void checkRadioButton(int id, int itemPosition);

    }
    public  class  myViewHolder extends RecyclerView.ViewHolder
    {
        TextView bankName ;
        Button  yes , no;

        ImageButton deleteBank;
        Dialog warningDialog;
        CheckBox check ;

        public myViewHolder(View itemView) {
            super(itemView);

            bankName = (TextView) itemView.findViewById(R.id.tx_bankName);
            deleteBank = (ImageButton) itemView.findViewById(R.id.bt_deleteBank);
             check = itemView.findViewById(R.id.bankCheck);
            warningDialog = new Dialog(itemView.getContext()); // Context, this, etc.
            warningDialog.setContentView(R.layout.dialog_delete_item);


            yes = (Button) warningDialog.findViewById(R.id.yes);
            no = (Button) warningDialog.findViewById(R.id.no);

        } // constructor
    } // class of myViewHolder


    public  BankAccountsAdapter (Context context , List<BankAccount> bankAccountList , bankInterface listener)

    {
        this.listener = listener ;
        this.context = context;
        this.bankAccountList = bankAccountList;
    } // param constructor

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bank_account, parent, false);
        return new BankAccountsAdapter.myViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final myViewHolder holder, final int position) {

        final BankAccount bankAccount = bankAccountList.get(position);
      if (bankAccount.getChecked() == 1) {
          holder.check.setChecked(true);
      }
        holder.bankName.setText(bankAccount.getName().toString());

        holder.deleteBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                view.startAnimation(buttonClick);

                holder.warningDialog.show();
            }
        }); // delete button

        holder.yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

              yes(view , bankAccount);


            }
        }); // yes button
        holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                listener.checkRadioButton(bankAccount.getId() ,position );
            }
        });

    }

    @Override
    public int getItemCount() {
        return bankAccountList.size();
    }


    private void yes(final View view  , final BankAccount bankAccount){

        progress = new ProgressDialog(view.getContext());
        progress.setTitle("Please Wait");
        progress.setMessage("Loading..");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                progress.dismiss();
                super.handleMessage(msg);
            }

        };
        progress.show();
        new Thread() {
            public void run() {
                //Retrofit

                Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/",context);


                final UserApi userApi = retrofit.create(UserApi.class);

                final Call<ResultModel> deleteBankAccountConnection = userApi.deleteBankAccount(bankAccount.getId());

                deleteBankAccountConnection.enqueue(new Callback<ResultModel>() {
                    @Override
                    public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                        try {
                            Toast.makeText(view.getContext(), "Item Deleted", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(view.getContext(), MyCashScreen.class);
                            view.getContext().startActivity(intent);
                            progress.dismiss();

                        } // try
                        catch (Exception e) {
                            Toast.makeText(view.getContext(), "Connection Faild",
                                    Toast.LENGTH_LONG).show();
                            Log.i("QP", "Error : " + e.toString() + "\n bankAccount" + bankAccount.getId());
                            progress.dismiss();
                        } // catch


                    } // onResponse

                    @Override
                    public void onFailure(Call<ResultModel> call, Throwable t) {
                        progress.dismiss();
                        if (t instanceof IOException){
                            final Dialog   noInternet = AppConfig.InternetFaild(context);
                            final Button btn = noInternet.findViewById(R.id.Retry);
                            noInternet.show();
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    noInternet.cancel();
                                    yes(view , bankAccount);
                                }
                            });
                        }

                    } // on Failure
                });


            }

        }.start();


    }

} // class of BankAccountsAdapter
