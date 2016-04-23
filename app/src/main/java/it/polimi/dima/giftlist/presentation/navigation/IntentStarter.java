package it.polimi.dima.giftlist.presentation.navigation;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import it.polimi.dima.giftlist.presentation.view.activity.ProductListActivity;

/**
 * Created by Alessandro on 18/03/16.
 */
public class IntentStarter {

    @Inject
    public IntentStarter() {
    }

    public static void startProductListActivity(@NonNull Context context, HashMap<Class, Boolean> enabledRepositoryMap, String category, String keywords) {
        Intent intentToLaunch = ProductListActivity.getCallingIntent(context, enabledRepositoryMap, category, keywords);
        context.startActivity(intentToLaunch);
    }
}
