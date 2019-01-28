package com.speedrocket.progmine.speedrocket;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.speedrocket.progmine.speedrocket.View.Activites.LoginScreen.MY_PREFS_NAME;

public class User_Profle extends AppCompatActivity {
    public TextView viewId;
    public String firstName;

//    public String mCurrent_state;

    public ImageView mProfileImage;
    public TextView mProfileName,
            mProfileStatues,
            mProfileFriendsCount;
    public Button SendReqBtn,
            mDeclineBtn;
    public Integer CurrentUserID;
    public String mCurrent_state;
    public String User_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__profle);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        firstName = prefs.getString("firstName", "");
        CurrentUserID = prefs.getInt("id", 0);


        final String UserId = getIntent().getStringExtra("UserId");
        User_ID = UserId;
        final String profile_name = getIntent().getStringExtra("profile_name");



        viewId = findViewById(R.id.profile_totalFriends);
        mProfileName = findViewById(R.id.profile_DisplayName);
       SendReqBtn = findViewById(R.id.profile_Send_Req_Btn);
        mProfileImage = findViewById(R.id.profile_Image);
        mCurrent_state = "not_Friend";


       viewId.setText(CurrentUserID.toString());
       mProfileName.setText(firstName);




//    public void GetData(View view){
//        FirebaseDatabase.getInstance().getReference().child("FriendRequest").child("user1Info").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//
//                    profileModel model = snapshot.getValue(profileModel.class);
//
//                    Log.e("data: ",model.getName()+model.getUserId());
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


//
//
//    }
//    //search by name in Firebase
//    public void getitembyQuesry(View view){
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("FriendRequest").child("user1Info");
//
//        Query query = reference.orderByChild("name").equalTo("ahmed");
//
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                try {
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//
//                        profileModel model = snapshot.getValue(profileModel.class);
//
//                        Log.v("your data is:",model.getUserId());
//                    }
//                }catch (Exception e){
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
//


    }


}
