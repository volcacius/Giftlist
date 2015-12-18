package it.polimi.dima.giftlist.ui.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import it.polimi.dima.giftlist.GiftlistApplication;
import it.polimi.dima.giftlist.injection.component.ActivityComponent;
import it.polimi.dima.giftlist.injection.component.DaggerActivityComponent;
import it.polimi.dima.giftlist.injection.module.ActivityModule;

public class BaseActivity extends AppCompatActivity {

    private ActivityComponent mActivityComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public ActivityComponent getActivityComponent() {
        if (mActivityComponent == null) {
            mActivityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .applicationComponent(GiftlistApplication.get(this).getComponent())
                    .build();
        }
        return mActivityComponent;
    }

}
