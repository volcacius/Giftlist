package it.polimi.dima.giftlist.presentation.navigation;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import java.util.HashMap;

import javax.inject.Inject;

import it.polimi.dima.giftlist.presentation.view.activity.ProductPickerActivity;
import it.polimi.dima.giftlist.presentation.view.activity.ProductPickerSettingsActivity;
import it.polimi.dima.giftlist.presentation.view.activity.WishlistActivity;
import it.polimi.dima.giftlist.presentation.view.activity.WishlistListActivity;

/**
 * Created by Alessandro on 18/03/16.
 */
public class IntentStarter {

    @Inject
    public IntentStarter() {
    }

    public void startProductPickerActivity(@NonNull Context context, HashMap<Class, Boolean> enabledRepositoryMap, String category, String keywords, long wishlistId) {
        Intent intentToLaunch = ProductPickerActivity.getCallingIntent(context, enabledRepositoryMap, category, keywords, wishlistId);
        context.startActivity(intentToLaunch);
    }

    public void startWishlistActivity(@NonNull Context context, long wishlistId) {
        Intent intentToLaunch = WishlistActivity.getCallingIntent(context, wishlistId);
        context.startActivity(intentToLaunch);
    }

    public void startWishlistListActivity(@NonNull Context context) {
        Intent intentToLaunch = WishlistListActivity.getCallingIntent(context);
        context.startActivity(intentToLaunch);
    }

    public void startProductPickerSettingsActivity(@NonNull Context context, long wishlistId) {
        Intent intentToLaunch = ProductPickerSettingsActivity.getCallingIntent(context, wishlistId);
        context.startActivity(intentToLaunch);
    }
}
