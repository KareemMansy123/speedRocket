package com.speedrocket.progmine.speedrocket.View.Activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

public class StartUp extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent inten  = new Intent(StartUp.this , SplachScreen.class);
        startActivity(inten);
        finish();

//        Log.e("**************","**********************************STAETuP");
//        Intent i = new Intent(StartUp.this,TestAct.class);
//        startActivity(i);
      //  finish();
    }
}
