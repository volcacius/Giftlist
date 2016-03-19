package it.polimi.dima.giftlist.presentation.navigation;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import it.polimi.dima.giftlist.presentation.view.activity.ProductListActivity;

/**
 * Created by Alessandro on 18/03/16.
 */
public class IntentStarter {

    @Inject
    public IntentStarter() {
    }

    public static void startProductListActivity(@NonNull Context context, String category, String keywords) {
        Intent intentToLaunch = ProductListActivity.getCallingIntent(context, category, keywords);
        context.startActivity(intentToLaunch);
    }
}
