package com.speedrocket.progmine.speedrocket.Control;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.speedrocket.progmine.speedrocket.Model.Offer;
import com.speedrocket.progmine.speedrocket.Model.PersonalUser;
import com.speedrocket.progmine.speedrocket.Model.UserInOffer;
import com.speedrocket.progmine.speedrocket.R;
import com.speedrocket.progmine.speedrocket.View.Activites.CompanyProfile;
import com.speedrocket.progmine.speedrocket.View.Activites.PostDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostsAdapter extends  RecyclerView.Adapter<PostsAdapter.MyViewHolder>
{


    int usId =0;
    List <PersonalUser> user ;
    String user_firstName , user_lastName , user_companName  ;
    private double doublePrice ;
    private int intprice  , realPrice ;
    private final double DOT_ZERO = 0.5 ;
    private Context context ;
    private ProgressDialog progress;
    private Handler handler;
    private  List<Offer> offerList;
    int o ;



     MyViewHolder holder ;

    int topSrcoin = 0 ;
    int view = 0 ;

     int views ;


    List <Integer> idOfUsers = new ArrayList<Integer>();
    List <Integer> srCoinsOfUsers = new ArrayList<Integer>();
    private void preparePostData(int offerID)
    {



    } // function preparepostdata


    public  class MyViewHolder extends RecyclerView.ViewHolder{

        public Button start_btn;
        public  int postID;
        public TextView companyName ,  price , description ,offertime;
        public CircleImageView profileimage ;
        public ImageView postimage ;
         Dialog dialog ;
         TextView  t_dialog_description ;
         public Button mDescriptionBtn ;
        public Button dialogCancel;



        public MyViewHolder(View itemView) {
            super(itemView);

            dialog = new Dialog(context); // Context, this, etc.
            dialog.setContentView(R.layout.activity_dialog_more_details);
            dialog.setTitle(R.string.dialog_title);

            t_dialog_description = (TextView) dialog.findViewById(R.id.offerdetails_description);
            mDescriptionBtn = itemView.findViewById(R.id.br_moredetails);
             companyName = (TextView) itemView.findViewById(R.id.companName);
             description = (TextView) itemView.findViewById(R.id.description);
             dialogCancel = dialog.findViewById(R.id.bt_cancel_dialog_moredetails);
                 // the max coins offerd in the offer
                   price = (TextView) itemView.findViewById(R.id.price); // price of the product in the offer
            //number of users in every offer
            profileimage = (CircleImageView) itemView.findViewById(R.id.profile_image);
               postimage = (ImageView) itemView.findViewById(R.id.post_image);
               offertime = (TextView) itemView.findViewById(R.id.offerTime);


            start_btn=(Button)itemView.findViewById(R.id.button_start);

        }
    }

    public  PostsAdapter(List<Offer> offerList , Context context)
    {
        this.context = context;
        this.offerList = offerList ;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        this.holder = holder ;
      final Offer offer  = offerList.get(position);


            holder.postID = offer.getId();

            double timeN = Double.parseDouble(offer.getTime());
            int t = (int) timeN;
            holder.offertime.setText(""+t);

            final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);

            holder.start_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    view.startAnimation(buttonClick);

                    Intent intent = new Intent(view.getContext() , PostDetails.class);
                    intent.putExtra("offerID",offer.getId());
                    view.getContext().startActivity(intent);

                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
            });

            holder.postimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    view.startAnimation(buttonClick);

                    Intent intent = new Intent(view.getContext() , PostDetails.class);
                    intent.putExtra("offerID",offer.getId());
                    view.getContext().startActivity(intent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


                }
            });

           //    Log.e("loooookedhereeeeeeeeeee", String.valueOf(offer.getId()));// <K.M>


            holder.profileimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(buttonClick);

                    Intent intent = new Intent(v.getContext() , CompanyProfile.class);
                    intent.putExtra("companyId",offer.getCompanyId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);

                }
            });

            holder.companyName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(buttonClick);

                    Intent intent = new Intent(v.getContext() , CompanyProfile.class);
                    intent.putExtra("companyId",offer.getCompanyId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }
            });

// language
            if (context.getSharedPreferences("MyPref" , 0).getString("langa" , "").equalsIgnoreCase("ar")){
                holder.companyName.setText(offer.getAr_companyName());
                holder.description.setText(offer.getAr_title());
                holder. t_dialog_description.setText(offer.getAr_description());
                // Log.e("postAdapter#" , offer.getAr_title());
            }else{

                holder.description.setText(offer.getEn_title());
                holder.companyName.setText(offer.getCompanyName());
                holder. t_dialog_description.setText(offer.getEn_description());
                // Log.e("postAdapter#" , offer.getEn_title());

            }
            holder.mDescriptionBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.dialog.show();
                }
            });
            holder.dialogCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.dialog.cancel();
                }
            });
            doublePrice = offer.getPrice();
            intprice = (int)doublePrice ;
            Log.e("prices##" , " doublePrice : "+doublePrice);
            Log.e("prices##","IntPrice : "+intprice);
            if ((doublePrice-intprice) > DOT_ZERO){
                realPrice = intprice+1 ;
            }else{
                realPrice = intprice ;
            }
            Log.e("prices##","realPrice : "+realPrice);
            holder.price.setText(realPrice+"");


            Picasso.with(context).load("https://speed-rocket.com/upload/offers/"
                    +offer.getImage()).
                    fit().centerCrop().into(holder.postimage);



            Picasso.with(context).load("https://speed-rocket.com/upload/logo/"
                    +offer.getCompanyLogo()).
                    fit().centerCrop().into(holder.profileimage);






    }//on Bind



    @Override
    public int getItemCount() {
        return offerList.size();
    }






    public  void  getMaxSrcoinInThisOffer(final int offerId)
    {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child("UserInOffer");
        //Query offerQuery = ref.orderByChild("srCoin");


        if(ref != null) {


            ref.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    UserInOffer value = dataSnapshot.getValue(UserInOffer.class);
                    int offId = value.getOffersId();

                    Log.d("fireee","OfferId from method : "+offerId
                    +"  Offer Id  From firebase : "+offId+"   topCoin : "+topSrcoin);

                    if(offerId == offId)
                    {
                        topSrcoin = value.getSrCoin();
                    } // i
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    UserInOffer value = dataSnapshot.getValue(UserInOffer.class);
                    int offId = value.getOffersId();


                    if(offerId == offId)
                    {
                        topSrcoin = value.getSrCoin();
                    } // i
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


        } // if
    } // function of getMaxSrcoinInThisOffer
    public void updateUi(){


    }

//    public  class asynctascForCoinsAndnumPeople extends AsyncTask<DataSnapshot , Long , Integer[]>{
//
//
//        @Override
//        protected Integer[] doInBackground(DataSnapshot... dataSnapshots) {
//
//
//            return new Integer[0];
//        }
//
//        @Override
//        protected void onPostExecute(Integer[] strings) {
//            super.onPostExecute(strings);
//        }
//    }
//
    public class FireBaseOpretionAsync extends AsyncTask<Object[],Long,Integer>{


    @Override
    protected Integer doInBackground(Object[]... objects) {
       Log.e("object##" ,""+(boolean)objects[0][1]) ;
        return null;
    }
}
} // class of PostsAdapter


