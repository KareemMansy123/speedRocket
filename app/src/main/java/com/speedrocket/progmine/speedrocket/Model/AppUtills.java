package com.speedrocket.progmine.speedrocket.Model;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nex3z.notificationbadge.NotificationBadge;
import com.speedrocket.progmine.speedrocket.R;
import com.speedrocket.progmine.speedrocket.View.Activites.CompanyProfile;
import com.speedrocket.progmine.speedrocket.View.Activites.PostDetails;
import com.speedrocket.progmine.speedrocket.View.Activites.ProductsScreen;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.speedrocket.progmine.speedrocket.View.Activites.LoginScreen.MY_PREFS_NAME;

/**
 * Created by progmine on 7/31/2018.
 */

  public   class AppUtills {
   static  public  List<BasketItem> basket = new ArrayList<>();
    public AppUtills(){}
    public static class CONSTANTS {

        public static final int FORALLWINNEROFFERS = 0 ;
        public static final int FORSOLDOFFERS = 1 ;
        public static final  int FORACTIVEOFFERS = 2 ;
        public static final  int FOREXPIREDOFFERS = 3 ;




    }

    public  static  class notificationBadge {


             static BasketItem item ;
            static String   totalPrice ;
            public static void increaseNumberOfItemsBuy(final Context context, final NotificationBadge badge , final int id , final int type , final int userId) {
                final ProgressDialog progress;
                progress = new ProgressDialog(context);
                progress.setTitle("Please Wait");
                progress.setMessage("Loading..");
                progress.setCancelable(false);
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                if (!((Activity) context).isFinishing()) {
                    //show dialog
                    progress.show();
                }

                  new Thread(){
                      public void run()
                      {
                          Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/",context);


                          final UserApi userApi = retrofit.create(UserApi.class);
                          final Call<ResultModel> increaseBasket =
                                  userApi.setProductToBasket(id , type , userId);
                          Log.e("apputils" , " id  "+id);
                          Log.e("apputils" , " type  "+type);
                          Log.e("apputils" , " userid "+userId);
                          increaseBasket.enqueue(new Callback<ResultModel>() {
                              @Override
                              public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                                try {
                                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    if (!response.body().getMessage().equalsIgnoreCase("product is found")) {


                                        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, 0);
                                        SharedPreferences.Editor editor = prefs.edit();
                                        int number = prefs.getInt("ItemsNumber", 0) + 1;
                                        editor.putInt("ItemsNumber", number);
                                        editor.apply();
                                        badge.setNumber(number);
                                        if (progress != null && progress.isShowing()) {
                                            progress.dismiss();
                                        }
                                    }else{
                                    progress.dismiss();
                                    }
                                }catch (Exception e){
                                    Toast.makeText(context, "there is some server problems ", Toast.LENGTH_SHORT).show();
                                    if (progress != null && progress.isShowing()) {
                                        progress.dismiss();
                                    }
                                }
                              }

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
                                              increaseNumberOfItemsBuy(context , badge , id , type , userId);
                                          }
                                      });
                                  }
                              }
                          });

                      }

                  }.start();
            }


            public static void decreaseNumberOfItemsBuy(final Context context, final NotificationBadge badge , final int basketId  , final int userId) {
                final ProgressDialog progress;
                progress = new ProgressDialog(context);
                progress.setTitle("Please Wait");
                progress.setMessage("Loading..");
                progress.setCancelable(false);
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                if (!((Activity) context).isFinishing()) {
                    //show dialog
                    progress.show();
                }

                new Thread(){
                    public void run()
                    {
                        Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/",context);


                        final UserApi userApi = retrofit.create(UserApi.class);
                        final Call<ResultModel> decreaseBasket =
                                userApi.deleteProductFromBasket(basketId , userId);
                        decreaseBasket.enqueue(new Callback<ResultModel>() {
                            @Override
                            public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                                Toast.makeText(context , response.body().getMessage(),Toast.LENGTH_SHORT).show();

                                SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, 0);
                                SharedPreferences.Editor editor = prefs.edit();
                                if (prefs.getInt("ItemsNumber" ,0) > 0){
                                    int number = prefs.getInt("ItemsNumber", 0) - 1;
                                    editor.putInt("ItemsNumber", number);
                                    editor.apply();
                                }else {
                                    editor.putInt("ItemsNumber", 0);
                                    editor.apply();
                                }

                               // badge.setNumber(number);
                                if (progress != null && progress.isShowing()) {
                                    progress.dismiss();
                                }
                            }

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
                                            decreaseNumberOfItemsBuy(context,badge,basketId,userId);
                                        }
                                    });
                                }
                            }
                        });

                    }

                }.start();
            }



            public static void setNotifecationBadgeByShared(NotificationBadge badge, Context context) {
                SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, 0);
                badge.setNumber(prefs.getInt("ItemsNumber", 0));
            }

            public static void setNotifecationBadge(NotificationBadge badge, int number) {
                badge.setNumber(number);
            }

            public static void clearNotificationBage(NotificationBadge badge) {
                badge.clear();
            }


        }

    public static  class onLineAdvertisment {
          static  Dialog dialog;
           static ImageView advertiseImage , closeBtn;

          static   boolean check = true ; ;
           static String image ;
           static int action  , Id ;


          public static  void showAdvertisment(final Context context , final String image  ){

              dialog = new Dialog(context); // Context, this, etc.
              dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
              dialog.setContentView(R.layout.popup_advertisment);
              dialog.setCancelable(true);
              dialog.setCanceledOnTouchOutside(true);

              advertiseImage = dialog.findViewById(R.id.advertis_main_image);
              closeBtn = dialog.findViewById(R.id.advertise_close);
              Log.e("advertise:::" , image);
              new Thread(){
                  @Override
                  public void run() {


                      try {
                          URL   url = new URL("https://speed-rocket.com/upload/advertise/"+image);
                          Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                          advertiseImage.setImageBitmap(bmp);
                      } catch (Exception e) {
                          e.printStackTrace();
                      }

                  }
              }.start();

              Picasso.with(context).load("https://speed-rocket.com/upload/advertise/"+image).fit().centerCrop()
                      .into(advertiseImage);




              advertiseImage.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      if (action == 0){
                          Intent intent = new Intent(view.getContext() , ProductsScreen.class);
                          intent.putExtra("productId",Id);
                          intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                          context.startActivity(intent);
                      }else if(action == 1){

                          Intent intent = new Intent(view.getContext() , PostDetails.class);
                          intent.putExtra("offerID",Id);
                          view.getContext().startActivity(intent);
                          intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                      }else if (action == 2){
                          Intent intent = new Intent(view.getContext() , CompanyProfile.class);
                          intent.putExtra("companyId",Id);
                          intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                          view.getContext().startActivity(intent);
                      }
                  }
              });
              closeBtn.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      dialog.dismiss();
                  }
              });

             dialog.show();

          }
          public static void getAdvertiseFromFireBase(final Context context ){
               check = true ;

              FirebaseApp.initializeApp(context);
              final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
              database.addChildEventListener(new ChildEventListener() {
                  @Override
                  public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                      if (dataSnapshot.getKey().equalsIgnoreCase("advertise")) {
                          advertisement advertise = dataSnapshot.getValue(advertisement.class);
                          String code =context.getSharedPreferences("advertise" , 0).getString("code","null");
                            if (!advertise.getCode().equalsIgnoreCase(code)) {
                                image = advertise.getImage();
                                action = advertise.getAction();
                                Id = advertise.getId();
                                showAdvertisment(context,advertise.getImage());
                              SharedPreferences.Editor edite = context.getSharedPreferences("advertise",0).edit();
                              edite.putString("code",advertise.getCode());
                              edite.apply();
                            }
                      }
                  }
                  @Override
                  public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                      if (dataSnapshot.getKey().equalsIgnoreCase("advertise")) {
                          advertisement advertise = dataSnapshot.getValue(advertisement.class);
                          String code =context.getSharedPreferences("advertise" , 0).getString("code","null");
                          if (!advertise.getCode().equalsIgnoreCase(code)) {
                              image = advertise.getImage();
                              action = advertise.getAction();
                              Id = advertise.getId();
                              showAdvertisment(context,advertise.getImage());
                              SharedPreferences.Editor edite = context.getSharedPreferences("advertise",0).edit();
                              edite.putString("code",advertise.getCode());
                              edite.apply();
                          }
                      }
                  }

                  @Override
                  public void onChildRemoved(DataSnapshot dataSnapshot) {

                  }

                  @Override
                  public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                  }

                  @Override
                  public void onCancelled(DatabaseError databaseError) {

                  }
              });


          }



        }

    public static class Rate {
        public static void setRate(final int companyId, final int userId, final float rate, final Context context) {

            final ProgressDialog progress;
            progress = new ProgressDialog(context);
            progress.setTitle("Please Wait");
            progress.setMessage("Loading..");
            progress.setCancelable(false);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            if (!((Activity) context).isFinishing()) {
                //show dialog
                progress.show();
            }
            new Thread() {
                @Override
                public void run() {
                    Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/",context);


                    UserApi api = retrofit.create(UserApi.class);
                    Call<ResultModel> setProgress = api.rateCompany(companyId, userId, rate);

                    setProgress.enqueue(new Callback<ResultModel>() {
                        @Override
                        public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                            progress.dismiss();
                            Toast.makeText(context, "thanks for your rating ", Toast.LENGTH_SHORT).show();
                        }

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
                                        setRate(companyId,userId,rate,context);
                                    }
                                });
                            }
                        }
                    });


                }
            }.start();


        }




    }

            public static  void getUserCoins(final Context context){
            final SharedPreferences pref = context.getSharedPreferences("MyPrefsFile", 0);
           final int id = pref.getInt("id" , 0);
            Log.e("Apputills##" ," id : " +id  );
            if (id!= 0){
                new Thread(){
                    @Override
                    public void run() {

                        Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/",context);


                        final UserApi userApi = retrofit.create(UserApi.class);
                        final Call<ResultModel> userCoins = userApi.getUserCoins(id);
                        userCoins.enqueue(new Callback<ResultModel>() {
                            @Override
                            public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                                SharedPreferences.Editor edite = context.getSharedPreferences("MyPrefsFile", 0).edit();
                                edite.putInt("coins" , response.body().getUserCoins());
                                edite.putInt("ItemsNumber" , response.body().getBasketItem());
                                edite.putBoolean("msg" ,response.body().isUnSeen());
                                edite.apply();




                                Log.e("Apputills##" ," " +response.body().getUserCoins() );
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
                                            getUserCoins(context);
                                        }
                                    });
                                }
                            }
                        });

                    }
                }.start();

            }else {
            }

        }


    public static class upgradeClass{
        static int upgradeDialogCheck = 0 ;
        public static void showUpgradeDialoog(Context context  ){



               Dialog upgradeDialog  = new Dialog(context);
               upgradeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
               upgradeDialog.setContentView(R.layout.upgraded_dialog);
               upgradeDialog.setCancelable(false);
               upgradeDialog.setCanceledOnTouchOutside(false);

               final Button btn = upgradeDialog.findViewById(R.id.update);
               btn.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       final String appPackageName = view.getContext().getPackageName(); // getPackageName() from Context or Activity object
                       try {
                          view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                       }
                       catch (android.content.ActivityNotFoundException anfe) {
                           view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                       }
                   }
               });


                  upgradeDialog.show();

           }


    }

}
