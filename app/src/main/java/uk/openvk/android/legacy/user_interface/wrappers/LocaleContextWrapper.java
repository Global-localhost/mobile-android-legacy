package uk.openvk.android.legacy.user_interface.wrappers;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;

import java.util.Locale;

/**
 * Created by Dmitry on 23.01.2023.
 */

public class LocaleContextWrapper extends ContextWrapper {
    public LocaleContextWrapper(Context base) {
        super(base);
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static ContextWrapper wrap(Context context, Locale newLocale) {
        Resources res = context.getResources();
        Configuration configuration = res.getConfiguration();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            configuration.setLocale(newLocale);

            LocaleList localeList = new LocaleList(newLocale);
            LocaleList.setDefault(localeList);
            configuration.setLocales(localeList);

            context = context.createConfigurationContext(configuration);

        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(newLocale);
            context = context.createConfigurationContext(configuration);
        } else {
            configuration.locale = newLocale;
            res.updateConfiguration(configuration, res.getDisplayMetrics());
        }

        return new ContextWrapper(context);
    }
}
