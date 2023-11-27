package com.example.recordingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.recordingapp.databinding.ActivityMainBinding;
import com.example.recordingapp.utils.PermissiononUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        // 接收到的信息
        public boolean handleMessage(@NonNull Message message) {
            if (message.what == 1) {
                time--;
                if (time == 0) {
                    startActivity(new Intent(MainActivity.this, AudioListActivity.class));
                    finish();
                } else {
                    binding.mainTv.setText(time + "");
                    handler.sendEmptyMessageDelayed(1, 1000);
                }
            }
            return false;
        }
    });
    // 倒计时时间
    private int time = 3;
    // 申请的状态权限
    String []permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.mainTv.setText(time + "");
        PermissiononUtils.getInstance().onRequestPermission(this,permissions, listener);
    }
    PermissiononUtils.OnPermissionCallbackListener listener = new PermissiononUtils.OnPermissionCallbackListener() {
        @Override
        public void onGranted() {
            // 判断是否有应用文件夹，如果没有就创建应用文件夹
            // 倒计时进入播放录音页面
            Log.d("倒计时", "onGranted: ");
            handler.sendEmptyMessageDelayed(1, 1000);
        }

        @Override
        public void onDenied(List<String> deniedPermissions) {
            Log.d("有请求未通过", "onDenied: ");
            PermissiononUtils.getInstance().showDialogTipUserGotoAppSetting(MainActivity.this);
        }
    };

    @Override
    // 查看申请权限结果
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("主函数中的onrequestPermissionResult被调用了", "onRequestPermissionsResult: ");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissiononUtils.getInstance().onRequestPermissionResult(this, requestCode, permissions, grantResults);
    }
}