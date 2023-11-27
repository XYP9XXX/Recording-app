package com.example.recordingapp.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.recordingapp.StartSystemPageUtils;

import java.util.ArrayList;
import java.util.List;

// 动态申请权限工具类
public class PermissiononUtils {
    // 内存中唯一的对象
    private static PermissiononUtils permissiononUtils;
    //权限请求码
    private final int mRequestCode = 100;
    //接口
    public interface OnPermissionCallbackListener {
        void onGranted(); //表示授权会执行的方法
        void onDenied(List<String>deniedPermissions); // 未被授权
    }
    private OnPermissionCallbackListener mListener;
    private PermissiononUtils () {}

    public static PermissiononUtils getInstance() {
        if (permissiononUtils == null) {
            synchronized (PermissiononUtils.class) { // 线程安全
                if (permissiononUtils == null) {
                    permissiononUtils = new PermissiononUtils();
                }
            }
        }
        return permissiononUtils;
    }

    public void onRequestPermission (Activity context, String []permissions, OnPermissionCallbackListener listener) {
        Log.d("onRequestPermission被调用了", "onRequestPermission: ");
        mListener = listener;
        // 判断手机版本号是否大于6.0, 6.0以上需要申请权限
        if (Build.VERSION.SDK_INT >= 23) {
            // 创建一个集合，将用户之前没有授予的权限放入这个集合
            List<String>mPermissionList = new ArrayList<>();

            // 诸葛判断权限是否授权
            for (int i = 0; i < permissions.length; i++) {
                // 检查权限是否授权
                int res = ContextCompat.checkSelfPermission(context, permissions[i]);

                // 判断返回值
                if (res != PackageManager.PERMISSION_GRANTED) {
                    mPermissionList.add(permissions[i]);
                }
            }
            // 申请权限
            if (mPermissionList.size() > 0) {
                String[] permission_arr = mPermissionList.toArray(new String[mPermissionList.size()]);

                // Activity申请权限方法
                ActivityCompat.requestPermissions(context, permission_arr, mRequestCode);
            }else {
                // 权限通过，可以做想做的事情了
                mListener.onGranted();
            }
        }
    }

    public void onRequestPermissionResult(Activity context, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("onRequestPermissionResult被调用了", "onRequestPermissionResult: ");
        if (requestCode == mRequestCode) {
            // 存储没有授权的集合
            List<String>deniedPermissions = new ArrayList<>();

            // grantResults代表申请权限结果
            if (grantResults.length > 0) {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        deniedPermissions.add(permissions[i]);
                    }
                }
            }
            if (deniedPermissions.size() == 0) {
                mListener.onGranted();
            } else {
                mListener.onDenied(deniedPermissions);
            }
        } else {
            // 所有权限都接受了
            mListener.onGranted();
        }
    }

    // 提示用户去应用设置页面手动开启权限

    public void showDialogTipUserGotoAppSetting(Activity context) {
        DialogUtils.showNormalDialog(context, "提示信息", "已经禁用权限，请手动开启", "取消", new DialogUtils.OnLeftClickListener() {
            @Override
            public void onLeftClick() {
                context.finish();
            }
        }, "确认", new DialogUtils.OnRightClickListener() {
            @Override
            public void onRightClick() {
                StartSystemPageUtils.goToAppSetting(context);
                context.finish();
            }
        });
    }
}
