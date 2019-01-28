package com.speedrocket.progmine.speedrocket.View.Activites;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.speedrocket.progmine.speedrocket.Model.ApiConfig;
import com.speedrocket.progmine.speedrocket.Model.AppConfig;
import com.speedrocket.progmine.speedrocket.Model.ResultModel;
import com.speedrocket.progmine.speedrocket.Model.UserApi;
import com.speedrocket.progmine.speedrocket.R;

import java.io.IOException;
import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.speedrocket.progmine.speedrocket.View.Activites.LoginScreen.MY_PREFS_NAME;

/**
 * Created by progmine on 7/4/2018.
 */

public class fawry extends AppCompatActivity implements Serializable {
  ProgressDialog  progress ;

    int coinsQuantity = 0  , productId =0  , userId = 0   , userCoins = 0 ;
    double  price =0  , shipping_cost = 0 ;
    String customerName = "" , customerPhone = "" , customerAddress = "" ,userEmail="" ;
    String[] type , amunts ,productIds , prices  , titles;
    String type_letter = "";
    boolean offer = false ;
    String marchantRefNumber = "";
    String productID = "" , TYPE = "" , Amount = "" ;
     int removableId = 0 ;
    int maxId = 0 ;
    WebView fawryIntegration  ;
    double totalPrice ;

    boolean saveTempraryorder = false ;



    @Override
    public void onBackPressed() {
       final  AlertDialog.Builder dialog = new AlertDialog.Builder(this) ;
        dialog.setMessage("are you sure you won't complete the payment process ...?");
        dialog.setPositiveButton("discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {



               //removeShopperTrack();
               Intent intet = new Intent(fawry.this , NavigationMenu.class) ;
               startActivity(intet);
               dialogInterface.dismiss();
               finish();
            }
        });
        dialog.setNegativeButton("complete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.show() ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fawry);

        setTitle(getString(R.string.fawry));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        userId =prefs.getInt("id", 0);
        userEmail=prefs.getString("email","");
        userCoins = prefs.getInt("coins",0);
        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if (b.containsKey("customerName")&&b.containsKey("customerAddress")&&b.containsKey("customerPhone")){
            customerName = (String)b.get("customerName");
            customerPhone = (String)b.get("customerPhone");
            customerAddress = (String)b.get("customerAddress");
        }
        if (b.containsKey("offer")){
            offer = true ;
        }else offer=false;

        if (b.containsKey("productId")){
            type_letter="P";
           // int refNum =new  Random().nextInt((1000000 -1000)+1 )+1000 ;

            productID =(String)b.get("productId") ;
            TYPE = (String)b.get("type") ;
            Amount =(String)b.get("amunts");
            productIds = (productID).split(",");
            titles = ((String)b.get("titles")).split(",");
            type = (TYPE).split(",");
            amunts =(Amount).split(",");
            prices =((String)b.get("prices")).split(",");
            price = Double.valueOf( b.get("price").toString());
            shipping_cost = (Double)b.get("shippingCoast");
            totalPrice = price+shipping_cost ;
            String quntity = "" , price = "" , title ="";


            fawryIntegration = (WebView)findViewById(R.id.fawryWebView);
            fawryIntegration.setWebViewClient(new WebViewClient());
            fawryIntegration.addJavascriptInterface( new javaScriptInterface(this) , "Action");
            if ( Build.VERSION.SDK_INT >= 21)
                CookieManager.getInstance().setAcceptThirdPartyCookies(fawryIntegration,true);
            WebSettings settings = fawryIntegration.getSettings();
            settings.setJavaScriptEnabled(true);

//        for (int i =0 ; i<productIds.length ; i++){
//            productId = Integer.valueOf(productIds[i]);
//             quntity = amunts[i];
//              price= prices[i];
//              title = titles[i];
//            items.add(addItem("product : " + title , ""+productId , price , quntity) );
//        }
//        items.add(addItem("shippingCost" , "0000" ,""+ shipping_cost , "1"));
            getMaxOrderId();
            saveTempraryorder = true ;
        }
        if (b.containsKey("coinsQuantity")){
            type_letter="C";
            coinsQuantity = (int)b.get("coinsQuantity") ;
            price = Double.valueOf( b.get("price").toString());
            totalPrice = price;
            Log.e("perice##","fawry offer : "+price);


            fawryIntegration = (WebView)findViewById(R.id.fawryWebView);
            fawryIntegration.setWebViewClient(new WebViewClient());
            fawryIntegration.addJavascriptInterface( new javaScriptInterface(this) , "Action");
          if ( Build.VERSION.SDK_INT >= 21)
            CookieManager.getInstance().setAcceptThirdPartyCookies(fawryIntegration,true);
            WebSettings settings = fawryIntegration.getSettings();
            settings.setJavaScriptEnabled(true);

            getMaxOrderId();
            saveTempraryorder = true ;
        }
       if (b.containsKey("advertise")){
           type_letter="A";
            marchantRefNumber = (String)b.get("refNumber");
            price = Double.valueOf( b.get("price").toString());
            Log.e("perice##","fawry offer : "+price);

           // initFawrySdk(false);
           saveTempraryorder = false ;

           fawryIntegration = (WebView)findViewById(R.id.fawryWebView);
           fawryIntegration.setWebViewClient(new WebViewClient());
           fawryIntegration.addJavascriptInterface( new javaScriptInterface(this) , "Action");
           if ( Build.VERSION.SDK_INT >= 21)
               CookieManager.getInstance().setAcceptThirdPartyCookies(fawryIntegration,true);
           WebSettings settings = fawryIntegration.getSettings();
           settings.setJavaScriptEnabled(true);
           Log.e("fawryintegration##" , "refnum :" +marchantRefNumber);
           fawryIntegration.loadUrl("https://speed-rocket.com/api/WebservFawery/get_view_basket?"+"user_id="+userId+"&ref_num="+marchantRefNumber+"&shipping="+shipping_cost);


       }


        Log.e("fawryintegration##" , "userId :" +userId);
        Log.e("fawryintegration##" , "refnum :" +marchantRefNumber);
        Log.e("fawryintegration##" , "shipping :" +shipping_cost);


    }

    public void removeShopperTrack (){
        new Thread(){

            @Override
            public void run() {

                Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/", getApplicationContext());

                final UserApi api = retrofit.create(UserApi.class);
                if (removableId!=0) {
                    final Call<ResultModel> remove = api.removeTrack(removableId);

                    remove.enqueue(new Callback<ResultModel>() {
                        @Override
                        public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                            try {
                                Log.e("removeIt", response.body().getMessage());
                            } catch (Exception e) {
                            }
                        }

                        @Override
                        public void onFailure(Call<ResultModel> call, Throwable t) {
                            Toast.makeText(getBaseContext(), "there is some server problems ..?", Toast.LENGTH_LONG).show();

                        }
                    });
                }
            }
        }.start();

    }

private void saveTempraryorder(final String productId , final String type ,final int userID ,final int coinsqty , final String refNumber ,
                               final String type_letter , final String  itemQty ,final double shipping_cost , final double price,
                               final String shopper_address , final String shopper_phone , final String shopper_name
                                )


{
    if (saveTempraryorder) {

        new Thread() {
            @Override
            public void run() {

                Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/", getApplicationContext());

                final UserApi userApi = retrofit.create(UserApi.class);
                final Call<ResultModel> saveProducts =
                        userApi.setShoppertrack(productId, type, userID, coinsqty, price, refNumber, type_letter, itemQty,shipping_cost, shopper_address, shopper_name, shopper_phone);
                Log.e("saveTemprary##", "product id  : " + productId);
                Log.e("saveTemprary##", "type  : " + type);
                Log.e("saveTemprary##", "userId  : " + userID);
                Log.e("saveTemprary##", "coins :  " + coinsqty);
                Log.e("saveTemprary##", "refNum  :  " + refNumber);
                Log.e("saveTemprary##", "type lettere : " + type_letter);
                Log.e("saveTemprary##", "quty  : " + itemQty);

                saveProducts.enqueue(new Callback<ResultModel>() {
                    @Override
                    public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                        try {
                            fawryIntegration.loadUrl("https://speed-rocket.com/api/WebservFawery/get_view_basket?"+"user_id="+userId+"&ref_num="+marchantRefNumber+"&shipping="+shipping_cost);
                            Log.e("saveTemprary##", response.body().getMessage());



                        } catch (Exception e) {
                            Toast.makeText(getBaseContext(), "there is some server problems .. 401", Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<ResultModel> call, Throwable t) {
                        progress.dismiss();
                        if (t instanceof IOException){
                            final Dialog noInternet = AppConfig.InternetFaild(fawry.this);
                            final Button btn = noInternet.findViewById(R.id.Retry);
                            noInternet.show();
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    noInternet.cancel();
                                    saveTempraryorder(productId , type ,userID,coinsqty,refNumber,type_letter,itemQty,shipping_cost,price,shopper_address,shopper_phone,shopper_name);

                                }
                            });
                        }
                    }
                });


            }
        }.start();
    }

}






        public void getMaxOrderId(){

            final ProgressDialog progress = new ProgressDialog(this);
            progress.setTitle("Please Wait");
            progress.setMessage("Loading..");
            progress.setCancelable(false);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            final Handler handler ;

            if (!((Activity) this).isFinishing()) {
                //show dialog
                progress.show();
            }

            handler =    new Handler( ){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                }
            };


            new Thread(){
                public void run()
                {
                    Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/", getApplicationContext());

                    final ApiConfig userApi = retrofit.create(ApiConfig.class);
                    final Call<ResultModel> getMaxId =
                            userApi.getMaxOrderId();

                    getMaxId.enqueue(new Callback<ResultModel>() {
                        @Override
                        public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {

                            try{

                                Log.e("maxId##" , ""+response.body().getMaxOrdersId());
                                maxId = response.body().getMaxOrdersId() + 1;
                                progress.dismiss();
                                marchantRefNumber =type_letter+(maxId)+userId ;
                                saveTempraryorder(productID ,TYPE,userId,coinsQuantity ,marchantRefNumber , type_letter ,Amount ,shipping_cost, price , customerAddress,customerPhone,customerName);

                                //initFawrySdk(true);
                            }catch(Exception e) {
                                Toast.makeText(getBaseContext() , "there is some server problems ..?" , Toast.LENGTH_LONG).show();
                            }


                        }

                        @Override
                        public void onFailure(Call<ResultModel> call, Throwable t) {
                            if (t instanceof IOException){
                                final Dialog   noInternet = AppConfig.InternetFaild(fawry.this);
                                final Button btn = noInternet.findViewById(R.id.Retry);
                                noInternet.show();
                                btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        noInternet.cancel();
                                        getMaxOrderId();
                                    }
                                });
                            }
                        }
                    });
                }
                }.start();

        }




    public class javaScriptInterface{
            Context mContext ;

            javaScriptInterface(Context c){
                mContext = c ;
            }

            @JavascriptInterface
            public void onSucsses(String msg){
                Toast.makeText(mContext , msg , Toast.LENGTH_LONG).show();
            }
            @JavascriptInterface
            public void onFail(String msg){
                Toast.makeText(mContext , msg , Toast.LENGTH_LONG).show();
            }

    }
}
