package it.polimi.dima.giftlist;

import java.util.ArrayList;
import java.util.List;

import it.polimi.dima.giftlist.model.Wishlist;

/**
 * Created by Elena on 13/01/2016.
 */
public class DummyList implements DummyInterface {
    List<List<Wishlist>> mWishlists;

    public DummyList() {
        mWishlists = new ArrayList<>();
        List<Wishlist> l1 = new ArrayList<>();
        List<Wishlist> l2 = new ArrayList<>();
        List<Wishlist> l3 = new ArrayList<>();
        int i;
        for(i=0; i<5; i++) {
            l1.add(new Wishlist("You deserve "+i+" milkshakes"));
            l2.add(new Wishlist("You have to give me "+i+" chocolate bars"));
            l3.add(new Wishlist("I really need you to do "+i+" squats"));
        }
        mWishlists.add(l1);
        mWishlists.add(l2);
        mWishlists.add(l3);
    }


    @Override
    public List<List<Wishlist>> getDummyList() {
        return mWishlists;
    }
}
