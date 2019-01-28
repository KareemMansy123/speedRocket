package com.speedrocket.progmine.speedrocket.View.Fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.speedrocket.progmine.speedrocket.R;

/**
 * Created by Ibrahim on 9/6/2018.
 */

public class InstructionsTab extends Fragment {


     ImageView instruction ;
     final  String IMAGE_ID[] = new String[]{"@drawable/ins1" , "@drawable/inst2","@drawable/inst3","@drawable/inst4","@drawable/inst5"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view=inflater.inflate(R.layout.instruction_tap,
                container,false);
        instruction = view.findViewById(R.id.instruction_image);
       int position = getArguments().getInt("position");
        Log.e("fragment " , " position " + position ) ;
        int imageResource = getResources().getIdentifier(IMAGE_ID[position], null, getActivity().getPackageName());

        Drawable res = getResources().getDrawable(imageResource);
        instruction.setImageDrawable(res);

        return view ;
}

}
