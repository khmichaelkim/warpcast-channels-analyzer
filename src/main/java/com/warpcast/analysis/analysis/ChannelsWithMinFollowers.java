package com.warpcast.analysis.analysis;

import com.warpcast.analysis.model.Channel;

import java.util.List;

public class ChannelsWithMinFollowers {

    public static int getChannelCountWithMinFollowers(List<Channel> channels, int minFollowers) {
        return (int) channels.stream()
                .filter(channel -> channel.getFollowerCount() >= minFollowers)
                .count();
    }

}
