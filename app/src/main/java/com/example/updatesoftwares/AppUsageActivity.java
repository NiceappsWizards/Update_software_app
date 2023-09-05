package com.example.updatesoftwares;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.updatesoftwares.adapters.AppUsageAdapter;
import com.example.updatesoftwares.utils.MyAppUtils;

import java.util.ArrayList;
import java.util.List;
import bot.box.appusage.contract.UsageContracts;
import bot.box.appusage.handler.Monitor;
import bot.box.appusage.model.AppData;
import bot.box.appusage.utils.Duration;


public class AppUsageActivity extends AppCompatActivity implements UsageContracts.View, AdapterView.OnItemSelectedListener {
    Spinner spinner;
    ImageView imgback;
    RecyclerView myrecyler;
    int onResumeCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_uage);
        MyAppUtils.INSTANCE.setStatusBarColor(this,R.color.bgcolor,R.color.white);
        spinner = findViewById(R.id.spinner);
        imgback = findViewById(R.id.imgback);
        myrecyler = findViewById(R.id.recycler);
        myrecyler.setLayoutManager(new LinearLayoutManager(this));
        init();
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (!isAccessGranted()) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }
    }

    //set sppiner
    private void init() {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                R.layout.simple_list_spinner, getResources().getStringArray(R.array.duration));

        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(0);
        //spinner.setPopupBackgroundDrawable(getDrawable(R.drawable.popupbg));
        spinner.setPopupBackgroundResource(R.drawable.spinnerpopup);
        spinner.setGravity(Gravity.CENTER);
        spinner.setOnItemSelectedListener(this);
    }

    //check permission
    private boolean isAccessGranted() {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(this.getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode = 0;
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                        applicationInfo.uid, applicationInfo.packageName);
            }
            return (mode == AppOpsManager.MODE_ALLOWED);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onResume() {
        super.onResume();
        if (Monitor.hasUsagePermission()) {
            Monitor.scan().getAppLists(this).fetchFor(Duration.TODAY);
        } else {
            if (onResumeCount >= 1) {
                finish();
                return;
            }
            onResumeCount++;
            Monitor.requestUsagePermission();
        }
    }

    //override method for get app usage data
    @Override
    public void getUsageData(List<AppData> usageData, long mTotalUsage, int duration) {
        if (usageData.size() > 0) {
            //call adapter for populate list
            myrecyler.setAdapter(new AppUsageAdapter((ArrayList<AppData>) usageData, this));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
       //((TextView) adapterView.getChildAt(0)).setGravity(Gravity.CENTER);
        Monitor.scan().getAppLists(this).fetchFor(i);
    }



    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

}