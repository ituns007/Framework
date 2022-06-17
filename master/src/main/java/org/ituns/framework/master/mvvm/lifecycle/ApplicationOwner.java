package org.ituns.framework.master.mvvm.lifecycle;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

import org.jetbrains.annotations.NotNull;

public class ApplicationOwner implements ViewModelStoreOwner {
    public static ApplicationOwner get() {
        return ApplicationOwner.SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final ApplicationOwner INSTANCE = new ApplicationOwner();
    }

    private ViewModelStore viewModelStore;
    private ApplicationOwner() {}

    @NonNull
    @NotNull
    @Override
    public ViewModelStore getViewModelStore() {
        if(viewModelStore == null) {
            viewModelStore = new ViewModelStore();
        }
        return viewModelStore;
    }
}
