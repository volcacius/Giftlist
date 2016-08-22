package it.polimi.dima.giftlist.util;

import java.util.ArrayList;
import java.util.List;

import it.polimi.dima.giftlist.data.repository.datasource.EbayProductDataSource;
import it.polimi.dima.giftlist.domain.repository.ProductRepository;

/**
 * Created by Elena on 22/08/2016.
 */
public class CategoryDeterminer {

    public static ArrayList<String> getCategoriesFromOccasion(String occasion) {
        ArrayList<String> categories = new ArrayList<>();
        switch (occasion) {

            case "graduation" : {
                categories.add("wedding");
                break;
            }
            case "wedding" : {
                categories.add("wedding");
                break;
            }
            case "anniversary":
            case "engagement" :{
                categories.add("jewelry");
                break;
            }
            case "christening" :
            case "newborn" : {
                categories.add("baby");
            }
            //case "birthday"case "Christmas"case "thank you"case "other":
            default: {
                categories.add("others");
                break;
            }
        }


        return categories;
    }

    public static ArrayList<String> getCategoriesFromAge(String ageSelected) {
        ArrayList<String> categories = new ArrayList<>();
        categories.add(ageSelected);
        return categories;
    }
}
