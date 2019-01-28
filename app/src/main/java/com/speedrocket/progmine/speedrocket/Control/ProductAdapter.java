package com.speedrocket.progmine.speedrocket.Control;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.nex3z.notificationbadge.NotificationBadge;
import com.speedrocket.progmine.speedrocket.Model.AppConfig;
import com.speedrocket.progmine.speedrocket.Model.AppUtills;
import com.speedrocket.progmine.speedrocket.Model.Image;
import com.speedrocket.progmine.speedrocket.Model.Product;
import com.speedrocket.progmine.speedrocket.Model.ResultModel;
import com.speedrocket.progmine.speedrocket.Model.UserApi;
import com.speedrocket.progmine.speedrocket.R;
import com.speedrocket.progmine.speedrocket.View.Activites.LoginScreen;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.speedrocket.progmine.speedrocket.View.Activites.LoginScreen.MY_PREFS_NAME;



public class ProductAdapter  extends RecyclerView.Adapter<ProductAdapter.MyViewHolder>
{


    private List<Product> productlist;

    private double doublePrice ;
    private int intPrice , realPrice =0 ;
    private final double  DOT_ZERO = 0.5 ;

    private ProgressDialog progress;
    private Handler handler;
    private Context context ;
    List <Image> images;
    ArrayList<String> imagesUrl = new ArrayList<String>();
   NotificationBadge badge ;
    String urls []={"https://image.ibb.co/mUVkQd/11_9664.png"
            ,"https://image.ibb.co/mbBgyy/Gionee_A1_64_GB_Black_SDL352791824_1_ff379.jpg"
    ,"https://image.ibb.co/hVbEJy/i_phone_6_500x500.jpg"};



    public  class MyViewHolder extends RecyclerView.ViewHolder
    {


        public LinearLayout productbNumberLayout ;
        public Button buy_btn , moreInfo_btn , dialog_buyNow_btn , dialog_cancel_btn;
        public TextView product_name , product_points , product_description ,
                dialog_product_description , companyName ,product_price , egWord , productNumper;
        public ImageView product_image ;
        public Dialog dialogProductMoreInfo;
        public SliderLayout sliderShow;
        TextSliderView textSliderView ;



        public MyViewHolder(View itemView) {
            super(itemView);

            buy_btn = (Button) itemView.findViewById(R.id.productstart);
            moreInfo_btn = (Button) itemView.findViewById(R.id.productInfo);
            product_image = (ImageView) itemView.findViewById(R.id.productimage);
            product_name = (TextView) itemView.findViewById(R.id.productname);
            product_description = (TextView) itemView.findViewById(R.id.productDescription);
            companyName = (TextView) itemView.findViewById(R.id.companNameProduct);
            productNumper = itemView.findViewById(R.id.product_qty);
            product_price = (TextView) itemView.findViewById(R.id.priceProduct);
            egWord = (TextView) itemView.findViewById(R.id.egWord);

            dialogProductMoreInfo = new Dialog(itemView.getContext()); // Context, this, etc.
            dialogProductMoreInfo.setContentView(R.layout.dialog_moreinfo_product);

            dialog_buyNow_btn = (Button) dialogProductMoreInfo.findViewById(R.id.buyMoreInfoProduct);
            dialog_cancel_btn = (Button) dialogProductMoreInfo.findViewById(R.id.cancelMoreInfoProduct);
            dialog_product_description =(TextView)  dialogProductMoreInfo.findViewById(R.id.descriptionMoreInfoProduct);

            productbNumberLayout = itemView.findViewById(R.id.numberProductLayout) ;
         //   sliderShow = (SliderLayout) dialogProductMoreInfo.findViewById(R.id.sliderInMoreInfo);
           /* sliderShow.setCustomIndicator((PagerIndicator) dialogProductMoreInfo.findViewById(R.id.custom_indicator));*/
           // textSliderView= new TextSliderView (itemView.getContext());
        }



    }

    public  ProductAdapter(List<Product> productlist , Context context , NotificationBadge badge)
    {

        this.context = context;
        this.productlist = productlist ;
        this.badge = badge ;

    }
    @Override
    public ProductAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
      /*  Toast.makeText(itemView.getContext(), "hi", Toast.LENGTH_SHORT).show();*/

        return new ProductAdapter.MyViewHolder(itemView);
    }




    @Override
    public void onBindViewHolder(final ProductAdapter.MyViewHolder holder, final int position) {

        final Product product = productlist.get(position);

       // holder.productbNumberLayout.setVisibility(View.VISIBLE);
        holder.egWord.setVisibility(View.VISIBLE);
        holder.product_price.setVisibility(View.VISIBLE);
        final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);

        holder.buy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                    Log.e("productid##" , ""+product.getId());

                SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, 0);
                if (prefs.getBoolean("login" ,false)){
                    AppUtills.notificationBadge.increaseNumberOfItemsBuy(context ,badge,product.getId(),0,prefs.getInt("id",0));
                    view.setEnabled(false);
                }else {
                    Toast.makeText(context, "please log in first :)", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context , LoginScreen.class);
                    context.startActivity(intent);

                }

               /*Toast.makeText(view.getContext(), "Product "+post.getPostID(), Toast.LENGTH_LONG).show();*/
             /*   Intent intent = new Intent(view.getContext(), PaymentScreen.class);
                intent.putExtra("kind","product"); // kind may be product or coin
                intent.putExtra("price" ,product.getPrice());
                intent.putExtra("productId",product.getId());
                view.getContext().startActivity(intent);*/


            }
        });

         SharedPreferences shared = context.getSharedPreferences("MyPref",0);

        if (shared.getString("langa","ff").equalsIgnoreCase("ar")){

         holder.dialog_product_description.setText(product.getAr_discription());

        }else{

            holder.dialog_product_description.setText(product.getEn_discription());

        }



        holder.moreInfo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                view.setAnimation(buttonClick);

                holder.dialogProductMoreInfo.show();

               more_info(view , holder , product);

                // display dialogu with image slider of product and discreptions
            }
        });

        holder.product_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                view.startAnimation(buttonClick);
               /*Toast.makeText(view.getContext(), "Product "+post.getPostID(), Toast.LENGTH_LONG).show();*/

                holder.dialogProductMoreInfo.show();

                more_info(view , holder , product);
            }
        });

        holder.dialog_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.dialogProductMoreInfo.cancel();
            }
        });

        holder.dialog_buyNow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent(view.getContext(), PaymentScreen.class);
                intent.putExtra("kind","product"); // kind may be product or coin
                intent.putExtra("price" ,product.getPrice());
                intent.putExtra("productId",product.getId());
                view.getContext().startActivity(intent);*/


                holder.dialogProductMoreInfo.cancel();
                SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, 0);
                if (prefs.getBoolean("login" ,false)){
                    AppUtills.notificationBadge.increaseNumberOfItemsBuy(context ,badge,product.getId(),0,prefs.getInt("id",0));
                    holder.buy_btn.setEnabled(false);
                }else {
                    Toast.makeText(context, "please log in first :)", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context , LoginScreen.class);
                    context.startActivity(intent);
                }

            }
        });


        doublePrice = product.getPrice() ;
        intPrice= (int)doublePrice ;
        if ((doublePrice-intPrice)>DOT_ZERO)
            realPrice = intPrice+1 ;
        else
            realPrice = intPrice ;

        holder.product_price.setText(realPrice+"");

        if (context.getSharedPreferences("MyPref" , 0).getString("langa" , "").equalsIgnoreCase("ar")){
            holder.product_name.setText(product.getAr_title());
            holder.product_description.setText(product.getAr_discription());
            Log.e("productScreen" ,"arTitle"+product.getAr_title());
            Log.e("productScreen" ,"enTitle"+product.getEn_title());
            Log.e("productScreen" ,"endisc"+product.getEn_discription());
            Log.e("productScreen" ,"ardisc"+product.getAr_discription());
            holder.companyName.setText(product.getCompanyName());
        }else{
            Log.e("productScreen" ,"arTitle"+product.getAr_title());
            Log.e("productScreen" ,"enTitle"+product.getEn_title());
            Log.e("productScreen" ,"endisc"+product.getEn_discription());
            Log.e("productScreen" ,"ardisc"+product.getAr_discription());
            holder.product_name.setText(product.getEn_title());
            holder.product_description.setText(product.getEn_discription());
            holder.companyName.setText(product.getCompanyName());
        }

        holder.companyName.setVisibility(View.VISIBLE);
        holder.productNumper.setText(""+product.getProductQty());

        Picasso.with(context).load("https://speed-rocket.com/upload/products/"+product.getImage())
                .fit().centerCrop().into(holder.product_image);


    }



    @Override
    public int getItemCount() {
        return productlist.size();
    }



    private void more_info(final View view , final ProductAdapter.MyViewHolder holder , final Product product){

        progress = new ProgressDialog(view.getContext());
        progress.setTitle("Please Wait");
        progress.setMessage("Loading..");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        handler = new Handler()
        {

            @Override
            public void handleMessage(Message msg)
            {
                progress.dismiss();
                super.handleMessage(msg);
            }

        };

        progress.show();

        new Thread()
        {
            public void run()
            {

                //Retrofit

                Retrofit retrofit = AppConfig.getRetrofit2("https://speed-rocket.com/api/",context);



                final UserApi userApi = retrofit.create(UserApi.class);
                Call<ResultModel> getProductConnection =
                        userApi.getProductDetails(product.getId());

                getProductConnection.enqueue(new Callback<ResultModel>() {
                    @Override
                    public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {

                        try {


                            SliderLayout sliderShow = (SliderLayout) holder.dialogProductMoreInfo.findViewById(R.id.sliderInMoreInfo);

                            sliderShow.removeAllSliders();
                            images=response.body().getImages();
                            for(int i = 0 ; i < images.size() ; i++)
                            {

                                TextSliderView textSliderView = new TextSliderView(view.getContext());
                                textSliderView
                                        .image("https://speed-rocket.com/upload/products/"+ images.get(i).getImage());
                                sliderShow.addSlider(textSliderView);
                            }
                                Log.e("youareherenow","you are here now in image slider");
                            progress.dismiss();



                        } catch (Exception e) {
                            Toast.makeText(view.getContext(), "Connection Success\n" +
                                            "Exception"+e.toString()
                                    ,Toast.LENGTH_LONG).show();


                        }
                        progress.dismiss();
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
                                   more_info(view , holder , product);
                                }
                            });
                        }


                    }
                });
                //Retrofit

            }

        }.start();




    }

} // class of PostsAdapter