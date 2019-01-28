package com.speedrocket.progmine.speedrocket.View.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.speedrocket.progmine.speedrocket.Control.RequestAdapter;
import com.speedrocket.progmine.speedrocket.R;

import java.util.ArrayList;
import java.util.List;

import rx.internal.operators.BackpressureUtils;

import static android.content.Context.MODE_PRIVATE;
import static com.speedrocket.progmine.speedrocket.View.Activites.LoginScreen.MY_PREFS_NAME;

public class RequestFragment extends Fragment {
    private RecyclerView mReqList;
//    private TextView test25;
    private List<String> requestList = new ArrayList<>();
    private Button getdataFirebase;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mUsersDatabase;
    private DatabaseReference mMessageDatabase;

    public Context context;

    private Integer mCurrent_user_id;
  //  private String mUserName;

    private View mMainView;
    private String showname;
    private RequestAdapter mRequestAdapter;

    public RequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_request, container, false);
        mReqList = (RecyclerView)mMainView.findViewById(R.id.recyclerViewRequestList);



     SharedPreferences preferences =  this.getActivity().getSharedPreferences(MY_PREFS_NAME,context.MODE_PRIVATE);
     mCurrent_user_id = preferences.getInt("id",0);





//        SharedPreferences sharedPreferences = PreferenceManager
//                .getDefaultSharedPreferences(getContext());
//        mCurrent_user_id = sharedPreferences.getInt("id", 0);

//        getdataFirebase.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.e("Current id :",mCurrent_user_id.toString());
//            }
//        });



        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("friend_request");
        mDatabaseReference.keepSynced(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        mReqList.setHasFixedSize(true);
        mReqList.setLayoutManager(linearLayoutManager);

        requestList.clear();
        mRequestAdapter = new RequestAdapter(requestList);
        mReqList.setAdapter(mRequestAdapter);
//        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("friend_request").child("name");
//
//        getdataFirebase.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//               mUsersDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
//                   @Override
//                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                       String getname = dataSnapshot.getValue().toString();
//                       test25.setText(getname);
//                       Log.e("aleeerttt",getname);
//                   }
//
//                   @Override
//                   public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                   }
//               });
//            }
//        });

        mDatabaseReference.child(mCurrent_user_id.toString()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String userId = dataSnapshot.getKey();
                requestList.add(userId);
                mRequestAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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

        return mMainView;

    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
