package com.speedrocket.progmine.speedrocket.Control;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nex3z.notificationbadge.NotificationBadge;
import com.speedrocket.progmine.speedrocket.Model.AppConfig;
import com.speedrocket.progmine.speedrocket.Model.Company;
import com.speedrocket.progmine.speedrocket.Model.ResultModel;
import com.speedrocket.progmine.speedrocket.Model.UserApi;
import com.speedrocket.progmine.speedrocket.R;
import com.speedrocket.progmine.speedrocket.View.Activites.AddCompany;
import com.speedrocket.progmine.speedrocket.View.Activites.CompanyScreen;
import com.speedrocket.progmine.speedrocket.View.Activites.ConfirmCompany;
import com.speedrocket.progmine.speedrocket.View.Activites.MessageActivity;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class CompanyAdapter  extends RecyclerView.Adapter<CompanyAdapter.MyViewHolder> {


    private List<Company> companyList;
    Context context;


    private ProgressDialog progress;
    private Handler handler;


    final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);


    public class MyViewHolder extends RecyclerView.ViewHolder {

        Button edit , delete , yes , no , messageBtn;
        TextView companyName , confirm  ;
        CircleImageView companyLogo ;
        Dialog warningDialog;
        NotificationBadge badge ;
        RelativeLayout message ;

        public MyViewHolder(View itemView) {
            super(itemView);

            edit = (Button) itemView.findViewById(R.id.item_edit);
            delete = (Button) itemView.findViewById(R.id.item_delete);
            companyName = (TextView) itemView.findViewById(R.id.item_companyName);
            companyLogo = (CircleImageView) itemView.findViewById(R.id.item_companyLogo);

            warningDialog = new Dialog(itemView.getContext()); // Context, this, etc.
            warningDialog.setContentView(R.layout.dialog_delete_item);
            confirm = itemView . findViewById(R.id.confirmed) ;
            yes = (Button) warningDialog.findViewById(R.id.yes);
            no = (Button) warningDialog.findViewById(R.id.no);
            messageBtn = itemView.findViewById(R.id.message_btn);

            badge = itemView.findViewById(R.id.badge);
            message = itemView.findViewById(R.id.messages);
        } // constructor
    } // class myholder


    public  CompanyAdapter(List<Company> companyList , Context context)
    {

        this.context = context;
        this.companyList = companyList ;

    } // constructor

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.company_item, parent, false);
      /*  Toast.makeText(itemView.getContext(), "hi", Toast.LENGTH_SHORT).show();*/

        return new CompanyAdapter.MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final Company company = companyList.get(position);

        holder.badge.setNumber(company.getMessageNumber());

    if (context.getSharedPreferences("MyPref",0).getString("langa","").equalsIgnoreCase("ar")){

        holder.companyName.setText(company.getAr_name());

    }else{

        holder.companyName.setText(company.getEn_name());
    }

        Log.i("QP",company.getLogo());

        Picasso.with(context).load("https://speed-rocket.com/upload/logo/"
                +company.getLogo()).
                fit().centerCrop().into(holder.companyLogo);


        holder.message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("companyId" , company.getId());
                view.getContext().startActivity(intent);
            }
        });

        holder.badge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("companyId" , company.getId());
                view.getContext().startActivity(intent);
            }
        });

        holder.messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("companyId" , company.getId());
                view.getContext().startActivity(intent);
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                Intent intent = new Intent(view.getContext(),AddCompany.class);
                intent.putExtra("companyId",company.getId());
                view.getContext().startActivity(intent);

            }
        }); // edit button


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                holder.warningDialog.show();
            }
        }); // delete button

        holder.yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

          yes(view , company);


            }
        }); // yes button

        holder.no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.warningDialog.cancel();
            }
        });// no button

          if (company.getConfirmed() == 1){
              holder.confirm.setText(R.string.confirmed);
              holder.confirm.setTextColor(Color.parseColor("#29c40e"));
          }else {

              holder.confirm.setText(R.string.unconfirmed);
              holder.confirm.setTextColor(Color.parseColor("#bf0c0f"));
              holder.confirm.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      Intent intent = new Intent(view.getContext() , ConfirmCompany.class);
                      intent.putExtra("companyId" , company.getId());
                      view.getContext().startActivity(intent);
                  }
              });
          }


    } // onBindHolder




    @Override
    public int getItemCount() {
        return companyList.size();
    }

  private void yes ( final View view , final Company company){
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

              final Call<ResultModel>  deleteCompanyConnection= userApi.deleteCompany(company.getId());

              deleteCompanyConnection.enqueue(new Callback<ResultModel>() {
                  @Override
                  public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                      try
                      {
                          Log.i("QP","companyId"+company.getId());
                          Toast.makeText(view.getContext(),"Item Deleted",Toast.LENGTH_LONG).show();
                          progress.dismiss();
                          Intent intent = new Intent(view.getContext(), CompanyScreen.class);
                          view.getContext().startActivity(intent);
                      } // try
                      catch (Exception e)
                      {
                          Toast.makeText(view.getContext(),"Connection Faild",
                                  Toast.LENGTH_LONG).show();
                          Log.i("QP","Error : "+e.toString()+"\n companyId"+company.getId());
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
                                  yes(view , company);
                              }
                          });
                      }

                  } // on Failure
              });


          }

      }.start();

  }
}
