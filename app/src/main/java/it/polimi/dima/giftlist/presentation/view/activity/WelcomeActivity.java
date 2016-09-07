package it.polimi.dima.giftlist.presentation.view.activity;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.redbooth.WelcomeCoordinatorLayout;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;
import net.steamcrafted.materialiconlib.MaterialIconView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.presentation.navigation.IntentStarter;
import it.polimi.dima.giftlist.presentation.view.animation.GiftAnimator;
import timber.log.Timber;

/**
 * Created by Alessandro on 29/08/16.
 */
public class WelcomeActivity extends BaseActivity {

    @Bind(R.id.coordinator)
    WelcomeCoordinatorLayout coordinatorLayout;
    @Bind(R.id.skip)
    Button skipButton;
    @Bind(R.id.next)
    Button nextButton;
    @Bind(R.id.finish)
    Button finishButton;

    MaterialIconView giftIcon;
    MaterialIconView personIcon;
    MaterialIconView quizIcon;
    MaterialIconView shopIcon;

    private boolean animationReady = false;
    private ValueAnimator backgroundAnimator;
    private GiftAnimator giftAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies();
        initListeners();
        initPages();
        initIcons();
        initBackgroundTransitions();
    }

    private void initIcons() {
        giftIcon = (MaterialIconView) findViewById(R.id.gift_icon);
        giftIcon.setIcon(MaterialDrawableBuilder.IconValue.GIFT);
        giftIcon.setColorResource(R.color.material_light_white);
        giftIcon.setSizeResource(R.dimen.welcome_icon);

        personIcon = (MaterialIconView) findViewById(R.id.person_icon);
        personIcon.setIcon(MaterialDrawableBuilder.IconValue.ACCOUNT);
        personIcon.setColorResource(R.color.material_light_white);
        personIcon.setSizeResource(R.dimen.welcome_icon);

        quizIcon = (MaterialIconView) findViewById(R.id.quiz_icon);
        quizIcon.setIcon(MaterialDrawableBuilder.IconValue.NOTE_TEXT);
        quizIcon.setColorResource(R.color.material_light_white);
        quizIcon.setSizeResource(R.dimen.welcome_icon);

        shopIcon = (MaterialIconView) findViewById(R.id.shop_icon);
        shopIcon.setIcon(MaterialDrawableBuilder.IconValue.SHOPPING);
        shopIcon.setColorResource(R.color.material_light_white);
        shopIcon.setSizeResource(R.dimen.welcome_icon);

    }

    private void initListeners() {
        coordinatorLayout.setOnPageScrollListener(new WelcomeCoordinatorLayout.OnPageScrollListener() {
            @Override
            public void onScrollPage(View v, float progress, float maximum) {
                if (!animationReady) {
                    animationReady = true;
                    backgroundAnimator.setDuration((long) maximum);
                }
                backgroundAnimator.setCurrentPlayTime((long) progress);
            }
            @Override
            public void onPageSelected(View v, int pageSelected) {
                if (pageSelected == 3) {
                    skipButton.setVisibility(View.INVISIBLE);
                    nextButton.setVisibility(View.INVISIBLE);
                    finishButton.setVisibility(View.VISIBLE);
                } else {
                    skipButton.setVisibility(View.VISIBLE);
                    nextButton.setVisibility(View.VISIBLE);
                    finishButton.setVisibility(View.INVISIBLE);
                }
                switch (pageSelected) {
                    case 0:
                        if (giftAnimator == null) {
                            giftAnimator = new GiftAnimator(coordinatorLayout);
                            giftAnimator.play();
                        }
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }
            }
        });
    }

    private void initPages() {
        coordinatorLayout.addPage(R.layout.page_welcome_first,
                R.layout.page_welcome_second,
                R.layout.page_welcome_third,
                R.layout.page_welcome_fourth);
    }

    private void initBackgroundTransitions() {
        Resources resources = getResources();
        int firstBackgroundColor = ResourcesCompat.getColor(resources, R.color.material_purple_600, getTheme());
        int secondBackgroundColor = ResourcesCompat.getColor(resources, R.color.material_purple_500, getTheme());
        int thirdBackgroundColor = ResourcesCompat.getColor(resources, R.color.material_purple_400, getTheme());
        int fourthBackgroundColor = ResourcesCompat.getColor(resources, R.color.material_purple_300, getTheme());
        backgroundAnimator = ValueAnimator
                .ofObject(new ArgbEvaluator(), firstBackgroundColor, secondBackgroundColor, thirdBackgroundColor, fourthBackgroundColor);
        backgroundAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                coordinatorLayout.setBackgroundColor((int) animation.getAnimatedValue());
            }
        });
    }

    public void injectDependencies() {
        getApplicationComponent().inject(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_welcome;
    }

    @OnClick(R.id.skip)
    void skip() {
        IntentStarter.startWishlistListActivity(this);
    }

    @OnClick(R.id.next)
    void next() {
        coordinatorLayout.setCurrentPage(coordinatorLayout.getPageSelected() + 1, true);
    }

    @OnClick(R.id.finish)
    void end() {
        IntentStarter.startWishlistListActivity(this);
    }
}
