package org.ituns.framework.master.mvvm.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import org.ituns.framework.master.mvvm.action.MVVMAction;
import org.ituns.framework.master.mvvm.activity.MediaActivity;
import org.ituns.framework.master.mvvm.adapter.MVVMAdapter;
import org.ituns.framework.master.mvvm.lifecycle.ApplicationOwner;
import org.ituns.framework.master.mvvm.viewmodel.MVVMVm;
import org.ituns.framework.master.service.channel.loading.LoadingProxy;
import org.ituns.framework.master.service.channel.toast.ToastProxy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class MVVMFragment<T> extends MediaFragment {
    private final ArrayList<MVVMVm<T>> vmList = new ArrayList<>();
    private final ArrayList<MVVMAdapter<?>> adapterList = new ArrayList<>();

    @Override
    @CallSuper
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releaseAdapter();
        releaseVm();
    }

    protected final <VM extends MVVMVm<T>> VM injectFragmentVm(Class<VM> clazz) {
        return injectFragmentVm(new ViewModelProvider.NewInstanceFactory(), clazz);
    }

    protected final <VM extends MVVMVm<T>> VM injectFragmentVm(ViewModelProvider.Factory factory,
                                                               Class<VM> clazz) {
        return injectVm(this, factory, clazz);
    }

    protected final <VM extends MVVMVm<T>> VM injectActivityVm(Class<VM> clazz) {
        return injectActivityVm(new ViewModelProvider.NewInstanceFactory(), clazz);
    }

    protected final <VM extends MVVMVm<T>> VM injectActivityVm(ViewModelProvider.Factory factory,
                                                               Class<VM> clazz) {
        return injectVm(requireActivity(), factory, clazz);
    }

    protected final <VM extends MVVMVm<T>> VM injectApplicationVm(Class<VM> clazz) {
        return injectApplicationVm(new ViewModelProvider.NewInstanceFactory(), clazz);
    }

    protected final <VM extends MVVMVm<T>> VM injectApplicationVm(ViewModelProvider.Factory factory,
                                                                  Class<VM> clazz) {
        return injectVm(ApplicationOwner.get(), factory, clazz);
    }

    protected final <VM extends MVVMVm<T>> VM injectVm(ViewModelStoreOwner owner, Class<VM> clazz) {
        return injectVm(owner, new ViewModelProvider.NewInstanceFactory(), clazz);
    }

    protected final <VM extends MVVMVm<T>> VM injectVm(ViewModelStoreOwner owner,
                                                     ViewModelProvider.Factory factory,
                                                     Class<VM> clazz) {
        VM vm = new ViewModelProvider(owner, factory).get(clazz);
        vm.action().observe(getViewLifecycleOwner(), this::doAction);
        vm.single().observe(getViewLifecycleOwner(), this::onSingle);
        vm.refresh().observe(getViewLifecycleOwner(), this::onRefresh);
        vm.loadmore().observe(getViewLifecycleOwner(), this::onLoadmore);
        if(!vmList.contains(vm)) {
            vm.setIntents(intents);
            vm.setArguments(arguments);
            vmList.add(vm);
        }
        vm.onReady();
        return vm;
    }

    protected final void releaseVm() {
        Iterator<MVVMVm<T>> iterator = vmList.iterator();
        while (iterator.hasNext()) {
            MVVMVm<T> vm = iterator.next();
            vm.action().removeObservers(getViewLifecycleOwner());
            vm.single().removeObservers(getViewLifecycleOwner());
            vm.refresh().removeObservers(getViewLifecycleOwner());
            vm.loadmore().removeObservers(getViewLifecycleOwner());
            vm.onRecycle();
            iterator.remove();
        }
    }

    private void doAction(MVVMAction action) {
        switch (action.code()) {
            case MVVMAction.LOADING:
                onLoading(action);
                break;
            case MVVMAction.DISMISS:
                onDismiss(action);
                break;
            case MVVMAction.MESSAGE:
                onMessage(action);
                break;
            default:
                onAction(action);
        }
    }

    protected void onLoading(MVVMAction action) {
        LoadingProxy.show();
    }

    protected void onDismiss(MVVMAction action) {
        LoadingProxy.hide();
    }

    protected void onMessage(MVVMAction action) {
        onDismiss(action);
        ToastProxy.show(action.getString("msg"));
    }

    protected void onAction(MVVMAction action) {
        onDismiss(action);
    }

    protected void onSingle(T value) {}

    protected void onRefresh(List<T> refresh) {}

    protected void onLoadmore(List<T> loadmore) {}

    @Deprecated
    protected final void injectAdapter(MVVMAdapter<?> adapter) {
        if(!adapterList.contains(adapter)) {
            adapter.action().observe(getViewLifecycleOwner(), this::onAdapter);
            adapterList.add(adapter);
        }
    }

    protected final <ADAPTER extends MVVMAdapter<?>> ADAPTER injectAdapter(Class<ADAPTER> clazz) {
        try {
            MVVMAdapter<?> adapter = clazz.newInstance();
            if(!adapterList.contains(adapter)) {
                adapter.action().observe(getViewLifecycleOwner(), this::onAdapter);
                adapterList.add(adapter);
            }
            return (ADAPTER) adapter;
        } catch (Exception e) {
            throw new RuntimeException("Cannot create an instance of " + clazz, e);
        }
    }

    protected final void releaseAdapter() {
        Iterator<MVVMAdapter<?>> iterator = adapterList.iterator();
        while (iterator.hasNext()) {
            MVVMAdapter<?> adapter = iterator.next();
            adapter.action().removeObservers(getViewLifecycleOwner());
            iterator.remove();
        }
    }

    protected void onAdapter(MVVMAction action) {}
}
