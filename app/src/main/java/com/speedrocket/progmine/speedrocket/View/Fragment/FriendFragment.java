package com.speedrocket.progmine.speedrocket.View.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.speedrocket.progmine.speedrocket.ChatActivity;
import com.speedrocket.progmine.speedrocket.Control.Friends;
import com.speedrocket.progmine.speedrocket.R;
import com.speedrocket.progmine.speedrocket.TestFireBaseTessssssssssssssssssst;
import com.speedrocket.progmine.speedrocket.User_Profle;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;
import static com.speedrocket.progmine.speedrocket.View.Activites.LoginScreen.MY_PREFS_NAME;

public class FriendFragment extends Fragment {

    private RecyclerView mFriendsList;

    private DatabaseReference mFriendDatabase;
    private DatabaseReference mUsersDatabase;
    public Context context;
    private Integer mCurrent_user_id;

    private View mMainView;

    public FriendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_friends, container, false);

        mFriendsList = (RecyclerView)mMainView.findViewById(R.id.friendRecycleList);


        //---CURRENT USER ID--
        SharedPreferences preferences =  this.getActivity().getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
        mCurrent_user_id = preferences.getInt("id",0);
        Log.e("CurrentUserID :",mCurrent_user_id.toString());

        mFriendDatabase = FirebaseDatabase.getInstance().getReference().child("friends").child(mCurrent_user_id.toString());
        mFriendDatabase.keepSynced(true);

        //---USERS DATA

        mUsersDatabase=FirebaseDatabase.getInstance().getReference().child("users");
        mUsersDatabase.keepSynced(true);

        mFriendsList.setHasFixedSize(true);
        mFriendsList.setLayoutManager(new LinearLayoutManager(getContext()));

        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();

        //---FETCHING DATABASE FROM mFriendDatabase USING Friends.class AND ADDING TO RECYCLERVIEW----
        FirebaseRecyclerAdapter<Friends,FriendsViewHolder> friendsRecycleAdapter=new FirebaseRecyclerAdapter<Friends, FriendsViewHolder>(

                Friends.class,
                R.layout.users_layout,
                FriendsViewHolder.class,
                mFriendDatabase
        ) {
            @Override
            protected void populateViewHolder(final FriendsViewHolder friendViewHolder,
                                              Friends friends, int position) {
//                Button profile_btn = mMainView.findViewById(R.id.profile_btn);
//                profile_btn.setVisibility(View.INVISIBLE);
//                profile_btn.setEnabled(true);


                friendViewHolder.setDate(friends.getDate());
                final String list_user_id=getRef(position).getKey();
                mUsersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //---IT WORKS WHENEVER CHILD OF mMessageDatabase IS CHANGED---

                        final String userName=dataSnapshot.child("name").getValue().toString();
                       // final String user_id=dataSnapshot.child("name").getValue().toString();
                        if(dataSnapshot.hasChild("online")){
                            String userOnline = dataSnapshot.child("online").getValue().toString();
                            friendViewHolder.setOnline(userOnline);

                        }
                        friendViewHolder.setName(userName);
                       // friendViewHolder.setUserImage(userthumbImage,getContext());

                        //--ALERT DIALOG FOR OPEN PROFILE OR SEND MESSAGE----

                        friendViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CharSequence[] options = new CharSequence[]{"Open Profile" , "Send Message"};
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setTitle("Select Options");
                                builder.setItems(options,new AlertDialog.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        if(which == 0){
                                            Intent intent=new Intent(getContext(), TestFireBaseTessssssssssssssssssst.class);
                                            intent.putExtra("profile_name",userName);
                                            intent.putExtra("UserId",list_user_id);
                                            startActivity(intent);
                                        }

                                        if(which == 1){
                                            Intent intent = new Intent(getContext(), ChatActivity.class);
                                            intent.putExtra("user_id",list_user_id);
                                            intent.putExtra("user_name",userName);
                                            startActivity(intent);
                                        }

                                    }
                                });
                                builder.show();

                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };
        mFriendsList.setAdapter(friendsRecycleAdapter);
    }

    public static class FriendsViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public FriendsViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }
        public void setDate(String date){

            TextView userNameView = (TextView) mView.findViewById(R.id.textViewSingleListStatus);
            userNameView.setText(date);

        }
        public void setName(String name){

            TextView userNameView = (TextView) mView.findViewById(R.id.textViewSingleListName);
            userNameView.setText(name);

        }
        public void setUserImage(String userThumbImage, Context ctx){
            CircleImageView userImageview=(CircleImageView)mView.findViewById(R.id.circleImageViewUserImage);
            Picasso.with(ctx).load(userThumbImage).placeholder(R.drawable.username).into(userImageview);
        }
        public void setOnline(String isOnline){
            ImageView online=(ImageView)mView.findViewById(R.id.userSingleOnlineIcon);
            if(isOnline.equals("true")){
                online.setVisibility(View.VISIBLE);
            }
            else{
                online.setVisibility(View.INVISIBLE);
            }
        }
    }


    }

