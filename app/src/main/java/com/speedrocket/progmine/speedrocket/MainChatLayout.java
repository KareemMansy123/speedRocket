package com.speedrocket.progmine.speedrocket;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.speedrocket.progmine.speedrocket.Control.MyFragmentPagerAdapter;
import com.speedrocket.progmine.speedrocket.View.Activites.LoginScreen;

import static com.speedrocket.progmine.speedrocket.View.Activites.LoginScreen.MY_PREFS_NAME;

public class MainChatLayout extends AppCompatActivity {

    ViewPager mviewPager;
    MyFragmentPagerAdapter mFragmentPagerAdapter;
    TabLayout mtabLayout;
    DatabaseReference mDatabaseReference;
   public Context context;
    public Integer mCurrent_user_id;
    public String mCurrent_user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat_layout);

        mviewPager=(ViewPager)findViewById(R.id.viewPager);

        //---ADDING ADAPTER FOR FRAGMENTS IN VIEW PAGER----
        mFragmentPagerAdapter=new MyFragmentPagerAdapter(getSupportFragmentManager());
        mviewPager.setAdapter(mFragmentPagerAdapter);

        //---SETTING TAB LAYOUT WITH VIEW PAGER
        mtabLayout=(TabLayout)findViewById(R.id.tabLayout);
        mtabLayout.setupWithViewPager(mviewPager);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");

//        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
//        mCurrent_user_id = prefs.getInt("id", 0);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        mCurrent_user_id = prefs.getInt("id", 0);
        mCurrent_user_name = prefs.getString("firstName", "");
        //Toast.makeText(MainChatLayout.this,mCurrent_user_id.toString(),Toast.LENGTH_LONG).show();

    }

    public class MyListener implements DialogInterface.OnClickListener{

        @Override
        public void onClick(DialogInterface dialog, int which) {
            finish();
        }
    }

    //---IF USER IS NULL , THEN GOTO LOGIN PAGE----
    @Override
    protected void onStart() {
        super.onStart();



        if(mCurrent_user_id.toString()==null){
           startfn();
          //  mDatabaseReference.child(mCurrent_user_id.toString()).child("online").setValue(ServerValue.TIMESTAMP);
        }
        else{
            //---IF LOGIN , ADD ONLINE VALUE TO TRUE---
            mDatabaseReference.child(mCurrent_user_id.toString()).child("online").setValue("true");

            mDatabaseReference.child(mCurrent_user_id.toString()).child("name").setValue(mCurrent_user_name);

        }
    }

    @Override
    protected void onStop() {
        super.onStop();

     /* //-----for disabling online function when appliction runs in background----
        FirebaseUser user=mauth.getCurrentUser();
        if(user!=null){
            mDatabaseReference.child(user.getUid()).child("online").setValue(ServerValue.TIMESTAMP);
        }
        */
    }

    //---CREATING OPTION MENU---
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu_bar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId()==R.id.settings){
            Intent intent=new Intent(MainChatLayout.this,User_Profle.class);
            startActivity(intent);
        }
        if(item.getItemId()==R.id.allUsers){
            Intent intent=new Intent(MainChatLayout.this,users_View_List.class);
            startActivity(intent);
        }

        //---LOGGING OUT AND ADDING TIME_STAMP----
//        if(item.getItemId()==R.id.logout){
//            mDatabaseReference.child(mauth.getCurrentUser().getUid()).child("online").setValue(ServerValue.TIMESTAMP).addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if(task.isSuccessful()){
//
//                        FirebaseAuth.getInstance().signOut();
//                        startfn();
//                    }
//                    else{
//                        Toast.makeText(MainActivity.this, "Try again..", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        }
        return true;
    }

    //--OPENING LOGIN ACTIVITY--
    private void startfn(){
        Intent intent = new Intent(MainChatLayout.this,LoginScreen.class);
        startActivity(intent);
        finish();
    }
}
