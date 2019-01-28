package com.speedrocket.progmine.speedrocket.Control;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.speedrocket.progmine.speedrocket.Model.Offer;
import com.speedrocket.progmine.speedrocket.R;
import com.speedrocket.progmine.speedrocket.View.Activites.CompanyProfile;
import com.speedrocket.progmine.speedrocket.View.Activites.PostDetails;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ibrahim on 3/21/2018.
 */

public class PostsAdapter1 extends RecyclerView.Adapter<PostsAdapter1.MyViewHolder> {


    private Context context;
    private ProgressDialog progress;
    private Handler handler;
    private List<Offer> offerList;
    int o;


    int topSrcoin = 0;
    int view = 0;


    public class MyViewHolder extends RecyclerView.ViewHolder {


        public Button start_btn;
        public int postID;
        public TextView companyName, price, description, offertime ,active;
        public CircleImageView profileimage;
        public ImageView postimage;

        public MyViewHolder(View itemView) {
            super(itemView);

            companyName =  (TextView) itemView.findViewById(R.id.companName);
            description =  (TextView) itemView.findViewById(R.id.description);
                  // the max coins offerd in the offer
                  price =  (TextView) itemView.findViewById(R.id.price); // price of the product in the offer
                   //number of users in every offer
            active = itemView.findViewById(R.id.Cview);
           profileimage =  (CircleImageView) itemView.findViewById(R.id.profile_image);
              postimage =  (ImageView) itemView.findViewById(R.id.post_image);
              offertime =  (TextView) itemView.findViewById(R.id.offerTime);


            start_btn = (Button)itemView.findViewById(R.id.button_start);

        }
    }


    public PostsAdapter1(List<Offer> offerList, Context context) {
        this.context = context;
        this.offerList = offerList;

    }

    @Override
    public PostsAdapter1.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);


        return new PostsAdapter1.MyViewHolder(itemView);
    }





            @Override
    public void onBindViewHolder(final PostsAdapter1.MyViewHolder holder, final int position) {
                final Offer offer = offerList.get(position);



                holder.postID = offer.getId();

                if (offer.isNow()) {
                    double timeN = Double.parseDouble(offer.getTime()); // 94.5
                    int t = (int) timeN; //94
                    holder.offertime.setText("" + t);
                }else{
                }

                // getProfielData(offer.getUserId());

                final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);


                            holder.start_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(final View view) {
                                    view.startAnimation(buttonClick);
                                    if (offer.isNow()) {
                                    Intent intent = new Intent(view.getContext(), PostDetails.class);
                                    intent.putExtra("offerID", offer.getId());
                                    view.getContext().startActivity(intent);
                                    }else {
                                        Toast.makeText(context , " sorry  offer isn't running currently " , Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });

                            holder.postimage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(final View view) {
                                    view.startAnimation(buttonClick);

                                    if (offer.isNow()) {
                                        Intent intent = new Intent(view.getContext(), PostDetails.class);
                                        intent.putExtra("offerID", offer.getId());
                                        view.getContext().startActivity(intent);
                                    }else {
                                        Toast.makeText(context , " sorry  offer isn't running currently " , Toast.LENGTH_SHORT).show();

                                    }
                                       /* Toast.makeText(view.getContext(),"OfferId from method : "+offer.getId()
                                                +"  Offer Id  From firebase : "+o+"   topCoin : "+topSrcoin
                                                ,Toast.LENGTH_LONG).show(); */

                                }
                            });




                holder.profileimage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.startAnimation(buttonClick);

                        Intent intent = new Intent(v.getContext() , CompanyProfile.class);
                        intent.putExtra("companyId",offer.getCompanyId());
                        v.getContext().startActivity(intent);
                    }
                });

                holder.companyName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.startAnimation(buttonClick);

                        Intent intent = new Intent(v.getContext() , CompanyProfile.class);
                        intent.putExtra("companyId",offer.getCompanyId());
                        v.getContext().startActivity(intent);
                    }
                });



                if (context.getSharedPreferences("MyPref" , 0).getString("langa" , "").equalsIgnoreCase("ar")){
                    holder.companyName.setText(offer.getAr_companyName());
                    holder.description.setText(offer.getAr_description());
                }else{

                    holder.description.setText(offer.getEn_description());
                    holder.companyName.setText(offer.getCompanyName());
                }



                holder.price.setText(offer.getPrice()+"");



                Picasso.with(context).load("https://speed-rocket.com/upload/offers/"
                        +offer.getImage()).
                        fit().centerCrop().into(holder.postimage);

                Picasso.with(context).load("https://speed-rocket.com/upload/logo/"
                        +offer.getCompanyLogo()).
                        fit().centerCrop().into(holder.profileimage);

    } // on Bind



    @Override
    public int getItemCount() {
        return offerList.size();
    }
} // class of PostsAdapter