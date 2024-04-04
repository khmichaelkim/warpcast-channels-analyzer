package com.warpcast.analysis.analysis;

import com.warpcast.analysis.model.Channel;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TopChannelsByFollower {

    public static List<Channel> getTopChannelsByFollowerCount(List<Channel> channels, int limit) {
        return channels.stream()
                .sorted(Comparator.comparingInt(Channel::getFollowerCount).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
}
