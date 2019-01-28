package com.speedrocket.progmine.speedrocket.Control;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.speedrocket.progmine.speedrocket.Model.PersonalUser;
import com.speedrocket.progmine.speedrocket.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class FireBaseViewHolder extends RecyclerView.ViewHolder {

      View mView ;
      Context mContext ;
    public TextView firstname , lastname , srcoin , country , expire;
    public ImageView countryimage ;

    public CircleImageView profileimage;
    Button offerhim ;
    Button sendofferhim , cancelofferhim ;
    Dialog offerhim_dialogue;


    public FireBaseViewHolder(View itemView) {
        super(itemView);
        mView = itemView ;
        mContext = itemView.getContext();
    }

    public void onBindViewHolder(PersonalUser person){

        firstname = (TextView) itemView.findViewById(R.id.firstnameoffer);
        lastname = (TextView) itemView.findViewById(R.id.lastnameoffer);
        srcoin = (TextView) itemView.findViewById(R.id.offer);
        country = (TextView) itemView.findViewById(R.id.countryoffer);
        countryimage = (ImageView) itemView.findViewById(R.id.imagecountryoffer);
        profileimage= (CircleImageView) itemView.findViewById(R.id.profileimageoffer);
        offerhim = (Button) itemView.findViewById(R.id.offerhim);
        expire = (TextView) itemView.findViewById(R.id.expired);

        offerhim_dialogue = new Dialog(itemView.getContext()); // Context, this, etc.
        offerhim_dialogue.setContentView(R.layout.sendofferhim);
        offerhim_dialogue.setTitle("send offer him");


        sendofferhim = (Button) offerhim_dialogue.findViewById(R.id.sendofferhim);
        cancelofferhim = (Button) offerhim_dialogue.findViewById(R.id.cancelofferhim) ;



        firstname.setText(person.getFirstName());
        lastname.setText(person.getLastName());
//       srcoin.setText(person.getSrCoin());
       country.setText(person.getCountry());

        Picasso.with(mContext).load("https://speed-rocket.com/upload/users/"
                +person.getImage()).
                fit().centerCrop().into(profileimage);
    }
}
