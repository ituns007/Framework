package org.ituns.framework.master.mvvm.activity;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import org.ituns.framework.master.mvvm.action.MVVMAction;
import org.ituns.framework.master.mvvm.adapter.MVVMAdapter;
import org.ituns.framework.master.mvvm.lifecycle.ApplicationOwner;
import org.ituns.framework.master.mvvm.viewmodel.MVVMVm;
import org.ituns.framework.master.service.channel.loading.LoadingProxy;
import org.ituns.framework.master.service.channel.toast.ToastProxy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class MVVMActivity<T> extends MediaActivity {
    private final ArrayList<MVVMVm<T>> vmList = new ArrayList<>();
    private final ArrayList<MVVMAdapter<?>> adapterList = new ArrayList<>();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearAdapter();
        releaseVm();
    }

    protected final <VM extends MVVMVm<T>> VM injectActivityVm(Class<VM> clazz) {
        return injectActivityVm(new ViewModelProvider.NewInstanceFactory(), clazz);
    }

    protected final <VM extends MVVMVm<T>> VM injectActivityVm(ViewModelProvider.Factory factory,
                                                               Class<VM> clazz) {
        return injectVm(this, factory, clazz);
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
        vm.action().observe(this, this::doAction);
        vm.single().observe(this, this::onSingle);
        vm.refresh().observe(this, this::onRefresh);
        vm.loadmore().observe(this, this::onLoadmore);
        if(!vmList.contains(vm)) {
            vm.setIntents(intents);
            vmList.add(vm);
        }
        vm.onReady();
        return vm;
    }

    protected final void releaseVm() {
        Iterator<MVVMVm<T>> iterator = vmList.iterator();
        while (iterator.hasNext()) {
            MVVMVm<T> vm = iterator.next();
            vm.action().removeObservers(this);
            vm.single().removeObservers(this);
            vm.refresh().removeObservers(this);
            vm.loadmore().removeObservers(this);
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
            adapter.action().observe(this, this::onAdapter);
            adapterList.add(adapter);
        }
    }

    protected final <ADAPTER extends MVVMAdapter<?>> ADAPTER injectAdapter(Class<ADAPTER> clazz) {
        try {
            MVVMAdapter<?> adapter = clazz.newInstance();
            if(!adapterList.contains(adapter)) {
                adapter.action().observe(this, this::onAdapter);
                adapterList.add(adapter);
            }
            return (ADAPTER) adapter;
        } catch (Exception e) {
            throw new RuntimeException("Cannot create an instance of " + clazz, e);
        }
    }

    protected final void clearAdapter() {
        Iterator<MVVMAdapter<?>> iterator = adapterList.iterator();
        while (iterator.hasNext()) {
            MVVMAdapter<?> adapter = iterator.next();
            adapter.action().removeObservers(this);
            iterator.remove();
        }
    }

    protected void onAdapter(MVVMAction action) {}
}
