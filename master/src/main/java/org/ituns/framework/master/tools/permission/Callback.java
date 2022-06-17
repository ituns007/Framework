package org.ituns.framework.master.tools.permission;

public abstract class Callback {
    public abstract void onGranted(int requestCode);
    public boolean onDenied(int requestCode) { return false; }
    public void onError(int requestCode) {}
}
