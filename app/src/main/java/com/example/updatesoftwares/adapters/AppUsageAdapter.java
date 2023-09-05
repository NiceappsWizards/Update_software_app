package com.example.updatesoftwares.adapters;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.updatesoftwares.R;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

import bot.box.appusage.model.AppData;
import bot.box.appusage.utils.UsageUtils;

public class AppUsageAdapter extends RecyclerView.Adapter<AppUsageAdapter.MyViewHolder> {
    ArrayList<AppData> list = new ArrayList<>();
    Context context;

    public AppUsageAdapter(ArrayList<AppData> list, Context context) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_app_usae_layout, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //load icon
        Glide.with(context)
                .load(UsageUtils.parsePackageIcon(list.get(position).mPackageName, R.mipmap.ic_launcher)).
                transition(new DrawableTransitionOptions().crossFade())
                .into(holder.img_app_icon);
        //pakigName

        //usage time
        holder.txt_usage_time.setText(UsageUtils.humanReadableMillis(list.get(position).mUsageTime));
        //total lunch
        final PackageManager pm = context.getPackageManager();
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = pm.getApplicationInfo(list.get(position).mPackageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String appName = applicationInfo.loadLabel(context.getPackageManager()).toString();
        File file = new File(applicationInfo.publicSourceDir);
        int size = (int) file.length();
        holder.appNAme.setText(appName);
        holder.txt_lunch.setText(" Lunch :" + list.get(position).mCount + "       \n Size :" + getStringSizeLengthFile(size));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSetting(position);
            }
        });

    }

    public static String getStringSizeLengthFile(int size) {

        DecimalFormat df = new DecimalFormat("0.00");

        float sizeKb = 1024.0f;
        float sizeMb = sizeKb * sizeKb;
        float sizeGb = sizeMb * sizeKb;
        float sizeTerra = sizeGb * sizeKb;


        if (size < sizeMb)
            return df.format(size / sizeKb) + " Kb";
        else if (size < sizeGb)
            return df.format(size / sizeMb) + " Mb";
        else if (size < sizeTerra)
            return df.format(size / sizeGb) + " Gb";

        return "";
    }

    private void openSetting(int position) {

        try {
            //Open the specific App Info page:
            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + list.get(position).mPackageName));
            context.startActivity(intent);

        } catch (ActivityNotFoundException e) {
            //e.printStackTrace();

            //Open the generic Apps page:
            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
            context.startActivity(intent);

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView appNAme;
        ImageView img_app_icon;
        TextView txt_usage_time;
        TextView txt_lunch;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img_app_icon = itemView.findViewById(R.id.img_app_icon);
            txt_usage_time = itemView.findViewById(R.id.txt_usage_time);
            txt_lunch = itemView.findViewById(R.id.txt_lunch);
            appNAme =  itemView.findViewById(R.id.appname);
        }
    }
}
