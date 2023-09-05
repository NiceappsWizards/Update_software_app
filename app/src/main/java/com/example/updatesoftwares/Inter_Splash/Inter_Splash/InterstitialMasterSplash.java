package com.example.updatesoftwares.Inter_Splash.Inter_Splash;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class InterstitialMasterSplash {

    public static InterstitialAd mInterstitialAd;
    public static boolean isAlreadyLoaded = false;
    static Context mContext;
    static String interstitial_id;
    static String logTag = "Ads_";

    public static void load_interstitial(Context your_activity_context, String your_interstitial_id) {

        mContext = your_activity_context;
        interstitial_id = your_interstitial_id;

        if (!isAlreadyLoaded) {

            Log.d(logTag, "Interstitial Load Request Sent.");
            AdRequest adRequest_interstitial = new AdRequest.Builder().build();

            InterstitialAd.load(mContext, your_interstitial_id, adRequest_interstitial,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            // The mInterstitialAd reference will be null until
                            // an ad is loaded.
                            mInterstitialAd = interstitialAd;
                            Log.d(logTag, "Insterstitial Loaded.");
                            isAlreadyLoaded = true;
                            InterstitialLoadedListenerImplementerMasterSplash.onInterstitialLoadedCalled();

                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error
                            Log.d(logTag, "Interstitial Failed to Load." + loadAdError.getMessage());
                            mInterstitialAd = null;
                            isAlreadyLoaded = false;
                            InterstitialLoadedListenerImplementerMasterSplash.onInterstitialFailedCalled();
                        }
                    });
        } else {
            Log.d(logTag, "Interstitial Already Loaded. Request not Sent.");
        }

    }


    public static void show_insterstitial(Activity yourActivity) {

        if (isAlreadyLoaded) {
            mInterstitialAd.show(yourActivity);
            isAlreadyLoaded = false;
        load_interstitial(mContext, interstitial_id);
            Log.d(logTag, "Interstitial Shown.");
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();

                    InterstitialClosedListenerImplementerMasterSplash.onInterstitialClosedCalled();
                    Log.d(logTag, "Interstitial Closed.");

                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);

                    InterstitialClosedListenerImplementerMasterSplash.onInterstitialFailedToShowCalled();
                    Log.d(logTag, "Interstitial Closed.");

                }
            });
        } else {
            Log.d(logTag, "Interstitial was not Loaded.");
            isAlreadyLoaded = false;
            /*   load_interstitial(mContext, interstitial_id);*/
        }
    }

    public static void show_insterstitial_fragnment(Context context) {

        if (isAlreadyLoaded) {
            mInterstitialAd.show((Activity) context);
            isAlreadyLoaded = false;
            load_interstitial(mContext, interstitial_id);
            Log.d(logTag, "Interstitial Shown.");
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();

                    InterstitialClosedListenerImplementerMasterSplash.onInterstitialClosedCalled();
                    Log.d(logTag, "Interstitial Closed.");

                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);

                    InterstitialClosedListenerImplementerMasterSplash.onInterstitialFailedToShowCalled();
                    Log.d(logTag, "Interstitial Closed.");

                }
            });
        } else {
            Log.d(logTag, "Interstitial was not Loaded.");
            isAlreadyLoaded = false;
            load_interstitial(mContext, interstitial_id);
        }
    }

    public static boolean isConnected() {

        boolean result = false;
        try {
            String command = "ping -c 1 google.com";
            result = Runtime.getRuntime().exec(command).waitFor() == 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
