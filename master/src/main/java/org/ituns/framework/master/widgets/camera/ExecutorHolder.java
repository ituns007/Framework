package org.ituns.framework.master.widgets.camera;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorHolder {
    private ExecutorService executorService;

    public synchronized Executor get() {
        if(executorService == null) {
            executorService = Executors.newSingleThreadExecutor();
        }
        return executorService;
    }

    public synchronized void shutdown() {
        if(executorService != null) {
            executorService.shutdown();
            executorService = null;
        }
    }
}
