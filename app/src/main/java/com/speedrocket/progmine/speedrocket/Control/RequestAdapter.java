package com.speedrocket.progmine.speedrocket.Control;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.speedrocket.progmine.speedrocket.R;
import com.speedrocket.progmine.speedrocket.TestFireBaseTessssssssssssssssssst;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {
    private List<String> requestList;
    DatabaseReference mDatabaseReference ;
    String userName;
    private Context ctx;

    public RequestAdapter(List<String> requestList) {
        this.requestList = requestList;
    }

    @Override
    public RequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_layout,parent,false);
        return new RequestAdapter.RequestViewHolder(view);

    }

    public class RequestViewHolder extends RecyclerView.ViewHolder {

        public TextView displayName;
        public TextView displayStatus;
        public CircleImageView displayImage;
        public ImageView imageView;

        public RequestViewHolder(View itemView) {
            super(itemView);

            ctx = itemView.getContext();

            displayName = (TextView)itemView.findViewById(R.id.textViewSingleListName);
            displayStatus = (TextView) itemView.findViewById(R.id.textViewSingleListStatus);
            displayImage = (CircleImageView)itemView.findViewById(R.id.circleImageViewUserImage);
            imageView = (ImageView)itemView.findViewById(R.id.userSingleOnlineIcon);
            imageView.setVisibility(View.INVISIBLE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("username :",userName);

                    int pos = getAdapterPosition();
                    Intent intent = new Intent(ctx,TestFireBaseTessssssssssssssssssst.class);
                    intent.putExtra("UserId",requestList.get(pos));
                    intent.putExtra("profile_name",userName);
                    ctx.startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(final RequestViewHolder holder, final int position) {

        String user_id = requestList.get(position);
        mDatabaseReference.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                 userName = dataSnapshot.child("name").getValue().toString();
                holder.displayName.setText(userName);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

       /* holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ctx,ProfileActivity.class);
                intent.putExtra("user_id",requestList.get(position));


            }
        }); */


    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

}