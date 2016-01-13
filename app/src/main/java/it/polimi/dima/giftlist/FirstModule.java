package it.polimi.dima.giftlist;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.polimi.dima.giftlist.wishlistlist.WishlistListAdapter;

/**
 * Created by Elena on 12/01/2016.
 */
@Module(
)
public class FirstModule {

    private Context context;

    public FirstModule(Context context) {
        this.context = context;
    }

    @Provides public Context provideContext() {
        return context;
    }

    @Provides @Singleton
    WishlistListAdapter provideMotor(){
        return new WishlistListAdapter();
    }
    /*
    @Provides @Singleton Picasso providesPicasso() {
        return Picasso.with(context);
    }


    @Provides @Singleton public GithubApi providesGithubApi() {

        OkHttpClient client = new OkHttpClient();
        client.setCache(new Cache(context.getCacheDir(), 10 * 1024 * 1024));

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setClient(new OkClient(client))
                .setEndpoint("https://api.github.com")
                .build();

        return restAdapter.create(GithubApi.class);
    }

    @Provides
    @Singleton
    public ErrorMessageDeterminer providesErrorMessageDeterminer(){
        return new ErrorMessageDeterminer();
    }*/
}
