package org.ituns.framework.master.modules.develop.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.v3.InputDialog;
import com.kongzue.dialog.v3.MessageDialog;

import org.ituns.framework.master.R;
import org.ituns.framework.master.modules.develop.activity.DevelopActivity;
import org.ituns.framework.master.modules.develop.adapter.EnvDetailAdapter;
import org.ituns.framework.master.modules.develop.viewitem.EnvDetailItem;
import org.ituns.framework.master.mvvm.action.MVVMAction;
import org.ituns.framework.master.mvvm.fragment.MVVMFragment;
import org.ituns.framework.master.mvvm.viewitem.DividerItem;
import org.ituns.framework.master.mvvm.viewitem.MVVMItem;
import org.ituns.framework.master.service.channel.toast.ToastProxy;
import org.ituns.framework.master.service.env.EnvProxy;
import org.ituns.framework.master.service.env.EnvService;
import org.ituns.framework.master.service.env.Environment;
import org.ituns.framework.master.service.tasks.MainProxy;
import org.ituns.framework.master.tools.android.IBundle;
import org.ituns.framework.master.tools.java.IList;
import org.ituns.framework.master.tools.java.IString;

import java.util.ArrayList;
import java.util.List;

public class EnvDetailFragment extends MVVMFragment<MVVMItem> {
    private String key = "";
    private String name = "";
    private EnvDetailAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fw_fragement_env_detail, container, false);
        initializeData();
        initializeTitle(rootView.findViewById(R.id.title));
        return rootView;
    }

    private void initializeData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            key = bundle.getString("key", "");
            name = bundle.getString("name", "");
        }
    }

    private void initializeTitle(LinearLayout titleLayout) {
        Activity activity = requireActivity();
        if (activity instanceof DevelopActivity) {
            ((DevelopActivity) activity).onTitleInit(titleLayout);
            ((DevelopActivity) activity).onTitleText(name);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new EnvDetailAdapter();
        injectAdapter(adapter);
        RecyclerView recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        refreshEnvData(key);
    }

    private void refreshEnvData(String key) {
        Activity activity = requireActivity();
        if (activity instanceof DevelopActivity) {
            ((DevelopActivity) activity).requestEnv(key, env -> {
                MainProxy.post(() -> refreshEnvView(key, env));
            });
        } else {
            refreshEnvView(key, null);
        }
    }

    private void refreshEnvView(String key, Environment remote) {
        ArrayList<MVVMItem> items = new ArrayList<>();
        items.addAll(buildCurrentEnv(key));
        items.addAll(buildLocalEnv(key));
        items.addAll(buildRemoteEnv(key, remote));
        adapter.setData(items);
    }

    private List<MVVMItem> buildCurrentEnv(String key) {
        int color = Color.parseColor("#E5E5E5");
        ArrayList<MVVMItem> items = new ArrayList<>();
        items.add(EnvDetailItem.title("当前配置"));
        items.add(DividerItem.divide(0.5f, color));
        items.add(EnvDetailItem.current(key, EnvProxy.getEnv(key)));
        items.add(DividerItem.divide(0.5f, color));
        return items;
    }

    private List<MVVMItem> buildLocalEnv(String key) {
        int color = Color.parseColor("#E5E5E5");
        ArrayList<MVVMItem> items = new ArrayList<>();
        Environment env = EnvService.get().getLocalEnv(key);
        if (env != null && IList.notEmpty(env.getConfigs())) {
            items.add(EnvDetailItem.title("本地配置"));
            items.add(DividerItem.divide(0.5f, color));
            for (Environment.Config config : env.getConfigs()) {
                if(items.size() > 2) {
                    items.add(DividerItem.divide(0.5f, color, 15f).backColor(Color.WHITE));
                }
                items.add(EnvDetailItem.config(key, config));
            }
            items.add(DividerItem.divide(0.5f, color));
        }
        return items;
    }

    private List<MVVMItem> buildRemoteEnv(String key, Environment env) {
        int color = Color.parseColor("#E5E5E5");
        ArrayList<MVVMItem> items = new ArrayList<>();
        if (env != null && IList.notEmpty(env.getConfigs())) {
            items.add(EnvDetailItem.title("远程配置"));
            items.add(DividerItem.divide(0.5f, color));
            for (Environment.Config config : env.getConfigs()) {
                if(items.size() > 2) {
                    items.add(DividerItem.divide(0.5f, color, 15f).backColor(Color.WHITE));
                }
                items.add(EnvDetailItem.config(key, config));
            }
            items.add(DividerItem.divide(0.5f, color));
        }
        return items;
    }

    @Override
    protected void onAdapter(MVVMAction action) {
        super.onAdapter(action);
        switch (action.code()) {
            case EnvDetailItem.ACTION_CONFIG: {
                showChangeEnvDialog(action);
                break;
            }
            case EnvDetailItem.ACTION_CURRENT: {
                showCurrentEnvDialog(action);
                break;
            }
        }
    }

    private void showChangeEnvDialog(IBundle bundle) {
        String key = bundle.getString("key", "");
        String name = bundle.getString("name", "");
        String value = bundle.getString("value", "");
        if(IString.isEmpty(key, name, value)) {
            ToastProxy.show("不合法的参数");
            return;
        }

        String message = String.format("切换<%s>为：%s", EnvDetailFragment.this.name, name);
        MessageDialog.build((AppCompatActivity) requireActivity())
                .setStyle(DialogSettings.STYLE.STYLE_IOS)
                .setCancelable(false)
                .setTitle("提示")
                .setMessage(message)
                .setCancelButton("放弃")
                .setOkButton("确定", ((baseDialog, v) -> {
                    chanceEnvironment(key, value);
                    return false;
                }))
                .show();
    }

    private void showCurrentEnvDialog(IBundle bundle) {
        String key = bundle.getString("key", "");
        String value = bundle.getString("value", "");
        if(IString.isEmpty(key, value)) {
            ToastProxy.show("不合法的参数");
            return;
        }

        InputDialog.build((AppCompatActivity) requireActivity())
                .setStyle(DialogSettings.STYLE.STYLE_IOS)
                .setCancelable(false)
                .setTitle("提示")
                .setMessage("请输入" + name)
                .setInputText(value)
                .setCancelButton("放弃")
                .setOkButton("确定", (baseDialog, v, text) -> {
                    chanceEnvironment(key, text);
                    return false;
                })
                .show();
    }

    private void chanceEnvironment(String key, String value) {
        EnvService.get().setEnv(key, value);
        Activity activity = requireActivity();
        if (activity instanceof DevelopActivity) {
            ((DevelopActivity) activity).onEnvChanged(key, value);
        }
        refreshEnvData(key);
    }
}
