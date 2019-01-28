package com.speedrocket.progmine.speedrocket.View.Activites;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.speedrocket.progmine.speedrocket.Control.MessageAdabter;
import com.speedrocket.progmine.speedrocket.Model.AppConfig;
import com.speedrocket.progmine.speedrocket.Model.CompanyMessage;
import com.speedrocket.progmine.speedrocket.Model.ResultModel;
import com.speedrocket.progmine.speedrocket.Model.UserApi;
import com.speedrocket.progmine.speedrocket.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.speedrocket.progmine.speedrocket.View.Activites.LoginScreen.MY_PREFS_NAME;

/**
 * Created by Ibrahim on 9/10/2018.
 */

public class MessageActivity  extends AppCompatActivity {

    String firstName = "" , lastName ="" , email ="" , userProfileImage;
      List<CompanyMessage> fakeone = new ArrayList<>();
      List<CompanyMessage> messages = new ArrayList<>();
    List<CompanyMessage> messagesfilter = new ArrayList<>();
      CompanyMessage Message ;
      int userID =0 , companyId =0 ;

      MessageAdabter mAdapter ;
    RecyclerView recyclerView ;
    int seen =0 ;
    String title ="" , message="" ,name ="" ,since = "" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_layout);
        setTitle(getString(R.string.company_message));

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        firstName = prefs.getString("firstName", "");//"No name defined" is the default value.
        lastName = prefs.getString("lastName", "");//"No name defined" is the default value.
        email = prefs.getString("email", "");//"No name defined" is the default value.
        userID = prefs.getInt("id", 0);
        userProfileImage = prefs.getString("profileImage", "");


        Bundle b = getIntent().getExtras() ;
        if (b != null ){
            if (b.containsKey("companyId"))companyId=(int)b.get("companyId");
        }


        recyclerView = findViewById(R.id.message_list);
        mAdapter = new MessageAdabter(messages ,MessageActivity.this );
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        getmessages();
    }

  private void getmessages() {


      final ProgressDialog progress = new ProgressDialog(this);
      progress.setTitle("Please Wait");
      progress.setMessage("Loading..");
      progress.setCancelable(false);
      progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
      final Handler handler;

      if (!((Activity) this).isFinishing()) {
          //show dialog
          progress.show();
      }

      handler = new Handler() {
          @Override
          public void handleMessage(Message msg) {
              super.handleMessage(msg);

          }
      };
      new Thread() {
          public void run() {
              Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/", getApplicationContext());
              UserApi api = retrofit.create(UserApi.class);
              final Call<ResultModel>Messages = api.getCompanyMessages(companyId);
              Messages.enqueue(new Callback<ResultModel>() {
                  @Override
                  public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                        fakeone = response.body().getCompanyMessages() ;
                        for (int i =0 ; i < fakeone.size() ; i++){
                            since = fakeone.get(i).getSince();
                            name = fakeone.get(i).getName();
                            message = fakeone.get(i).getMessage();
                            title = fakeone.get(i).getTitle();
                            seen = fakeone.get(i).getSeen();
                            int id = fakeone.get(i).getId();
                            Message = new CompanyMessage(seen , id,title, message , name,since );
                            messages.add(Message);

                            mAdapter.notifyDataSetChanged();

                        }
                        progress.dismiss();
                  }

                  @Override
                  public void onFailure(Call<ResultModel> call, Throwable t) {
                      if (t instanceof IOException){
                          final Dialog noInternet = AppConfig.InternetFaild(MessageActivity.this);
                          final Button btn = noInternet.findViewById(R.id.Retry);
                          noInternet.show();
                          btn.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View view) {
                                  noInternet.cancel();
                                  getmessages();
                              }
                          });
                      }
                  }
              });

          }
      }.start();
      }


      public void read (View v ){
        messagesfilter.clear();
        mAdapter = new MessageAdabter(messagesfilter , MessageActivity.this);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        CompanyMessage message = null ;
        for (int i =0 ; i < messages.size() ; i++){
            message = messages.get(i);
            if (message.getSeen() == 1){
                messagesfilter.add(message);
                mAdapter.notifyDataSetChanged();
            }
        }

      }

      public void unRead(View v){
          messagesfilter.clear();
          mAdapter = new MessageAdabter(messagesfilter , MessageActivity.this);
          recyclerView.setAdapter(mAdapter);
          mAdapter.notifyDataSetChanged();
          CompanyMessage message = null ;
          for (int i =0 ; i < messages.size() ; i++){
              message = messages.get(i);
              if (message.getSeen() == 0){
                  messagesfilter.add(message);
                  mAdapter.notifyDataSetChanged();
              }
          }
      }
      public void all(View v){
          mAdapter = new MessageAdabter(messages , MessageActivity.this);
          recyclerView.setAdapter(mAdapter);
          mAdapter.notifyDataSetChanged();
      }

}
