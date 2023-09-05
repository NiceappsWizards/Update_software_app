package com.example.updatesoftwares.Inter_Splash.Inter_Splash;

public class InterstitialClosedListenerImplementerMasterSplash {
    static InterstitialClosedListenerMasterSplash closedListenerMaster;

    public static void setOnInterstitialClosedMaster(InterstitialClosedListenerMasterSplash mClosedListenerMaster) {
        closedListenerMaster = mClosedListenerMaster;
    }

    public static void onInterstitialClosedCalled() {
        closedListenerMaster.onInterstitialClosed();
    }

    public static void onInterstitialFailedToShowCalled() {
        closedListenerMaster.onInterstitialFailedToShow();
    }


}

