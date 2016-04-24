package it.polimi.dima.giftlist.util;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.widget.Toast;

import it.polimi.dima.giftlist.R;
import timber.log.Timber;

/**
 * Created by Alessandro on 24/04/16.
 */
public final class ToastFactory {

    public static void showToast(@Nullable Context context, @Nullable String message, int length) {
        try {
            if (context != null && !TextUtils.isEmpty(message)) {
                Toast.makeText(context, message, length).show();
            }
        } catch (Exception e) {
            Timber.w(context.getString(R.string.toast_error), e);
        }
    }

    public static void showToast(@Nullable Context context, @StringRes int stringRes, int length, @Nullable Object... formatArgs) {
        try {
            showToast(context, context.getString(stringRes, formatArgs), length);
        } catch (Exception e) {
            Timber.w(context.getString(R.string.toast_error), e);
        }
    }

    public static void showShortToast(@Nullable Context context, @Nullable String message) {
        showToast(context, message, Toast.LENGTH_SHORT);
    }

    public static void showShortToast(@Nullable Context context, @StringRes int stringRes, @Nullable Object... formatArgs) {
        showToast(context, stringRes, Toast.LENGTH_SHORT, formatArgs);
    }

    public static void showLongToast(@Nullable Context context, @Nullable String message) {
        showToast(context, message, Toast.LENGTH_LONG);
    }

    public static void showLongToast(@Nullable Context context, @StringRes int stringRes, @Nullable Object... formatArgs) {
        showToast(context, stringRes, Toast.LENGTH_LONG, formatArgs);
    }
}
