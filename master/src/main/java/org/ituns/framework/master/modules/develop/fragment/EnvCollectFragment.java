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
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.ituns.framework.master.R;
import org.ituns.framework.master.modules.develop.activity.DevelopActivity;
import org.ituns.framework.master.modules.develop.adapter.EnvCollectAdapter;
import org.ituns.framework.master.modules.develop.viewitem.EnvCollectItem;
import org.ituns.framework.master.mvvm.action.MVVMAction;
import org.ituns.framework.master.mvvm.fragment.MVVMFragment;
import org.ituns.framework.master.mvvm.viewitem.DividerItem;
import org.ituns.framework.master.mvvm.viewitem.MVVMItem;
import org.ituns.framework.master.service.env.EnvService;
import org.ituns.framework.master.service.env.Environment;
import org.ituns.framework.master.tools.java.IString;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EnvCollectFragment extends MVVMFragment<MVVMItem> {
    private EnvCollectAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fw_fragement_env_collect, container, false);
        initializeTitle(rootView.findViewById(R.id.title));
        return rootView;
    }

    private void initializeTitle(LinearLayout titleLayout) {
        Activity activity = requireActivity();
        if(activity instanceof DevelopActivity) {
            ((DevelopActivity) activity).onTitleInit(titleLayout);
            ((DevelopActivity) activity).onTitleText("环境配置");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new EnvCollectAdapter();
        injectAdapter(adapter);
        RecyclerView recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.setData(buildEnvItem());
    }

    private List<MVVMItem> buildEnvItem() {
        Set<String> keys = EnvService.get().getDefKey();
        if(keys == null || keys.isEmpty()) {
            return new ArrayList<>();
        }

        int color = Color.parseColor("#E5E5E5");
        ArrayList<MVVMItem> items = new ArrayList<>();
        items.add(DividerItem.divide(15));
        items.add(DividerItem.divide(0.5f, color));
        for(String key : keys) {
            if(IString.isEmpty(key)) {
                continue;
            }

            Environment env = EnvService.get().getLocalEnv(key);
            if(env == null || IString.isEmpty(env.getName())) {
                continue;
            }

            if(items.size() > 2) {
                items.add(DividerItem.divide(0.5f, color, 15f).backColor(Color.WHITE));
            }
            items.add(EnvCollectItem.item(key, env.getName()));
        }
        items.add(DividerItem.divide(0.5f, color));
        return items;
    }

    @Override
    protected void onAdapter(MVVMAction action) {
        super.onAdapter(action);
        switch (action.code()) {
            case EnvCollectItem.ACTION_ITEM: {
                NavHostFragment.findNavController(this).navigate(R.id.envDetailFragment, action.bundle());
                break;
            }
        }
    }
}
