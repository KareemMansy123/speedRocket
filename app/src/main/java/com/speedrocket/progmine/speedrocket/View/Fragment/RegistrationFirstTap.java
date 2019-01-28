package com.speedrocket.progmine.speedrocket.View.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.speedrocket.progmine.speedrocket.R;
import com.speedrocket.progmine.speedrocket.View.Activites.RegistrationCustom;

public class RegistrationFirstTap extends Fragment {
     EditText mobile ;

     public interface  onCallBack{

         void RegisterNumber(String number) ;
     }
     private onCallBack callBack ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.registration_first_tap,
                container,false);

        mobile = view.findViewById(R.id.reg_personalmobile);
        mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.e("mobileNumber" , "mobileNumber##" + charSequence.toString());
                RegistrationCustom.rMobileNumber = charSequence.toString();

                callBack.RegisterNumber(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        return view ;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            callBack = (onCallBack) (Activity)context;
        } catch (ClassCastException e) {

        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        RegistrationCustom.check = 0;
        RegistrationCustom.buttonVisibalty(true);
    }
}
