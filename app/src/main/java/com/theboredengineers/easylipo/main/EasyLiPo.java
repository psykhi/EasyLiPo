package com.theboredengineers.easylipo.main;

import android.app.Application;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.theboredengineers.easylipo.security.AuthManager;

/**
 * Created by Alex on 14/06/2015.
 */
public class EasyLiPo extends Application {
    public static GoogleAnalytics analytics;
    public static Tracker tracker;
    private AuthManager sessionManager = AuthManager.getInstance();

    @Override
    public void onCreate() {
        analytics = GoogleAnalytics.getInstance(this);
        analytics.setLocalDispatchPeriod(1800);

        tracker = analytics.newTracker("UA-64101522-1");
        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);

        sessionManager.setContext(this);

    }
}
