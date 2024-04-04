package com.warpcast.analysis.analysis;

import com.warpcast.analysis.model.Channel;
import com.warpcast.analysis.dao.FetcherDao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AnalysisManager {

    private static final int TOP_CHANNEL_LIMIT = 10;

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
        int rank = 1;
        for (Channel channel : topChannels) {
            System.out.println(rank + ". " + channel.getName() + ": " + channel.getFollowerCount()/1000 + "K");
            rank++;
        }
    }
}
