package it.polimi.dima.giftlist.presentation.navigation;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import hugo.weaving.DebugLog;
import it.polimi.dima.giftlist.data.model.CategoryType;
import it.polimi.dima.giftlist.data.model.Product;
import it.polimi.dima.giftlist.presentation.view.activity.ProductDetailsPagerActivity;
import it.polimi.dima.giftlist.presentation.view.activity.ProductPickerActivity;
import it.polimi.dima.giftlist.presentation.view.activity.ProductPickerSettingsActivity;
import it.polimi.dima.giftlist.presentation.view.activity.WishlistActivity;
import it.polimi.dima.giftlist.presentation.view.activity.WishlistListActivity;
import it.polimi.dima.giftlist.presentation.view.activity.WishlistSettingsActivity;

/**
 * Created by Alessandro on 18/03/16.
 */
public class IntentStarter {

    @Inject
    public IntentStarter() {
    }

    public static void startProductPickerActivity(@NonNull Context context, HashMap<Class, Boolean> enabledRepositoryMap, ArrayList<CategoryType> chosenCategoriesList, String keywords, Float maxprice, Float minprice, long wishlistId) {
        Intent intentToLaunch = ProductPickerActivity.getCallingIntent(context, enabledRepositoryMap, chosenCategoriesList, keywords, maxprice, minprice, wishlistId);
        context.startActivity(intentToLaunch);
    }

    public static void startWishlistActivity(@NonNull Context context, long wishlistId) {
        Intent intentToLaunch = WishlistActivity.getCallingIntent(context, wishlistId);
        context.startActivity(intentToLaunch);
    }

    public static void startWishlistListActivity(@NonNull Context context) {
        Intent intentToLaunch = WishlistListActivity.getCallingIntent(context);
        context.startActivity(intentToLaunch);
    }

    public static void startProductPickerSettingsActivity(@NonNull Context context, long wishlistId) {
        Intent intentToLaunch = ProductPickerSettingsActivity.getCallingIntent(context, wishlistId);
        context.startActivity(intentToLaunch);
    }

    public static void startWishlistSettingsActivity(@NonNull Context context, long wishlistId) {
        Intent intentToLaunch = WishlistSettingsActivity.getCallingIntent(context,wishlistId);
        context.startActivity(intentToLaunch);
    }

    public static void startProductDetailsPagerActivity(@NonNull Context context, List<Product> productList, long selectedProductId) {
        ArrayList<Product> productArrayList = new ArrayList<>(productList);
        Intent intentToLaunch = ProductDetailsPagerActivity.getCallingIntent(context, productArrayList, selectedProductId);
        context.startActivity(intentToLaunch);
    }
}
