package com.speedrocket.progmine.speedrocket;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.speedrocket.progmine.speedrocket.Control.RequestAdapter;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.speedrocket.progmine.speedrocket.View.Activites.LoginScreen.MY_PREFS_NAME;

public class TestFireBaseTessssssssssssssssssst extends AppCompatActivity {
    public ImageView mProfileImage;
    private TextView mProfileName,mProfileStatus,mprofileFriendCount;
    private Button mProfileSendReqButton,mProfileDeclineReqButton;
    private Context context;
      public String CurrentName;
    public Integer CurrentUserID;

    public String mCurrent_state;
    public String User_ID;

    //Firebase Data
    DatabaseReference mfriendReqReference;
    DatabaseReference mDatabaseReference;
    DatabaseReference mFriendDatabase;
    DatabaseReference mNotificationReference;
    DatabaseReference mRootReference;
    DatabaseReference getmDatabaseReference;
    //progress Dialog
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_fire_base_tessssssssssssssssssst);

        // User 1  Data
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        CurrentName = prefs.getString("firstName", "");
        CurrentUserID = prefs.getInt("id", 0);

            //User 2 Data
        final String UserId = getIntent().getStringExtra("UserId");
        User_ID = UserId;
        final String profile_name = getIntent().getStringExtra("profile_name");
       // final String mProfileimage = getIntent().getStringExtra("profile_image");
//        Bitmap bitmapimage = getIntent().getExtras().getParcelable("profile_image");
//        Log.e("imageURLLLL", String.valueOf(bitmapimage));


        mProfileImage = (ImageView)findViewById(R.id.profileUserImage);
        mProfileName = (TextView)findViewById(R.id.profileUserName);
        mProfileStatus=(TextView)findViewById(R.id.profileUserStatus);
        mprofileFriendCount=(TextView)findViewById(R.id.profileUserFriends);
        mProfileSendReqButton=(Button)findViewById(R.id.profileSendReqButton);
        mProfileDeclineReqButton=(Button)findViewById(R.id.profileDeclineReqButton);;


        mProfileStatus.setText(User_ID.toString());
        mProfileName.setText(profile_name);

        if (getIntent().hasExtra("image_profile")){
            String profile_imagee =  getIntent().getStringExtra("image_profile");
            Glide.with(this).load("https://speed-rocket.com/upload/users/"+profile_imagee).into(mProfileImage);
        }



        mfriendReqReference=FirebaseDatabase.getInstance().getReference().child("friend_request");
        mDatabaseReference=FirebaseDatabase.getInstance().getReference().child("users").child(UserId);
        mFriendDatabase=FirebaseDatabase.getInstance().getReference().child("friends");
        mNotificationReference=FirebaseDatabase.getInstance().getReference().child("notifications");
        mRootReference=FirebaseDatabase.getInstance().getReference();

        mProgressDialog=new ProgressDialog(TestFireBaseTessssssssssssssssssst.this);
        mProgressDialog.setTitle("Fetching Details");
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setProgress(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        mCurrent_state = "not_friends"; // 4 types--- "not_friends" , "req_sent"  , "req_received" & "friends"

        mProfileStatus.setText(User_ID.toString());
        mProfileName.setText(profile_name);

        //----ADDING NAME , STATUS AND IMAGE OF USER----
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //----ADDING TOTAL  NO OF FRIENDS---
                mFriendDatabase.child(UserId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long len = dataSnapshot.getChildrenCount();
                        mprofileFriendCount.setText("TOTAL FRIENDS : "+len);

                        //----SEEING THE FRIEND STATE OF THE USER---
                        //----ADDING THE TWO BUTTON-----
                        mfriendReqReference.child(CurrentUserID.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //----CHECKING IF FRIEND REQUEST IS SEND OR RECEIVED----
                                if(dataSnapshot.hasChild(UserId)){

                                    String request_type = dataSnapshot.child(UserId).child("request_type").getValue().toString();
                                        Toast.makeText(TestFireBaseTessssssssssssssssssst.this,request_type,Toast.LENGTH_LONG);


                                    if(request_type.equals("sent")){

                                        mCurrent_state="req_sent";
                                        mProfileSendReqButton.setText("Cancel Friend Request");
                                        mProfileDeclineReqButton.setVisibility(View.INVISIBLE);
                                        mProfileDeclineReqButton.setEnabled(false);

                                    }

                                    else if(request_type.equals("received")){
                                        mCurrent_state="req_received";
                                        mProfileSendReqButton.setText("Accept Friend Request");
                                        mProfileDeclineReqButton.setVisibility(View.VISIBLE);
                                        mProfileDeclineReqButton.setEnabled(true);
                                    }

                                    mProgressDialog.dismiss();
                                }

                                //---USER IS FRIEND----
                                else{

                                    mFriendDatabase.child(CurrentUserID.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            mProfileDeclineReqButton.setVisibility(View.INVISIBLE);
                                            mProfileDeclineReqButton.setEnabled(false);

                                            if(dataSnapshot.hasChild(UserId)){
                                                mCurrent_state="friends";
                                                mProfileSendReqButton.setText("Unfriend This Person");
                                            }
                                            mProgressDialog.dismiss();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                            mProgressDialog.dismiss();
                                        }
                                    });

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(TestFireBaseTessssssssssssssssssst.this, "Error fetching Friend request data", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mProgressDialog.dismiss();
            }
        });

        //-------SEND REQUEST BUTTON IS PRESSED----
        mProfileSendReqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mUserId= CurrentUserID.toString();

                if(mUserId.equals(UserId)){
                    Toast.makeText(TestFireBaseTessssssssssssssssssst.this, "Cannot send request to your own", Toast.LENGTH_SHORT).show();
                    return ;
                }

                Log.e("m_current_state is : ",mCurrent_state);
                mProfileSendReqButton.setEnabled(false);


                //-------NOT FRIEND STATE--------
                if(mCurrent_state.equals("not_friends")){

                    DatabaseReference newNotificationReference = mRootReference.child("notifications").child(UserId).push();

                    String newNotificationId = newNotificationReference.getKey();

                    HashMap<String,String> notificationData=new HashMap<String, String>();
                    notificationData.put("from",CurrentUserID.toString());
                    notificationData.put("type","request");

                    Map requestMap = new HashMap();
                    requestMap.put("friend_request/"+UserId.toString()+ "/"+UserId + "/request_type","sent");
                    requestMap.put("friend_request/"+UserId.toString()+ "/"+UserId + "/name",profile_name);
                    requestMap.put("friend_request/"+UserId+"/"+CurrentUserID.toString()+"/request_type","received");
                    requestMap.put("friend_request/"+UserId+"/"+CurrentUserID.toString()+"/name",CurrentName);
//                    requestMap.put("friend_request/"+"name",profile_name);
                    requestMap.put("notifications/"+UserId+"/"+newNotificationId,notificationData);



                    //----FRIEND REQUEST IS SEND----
                    mRootReference.updateChildren(requestMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError==null){


                                Toast.makeText(TestFireBaseTessssssssssssssssssst.this, "Friend Request sent successfully", Toast.LENGTH_SHORT).show();

                                mProfileSendReqButton.setEnabled(true);
                                mCurrent_state= "req_sent";
                                mProfileSendReqButton.setText("Cancel Friend Request");

                            }
                            else{
                                mProfileSendReqButton.setEnabled(true);
                                Toast.makeText(TestFireBaseTessssssssssssssssssst.this, "Some error in sending friend Request", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }

                //-------CANCEL--FRIEND--REQUEST-----

                if(mCurrent_state.equals("req_sent")){

                    Map valueMap=new HashMap();
                    valueMap.put("friend_request/"+CurrentUserID.toString()+"/"+UserId,null);
                    valueMap.put("friend_request/"+UserId+"/"+CurrentUserID.toString(),null);
//                    valueMap.put("friend_request/"+profile_name,null);

                    //----FRIEND REQUEST IS CANCELLED----
                    mRootReference.updateChildren(valueMap, new DatabaseReference.CompletionListener() {

                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError == null){

                                mCurrent_state = "not_friends";
                                mProfileSendReqButton.setText("Send Friend Request");
                                mProfileSendReqButton.setEnabled(true);

//                                Intent intent = new Intent(TestFireBaseTessssssssssssssssssst.this, RequestAdapter.class);
//                                intent.putExtra("UserId",UserId.toString());
//                                intent.putExtra("profile_name",profile_name.toString());
                                Toast.makeText(TestFireBaseTessssssssssssssssssst.this, "Friend Request Cancelled Successfully...", Toast.LENGTH_SHORT).show();
                            }
                            else{

                                mProfileSendReqButton.setEnabled(true);
                                Toast.makeText(TestFireBaseTessssssssssssssssssst.this, "Cannot cancel friend request...", Toast.LENGTH_SHORT).show();

                            }
                        }

                    });




                }

                //-------ACCEPT FRIEND REQUEST------

                if(mCurrent_state.equals("req_received")){
                    //-----GETTING DATE-----
                    Date current_date=new Date(System.currentTimeMillis());

                    //Log.e("----Date---",current_date.toString());
                    String date[]=current_date.toString().split(" ");
                    final String todays_date=(date[1] + " " + date[2] + "," + date[date.length-1]+" "+date[3]);

                    Map friendMap=new HashMap();
                    friendMap.put("friends/"+CurrentUserID.toString()+"/"+UserId+"/date",todays_date);
                    friendMap.put("friends/"+UserId+"/"+CurrentUserID.toString()+"/date",todays_date);

                    friendMap.put("friend_request/"+CurrentUserID.toString()+"/"+UserId,null);
                    friendMap.put("friend_request/"+UserId+"/"+CurrentUserID.toString(),null);

                    //-------BECAME FRIENDS------
                    mRootReference.updateChildren(friendMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            if(databaseError==null){

                                mProfileSendReqButton.setEnabled(true);
                                mCurrent_state = "friends";
                                mProfileSendReqButton.setText("Unfriend this person");
                                mProfileDeclineReqButton.setEnabled(false);
                                mProfileDeclineReqButton.setVisibility(View.INVISIBLE);

                            }
                            else{
                                mProfileSendReqButton.setEnabled(true);
                                Toast.makeText(TestFireBaseTessssssssssssssssssst.this, "Error is " +databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }


                //----UNFRIEND---THIS---PERSON----
                if(mCurrent_state.equals("friends")){

                    Map valueMap=new HashMap();
                    valueMap.put("friends/"+CurrentUserID.toString()+"/"+UserId,null);
                    valueMap.put("friends/"+UserId+"/"+CurrentUserID.toString(),null);

                    //----UNFRIENDED THE PERSON---
                    mRootReference.updateChildren(valueMap, new DatabaseReference.CompletionListener() {

                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError == null){
                                mCurrent_state = "not_friends";
                                mProfileSendReqButton.setText("Send Friend Request");
                                mProfileSendReqButton.setEnabled(true);
                                Toast.makeText(TestFireBaseTessssssssssssssssssst.this, "Successfully Unfriended...", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                mProfileSendReqButton.setEnabled(true);
                                Toast.makeText(TestFireBaseTessssssssssssssssssst.this, "Cannot Unfriend..Contact Kshitiz..", Toast.LENGTH_SHORT).show();

                            }
                        }

                    });


                }

            }
        });


        //-----DECLING THE FRIEND REQUEST-----
        mProfileDeclineReqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map valueMap=new HashMap();
                valueMap.put("friend_request/"+CurrentUserID.toString()+"/"+UserId,null);
                valueMap.put("friend_request/"+UserId+"/"+CurrentUserID.toString(),null);

                mRootReference.updateChildren(valueMap, new DatabaseReference.CompletionListener() {

                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if(databaseError == null){

                            mCurrent_state = "not_friends";
                            mProfileSendReqButton.setText("Send Friend Request");
                            mProfileSendReqButton.setEnabled(true);
                            Toast.makeText(TestFireBaseTessssssssssssssssssst.this, "Friend Request Declined Successfully...", Toast.LENGTH_SHORT).show();

                            mProfileDeclineReqButton.setEnabled(false);
                            mProfileDeclineReqButton.setVisibility(View.INVISIBLE);
                        }
                        else{

                            mProfileSendReqButton.setEnabled(true);
                            Toast.makeText(TestFireBaseTessssssssssssssssssst.this, "Cannot decline friend request...", Toast.LENGTH_SHORT).show();

                        }
                    }

                });


            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
      //  getmDatabaseReference.child("online").setValue("true");
    }

    @Override
    protected void onStop() {
        super.onStop();
       // getmDatabaseReference.child("online").setValue(ServerValue.TIMESTAMP);

    }


    }

