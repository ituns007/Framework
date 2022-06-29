package org.ituns.framework;

import org.ituns.android.logcat.Level;
import org.ituns.framework.master.FrameworkApp;
import org.ituns.framework.master.service.ServiceConfig;
import org.ituns.framework.master.service.channel.ChannelConfig;
import org.ituns.framework.master.service.env.EnvConfig;
import org.ituns.framework.master.service.http.HttpConfig;
import org.ituns.framework.master.service.logcat.LogConfig;
import org.ituns.framework.master.service.persist.PersistConfig;
import org.ituns.framework.master.service.tasks.TaskConfig;
import org.ituns.framework.master.service.tbs.TbsConfig;

public class App extends FrameworkApp {

    @Override
    protected ServiceConfig onServiceConfig() {
        return new ServiceConfig.Builder()
                .add(new LogConfig.Builder(App.this)
                        .debug(true)
                        .level(Level.VERBOSE)
                        .tag("App")
                        .build())
                .add(new HttpConfig.Builder(App.this)
                        .debug(true)
                        .timeout(30)
                        .build())
                .add(new TaskConfig.Builder(App.this)
                        .build())
                .add(new ChannelConfig.Builder(App.this)
                        .build())
                .add(new PersistConfig.Builder(App.this)
                        .build())
                .add(new EnvConfig.Builder(App.this)
                        .tag("mc")
                        .def("env.properties")
                        .build())
                .add(new TbsConfig.Builder(App.this)
                        .build())
                .build();
    }
}
