package com.example.recordingapp;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

// 跳转系统的相关页面
public class StartSystemPageUtils {


    // 跳转到系统中当前应用的设置页面
    public static void goToAppSetting (Activity context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }
}
