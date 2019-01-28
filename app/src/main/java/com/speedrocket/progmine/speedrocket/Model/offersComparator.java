package com.speedrocket.progmine.speedrocket.Model;

import java.util.Comparator;

/**
 * Created by Ibrahim on 8/5/2018.
 */

public class offersComparator implements Comparator<CurrentOffers> {
    @Override
    public int compare(CurrentOffers currentOffers, CurrentOffers currentOffers1) {
        return 0/*currentOffers.getImageSortnumber()>currentOffers1.getImageSortnumber() ? 0 : 1 */;
    }
}
