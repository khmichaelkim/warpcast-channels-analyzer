package com.warpcast.analysis.analysis;

import com.warpcast.analysis.model.Channel;
import com.warpcast.analysis.dao.FetcherDao;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AnalysisManager {

    private static final int TOP_CHANNEL_LIMIT = 10;
    private static final int MIN_FOLLOWER_COUNT = 25;

    private final FetcherDao fetcherDao;

    public AnalysisManager() {
        this.fetcherDao = new FetcherDao();
    }

    public List<Channel> getTopChannelsByFollowers() {
        List<Channel> channels = new ArrayList<>();
        try {
            channels = fetcherDao.readData();
        } catch (IOException e) {
            System.err.println("Error fetching or analyzing data: " + e.getMessage());
        }
        return TopChannelsByFollower.getTopChannelsByFollowerCount(channels, TOP_CHANNEL_LIMIT);
    }

    public void printTopChannels(List<Channel> topChannels) {
        System.out.println(LocalDate.now() + " Top Channels By Follower Count: ");
        int rank = 1;
        for (Channel channel : topChannels) {
            System.out.println(rank + ". " + channel.getName() + ": " + Math.round(channel.getFollowerCount()/1000.0) + "K");
            rank++;
        }
        System.out.println();
    }

    public void getChannelsWithMinFollowers() {
        List<Channel> channels = new ArrayList<>();
        try {
            channels = fetcherDao.readData();
        } catch (IOException e) {
            System.err.println("Error fetching or analyzing data: " + e.getMessage());
        }
        int count = ChannelsWithMinFollowers.getChannelCountWithMinFollowers(channels, MIN_FOLLOWER_COUNT);
        System.out.println("Number of channels with at least " + MIN_FOLLOWER_COUNT + " followers: " +
                String.format("%.1fK", Math.round(count/100.0) / 10.0));
    }

}
