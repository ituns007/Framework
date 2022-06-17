package org.ituns.framework.master.mvvm.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.SparseArray;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.v3.MessageDialog;

import org.ituns.framework.master.mvvm.action.MVVMAction;
import org.ituns.framework.master.service.ServiceManager;
import org.ituns.framework.master.tools.android.IBundle;
import org.ituns.framework.master.tools.java.IString;

import java.util.Arrays;
import java.util.List;

public abstract class MasterFragment extends Fragment {
    protected final IBundle intents = new IBundle();
    protected final IBundle arguments = new IBundle();
    private final SparseArray<String[]> requestPermissions = new SparseArray<>();
    private final MutableLiveData<MVVMAction> lifecycleLiveData = new MutableLiveData<>();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        injectData();
    }

    @Override
    @CallSuper
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handleOnBackPressed();
        lifecycleLiveData.observe(getViewLifecycleOwner(), this::lifecycleObserver);
    }

    @Override
    @CallSuper
    public void onResume() {
        super.onResume();
        ServiceManager.get().fragmentResume(this);
    }

    @Override
    @CallSuper
    public void onPause() {
        ServiceManager.get().fragmentPause(this);
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        lifecycleLiveData.removeObservers(getViewLifecycleOwner());
    }

    /**
     * 注入intent数据
     */
    private void injectData() {
        Intent intent = requireActivity().getIntent();
        if(intent != null) {
            intents.putAll(intent.getExtras());
        }
        arguments.putAll(getArguments());
    }

    /**
     * 返回键控制
     */
    private void handleOnBackPressed() {
        OnBackPressedDispatcher dispatcher = requireActivity().getOnBackPressedDispatcher();
        dispatcher.addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(backEnable()) {
            @Override
            public void handleOnBackPressed() {
                pressBack();
            }
        });
    }
    protected boolean backEnable() {
        return true;
    }
    protected void pressBack() {
        pressBack(null);
    }
    protected void pressBack(String tag) {
        if(!pressBackWithNavController()) {
            FragmentManager manager = getParentFragmentManager();
            if(manager.getBackStackEntryCount() > 1) {
                manager.popBackStack(tag, 0);
            } else {
                requireActivity().finish();
            }
        }
    }
    private boolean pressBackWithNavController() {
        try {
            NavController controller = NavHostFragment.findNavController(this);
            return controller.popBackStack();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 生命周期调用
     * */
    private void lifecycleObserver(MVVMAction action) {
        if(action != null) {
            lifecycleLiveData.postValue(null);
            onLifecycle(action);
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
        requestPermissions(permissions, requestCode);
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
            int result = ActivityCompat.checkSelfPermission(requireActivity(), permission);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // requestCode 对应的申请权限数组不存在，可能是由 Fragment 发起的权限申请
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
        MessageDialog.build((AppCompatActivity) requireActivity())
                .setStyle(DialogSettings.STYLE.STYLE_IOS)
                .setCancelable(false)
                .setMessage("请到设置页面手动开启权限")
                .setCancelButton("取消", (dialog, v) -> {
                    pressBack();
                    return false;
                })
                .setOkButton("设置", (dialog, v) -> {
                    loadSettingActivity();
                    return false;
                })
                .show();
    }
    private void loadSettingActivity() {
        Uri packageURI = Uri.parse("package:" + requireActivity().getPackageName());
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
        startActivity(intent);
    }
}
