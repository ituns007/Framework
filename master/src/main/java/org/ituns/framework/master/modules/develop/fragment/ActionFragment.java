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
import org.ituns.framework.master.modules.develop.adapter.ActionAdapter;
import org.ituns.framework.master.modules.develop.viewitem.ActionItem;
import org.ituns.framework.master.mvvm.action.MVVMAction;
import org.ituns.framework.master.mvvm.fragment.MVVMFragment;
import org.ituns.framework.master.mvvm.viewitem.DividerItem;
import org.ituns.framework.master.mvvm.viewitem.MVVMItem;

import java.util.ArrayList;
import java.util.List;

public class ActionFragment extends MVVMFragment<MVVMItem> {
    private ActionAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fw_fragement_action, container, false);
        initializeTitle(rootView.findViewById(R.id.title));
        return rootView;
    }

    private void initializeTitle(LinearLayout titleLayout) {
        Activity activity = requireActivity();
        if(activity instanceof DevelopActivity) {
            ((DevelopActivity) activity).onTitleInit(titleLayout);
            ((DevelopActivity) activity).onTitleText("开发者选项");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new ActionAdapter();
        injectAdapter(adapter);
        RecyclerView recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter.setData(buildActions());
    }

    private List<MVVMItem> buildActions() {
        int color = Color.parseColor("#E5E5E5");
        ArrayList<MVVMItem> items = new ArrayList<>();
        items.add(DividerItem.divide(15));
        items.add(DividerItem.divide(0.5f, color));
        items.add(ActionItem.env());
        items.add(DividerItem.divide(0.5f, color, 15.0f));
        items.add(ActionItem.clean());
        items.add(DividerItem.divide(0.5f, color));
        return items;
    }

    @Override
    protected void onAdapter(MVVMAction action) {
        super.onAdapter(action);
        switch (action.code()) {
            case ActionItem.ACTION_ENV: {
                NavHostFragment.findNavController(this).navigate(R.id.envCollectFragment);
                break;
            }
            case ActionItem.ACTION_CLEAN: {
                NavHostFragment.findNavController(this).navigate(R.id.cleanFragment);
                break;
            }
        }
    }
}