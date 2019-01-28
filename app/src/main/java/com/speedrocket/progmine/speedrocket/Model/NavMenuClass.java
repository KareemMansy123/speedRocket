package com.speedrocket.progmine.speedrocket.Model;

import android.view.Menu;

import java.io.Serializable;
import java.util.ArrayList;



public class NavMenuClass implements Serializable
{


    Menu menu;
    ArrayList items;

    public NavMenuClass(Menu menu,ArrayList items){

        this.items = items;
        this.menu = menu;

    }

    public Menu getMenu(){
        return menu;
    }

    public ArrayList getItems(){
        return items;
    }
}
