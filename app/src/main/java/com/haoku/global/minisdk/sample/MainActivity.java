package com.haoku.global.minisdk.sample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.haoku.global.minisdk.BannerAdSize;
import com.haoku.global.minisdk.HaoKuGlobalAds;
import com.haoku.global.minisdk.OnBannerAdLoadListener;
import com.haoku.global.minisdk.OnRewardedListener;


public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

    private FrameLayout mBannerAdContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBannerAdContainer = findViewById(R.id.banner_ad_container);

        HaoKuGlobalAds.requestPermissions(this);
        HaoKuGlobalAds.onMainActivityCreated(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        HaoKuGlobalAds.onMainActivityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        HaoKuGlobalAds.onMainActivityPaused();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HaoKuGlobalAds.onMainActivityDestroyed();
    }

    @Override
    public void onBackPressed() {
        int theme = Build.VERSION.SDK_INT >= 21 ? android.R.style.Theme_Material_Light_Dialog_Alert :
                android.R.style.Theme_Holo_Light_Dialog;
        new AlertDialog.Builder(this, theme)
                .setMessage("是否退出游戏？")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.finish();
                        System.exit(0);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        HaoKuGlobalAds.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void showRewardVideoAd(View view) {
        if (!HaoKuGlobalAds.isRewardVideoAdReady()) {
            Toast.makeText(this, "没有奖励视频广告", Toast.LENGTH_SHORT).show();
            return;
        }

        HaoKuGlobalAds.showRewardVideoAd(0, new OnRewardedListener() {
            @Override
            public void onRewarded() {
                Log.d(TAG, "onRewarded: 发放奖励");
            }
        });

    }

    public void showInterstitialAd(View view) {
        HaoKuGlobalAds.showInterstitialAd();
    }

    public void loadBannerAd(View view) {
        HaoKuGlobalAds.loadBannerAd(BannerAdSize.MEDIUM, new OnBannerAdLoadListener() {
            @Override
            public void onBannerAdLoadError(String error) {
                Log.e(TAG, "onBannerAdLoadError: " + error);
            }

            @Override
            public void onBannerAdLoadSuccess(View adView) {
                mBannerAdContainer.removeAllViews();
                mBannerAdContainer.addView(adView);
            }

        });
    }
}
