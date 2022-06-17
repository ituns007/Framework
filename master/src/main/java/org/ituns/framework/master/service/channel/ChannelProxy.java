package org.ituns.framework.master.service.channel;

public class ChannelProxy {

    public static <T extends ChannelData> void postData(T data) {
        ChannelService.get().postData(data);
    }
}
