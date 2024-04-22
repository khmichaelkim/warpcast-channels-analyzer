package com.warpcast.analysis.analysis;

import com.warpcast.analysis.model.Channel;
import com.warpcast.analysis.dao.FetcherDao;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class AnalysisManager {

    private static final int TOP_CHANNEL_LIMIT = 10;
    private static final int MIN_FOLLOWER_COUNT = 25;
    private final int TOP_WORD_LIMIT = 10;

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
        System.out.println();
        System.out.println(LocalDate.now() + " Top Channels By Follower Count: ");
        int rank = 1;
        for (Channel channel : topChannels) {
            System.out.println(rank + ". " + channel.getName() + ": " + Math.round(channel.getFollowerCount()/1000.0) + "K");
            rank++;
        }
    }

    public void getChannelsWithMinFollowers() {
        List<Channel> channels = new ArrayList<>();
        try {
            channels = fetcherDao.readData();
        } catch (IOException e) {
            System.err.println("Error fetching or analyzing data: " + e.getMessage());
        }
        int count = ChannelsWithMinFollowers.getChannelCountWithMinFollowers(channels, MIN_FOLLOWER_COUNT);
        System.out.println();
        System.out.println("Number of channels with at least " + MIN_FOLLOWER_COUNT + " followers: " +
                String.format("%.1fK", Math.round(count/100.0) / 10.0));
    }

    public Map<String, Long> getMostCommonWords() {
        List<Channel> channels = new ArrayList<>();
        try {
            channels = fetcherDao.readData();
        } catch (IOException e) {
            System.err.println("Error fetching or analyzing data: " + e.getMessage());
            return Collections.emptyMap();
        }
        CommonWordsInDescriptions analysis = new CommonWordsInDescriptions(channels);
        return analysis.findMostCommonWords(TOP_WORD_LIMIT);
    }

    public void printMostCommonWords(Map<String, Long> commonWords) {
        System.out.println();
        System.out.println(LocalDate.now() + " Most Common Words in Channel Descriptions: ");
        AtomicInteger rank = new AtomicInteger(1);
        commonWords.forEach((word, count) -> {
            System.out.println(rank.getAndIncrement() + ". " + word + ": " + count + " times");
        });
        System.out.println();
    }

    public void printChannelGrowth() {
        ChannelGrowth channelGrowth = new ChannelGrowth();
        channelGrowth.compareWeeklyGrowth();
    }

}
