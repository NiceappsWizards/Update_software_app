package com.example.updatesoftwares.Inter_Splash.Inter_Splash;

public class InterstitialLoadedListenerImplementerMasterSplash {

    static InterstitialLoadedListenerMasterSplash loadedListenerMaster;

    public static void setOnInterstitialLoadedMaster(InterstitialLoadedListenerMasterSplash mLoadedListenerMaster) {
        loadedListenerMaster = mLoadedListenerMaster;
    }

    public static void onInterstitialLoadedCalled() {
        loadedListenerMaster.onInterstitialLoaded();
    }

    public static void onInterstitialFailedCalled() {
        loadedListenerMaster.onInterstitialFailed();
    }


}
