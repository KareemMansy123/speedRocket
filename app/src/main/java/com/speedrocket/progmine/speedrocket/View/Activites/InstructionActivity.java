package com.speedrocket.progmine.speedrocket.View.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.speedrocket.progmine.speedrocket.Control.InstructionAdabter;
import com.speedrocket.progmine.speedrocket.R;

/**
 * Created by Ibrahim on 9/6/2018.
 */

public class InstructionActivity  extends AppCompatActivity {
              int count = -1;
              TabLayout tabLayout ;
              ViewPager viewPager;
              TextView finish ;

                    @Override
                    protected void onCreate(Bundle savedInstanceState) {
                        super.onCreate(savedInstanceState);
                        setContentView(R.layout.instructions_layout);



                        finish = findViewById(R.id.finish);
                        viewPager  =  findViewById(R.id.instruction_view_pager);
                        final InstructionAdabter adabter = new InstructionAdabter(getSupportFragmentManager() , 5);

                        viewPager.setAdapter(adabter);
                        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                if (position ==4) finish.setText("Finish >");
                            }

                            @Override
                            public void onPageSelected(int position) {

                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {

                            }
                        });
                        tabLayout =findViewById(R.id.tab_layout);
                        tabLayout.setupWithViewPager(viewPager, true);
                    }

                    public void increase (){
                         viewPager.getCurrentItem() ;
                      if (viewPager.getCurrentItem() !=4) count = viewPager.getCurrentItem()+1 ;
                        viewPager.setCurrentItem(count);
                    }

                    public void decrease (View v ){

                        if (! (count == 0) ) count-- ;
                        viewPager.setCurrentItem(count);
                    }

                    public void finish(View v ){
                        if (viewPager.getCurrentItem() == 4) {
                            Intent intent = new Intent(InstructionActivity.this, NavigationMenu.class);
                            v.getContext().startActivity(intent);
                            InstructionActivity.this.finish();
                        }else{

                            increase();
                        }
                    }

}
