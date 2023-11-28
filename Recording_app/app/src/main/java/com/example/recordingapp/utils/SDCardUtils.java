package com.example.recordingapp.utils;

import android.os.Environment;
import android.util.Log;

import com.example.recordingapp.Contents;

import java.io.File;

// SD卡工具类
public class SDCardUtils {
    private SDCardUtils() {

    }

    // 唯一静态私有对象
    private static SDCardUtils sdCardUtils;
    public static SDCardUtils getInstance() {
        if (sdCardUtils == null) {
            synchronized (SDCardUtils.class) {
                if (sdCardUtils == null) {
                    sdCardUtils = new SDCardUtils();
                }
            }
        }
        return sdCardUtils;
    }

    // 判断当前手机是否拥有sd卡
    public boolean isHaveSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    // 创建项目的公共目录
    public File createAppPublicDir() {
        if (isHaveSDCard()) {
            // 得到SD卡地址
            File sdDir = Environment.getExternalStorageDirectory();

            // 从SD卡目录中找是否有app文件夹
            File appDir = new File(sdDir, IFileInter.APP_DIR);

            // 判断文件夹是否存在
            if (!appDir.exists()) {
                // 如果不存在就创建该文件夹
                Log.d(appDir.getAbsolutePath(), "createAppPublicDir: ");
                appDir.mkdir();
            }

            // 把app的地址存放到contents里
            Contents.PATH_APP_DIR = appDir.getAbsolutePath();

            return appDir;
        }
        return null;
    }

    // 创建项目分支目录
    public File createAppFetchDir(String dir) {
        // 得到项目地址对象
        File publicDir = createAppPublicDir();

        if (publicDir != null) {
            File fetchDir = new File(publicDir, dir);

            // 判断项目文件夹中是否存在分支目录，如果不存在，创建分支目录
            if (!fetchDir.exists()) {
                Log.d("创建分支目录", "createAppFetchDir: ");
                fetchDir.mkdir();
            }
            return fetchDir;
        }
        return null;
    }

}
