package org.ituns.framework.master.mvvm.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.SparseArray;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;

import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.v3.MessageDialog;

import org.ituns.framework.master.mvvm.action.MVVMAction;
import org.ituns.framework.master.service.ServiceManager;
import org.ituns.framework.master.tools.android.IBundle;
import org.ituns.framework.master.tools.java.IString;

import java.util.Arrays;
import java.util.List;

public class MasterActivity extends AppCompatActivity {
    protected final IBundle intents = new IBundle();
    private final SparseArray<String[]> requestPermissions = new SparseArray<>();
    private final MutableLiveData<MVVMAction> lifecycleLiveData = new MutableLiveData<>();

    @CallSuper
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectData();
        ServiceManager.get().activityCreate(this);
        lifecycleLiveData.observe(this, this::lifecycleObserver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ServiceManager.get().activityStart(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ServiceManager.get().activityResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ServiceManager.get().activityPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ServiceManager.get().activityStop(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lifecycleLiveData.removeObservers(this);
        ServiceManager.get().activityDestroy(this);
    }

    /**
     * 注入intent数据
     */
    private void injectData() {
        Intent intent = getIntent();
        if(intent != null) {
            intents.putAll(intent.getExtras());
        }
    }

    /**
     * 生命周期调用
     * */
    private void lifecycleObserver(MVVMAction action) {
        if(action != null) {
            onLifecycle(action);
            lifecycleLiveData.postValue(null);
        }
    }
    protected final void postLifecycle(MVVMAction action) {
        lifecycleLiveData.postValue(action);
    }
    protected void onLifecycle(MVVMAction action) {}

    /**
     * 权限申请
     * */
    protected final void checkPermissions(int requestCode, String[] permissions) {
        if(isPermissionsGranted(permissions)) {
            onPermissionGrant(requestCode);
            return;
        }
        requestPermissions.put(requestCode, permissions);
        ActivityCompat.requestPermissions(this, permissions, requestCode);
    }
    protected final boolean isPermissionsGranted(String[] permissions) {
        for(String permission : permissions) {
            if(!isPermissionGranted(permission)) {
                return false;
            }
        }
        return true;
    }
    protected final boolean isPermissionGranted(String permission) {
        if(IString.notEmpty(permission)) {
            int result = ActivityCompat.checkSelfPermission(this, permission);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // requestCode 对应的申请权限记录不存在，可能是由 Fragment 发起的权限申请
        String[] requestPermissions = this.requestPermissions.get(requestCode);
        if(requestPermissions == null) {
            onSystemPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        // 移除 requestCode 对应的权限申请记录
        this.requestPermissions.remove(requestCode);

        // 多次申请权限的时间间隔太小，可能会返回空数组
        if(permissions.length == 0 || grantResults.length == 0) {
            onPermissionError(requestCode);
            return;
        }

        int weight = 0;
        int length = Math.min(permissions.length, grantResults.length);
        List<String> permissionList = Arrays.asList(requestPermissions);
        for(int i = 0; i < length; i++) {
            if(permissionList.contains(permissions[i])) {
                if(grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    weight++;
                }
            }
        }

        if(weight < requestPermissions.length) {
            onPermissionDenied(requestCode);
        } else {
            onPermissionGrant(requestCode);
        }
    }
    protected void onSystemPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {}
    protected void onPermissionError(int requestCode) {}
    protected void onPermissionGrant(int requestCode) {}
    protected void onPermissionDenied(int requestCode) {
        showDeniedDialog((dialog, v) -> {
            finish();
            return false;
        });
    }
    protected void showDeniedDialog(OnDialogButtonClickListener listener) {
        MessageDialog.build(this)
                .setStyle(DialogSettings.STYLE.STYLE_IOS)
                .setCancelable(false)
                .setMessage("请到设置页面手动开启权限")
                .setCancelButton("取消", (dialog, v) -> {
                    dialog.doDismiss();
                    if(listener != null) {
                        listener.onClick(dialog, v);
                    }
                    return true;
                })
                .setOkButton("设置", (dialog, v) -> {
                    dialog.doDismiss();
                    loadSettingActivity();
                    return true;
                })
                .show();
    }
    private void loadSettingActivity() {
        Uri packageURI = Uri.parse("package:" + getPackageName());
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
        startActivity(intent);
    }
}
