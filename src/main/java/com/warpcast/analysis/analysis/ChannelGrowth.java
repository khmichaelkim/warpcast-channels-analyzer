package com.warpcast.analysis.analysis;

import com.warpcast.analysis.model.Channel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ChannelGrowth {

    private static final String BASE_DIR = "data";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final ObjectMapper mapper = new ObjectMapper();

    private String getFileName(LocalDate date) {
        return BASE_DIR + File.separator + "warpcast_data_" + date.format(DATE_FORMATTER) + ".json";
    }

    public ChannelGrowth() {
    }

    private List<Channel> readData(LocalDate date) throws IOException {
        File file = new File(getFileName(date));
        if (!file.exists()) {
            throw new IOException("Data file does not exist: " + getFileName(date));
        }
        JsonNode rootNode = mapper.readTree(file);
        JsonNode channelsNode = rootNode.path("result").path("channels");
        if (!channelsNode.isMissingNode() && channelsNode.isArray()) {
            return mapper.convertValue(channelsNode, new TypeReference<List<Channel>>(){});
        } else {
            throw new IOException("The expected data structure is not found or is not an array");
        }
    }

    public void compareWeeklyGrowth() {
        LocalDate today = LocalDate.now();
        LocalDate lastWeek = today.minusDays(7);
        try {
            List<Channel> todayChannels = readData(today);
            List<Channel> lastWeekChannels = readData(lastWeek);

            Map<String, Integer> lastWeekFollowers = lastWeekChannels.stream()
                    .collect(Collectors.toMap(Channel::getId, Channel::getFollowerCount));

            List<Channel> growthList = todayChannels.stream()
                    .filter(channel -> lastWeekFollowers.containsKey(channel.getId())) // Ensure channel existed last week
                    .sorted(Comparator.comparingInt((Channel c) -> c.getFollowerCount() - lastWeekFollowers.get(c.getId()))
                            .reversed())
                    .limit(10)
                    .collect(Collectors.toList());

            System.out.println(today + " Top Channels By Follower Growth (Past Week): ");
            int rank = 1;
            for (Channel channel : growthList) {
                int lastWeekCount = lastWeekFollowers.get(channel.getId());
                int growth = channel.getFollowerCount() - lastWeekCount;
                System.out.println(rank + ". " + channel.getName() + ": " + growth + " new followers");
                rank++;
            }
        } catch (IOException e) {
            System.err.println("Error in fetching or processing data: " + e.getMessage());
        }
    }
}
