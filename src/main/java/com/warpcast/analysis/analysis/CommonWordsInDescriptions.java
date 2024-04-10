package com.warpcast.analysis.analysis;

import com.warpcast.analysis.model.Channel;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CommonWordsInDescriptions {

    private static final Set<String> STOP_WORDS = Set.of(
            "and", "the", "a", "to", "of", "in", "for", "on",
            "with", "as", "is", "that", "it", "at", "by", "from",
            "channel", "your", "you", "all", "https", "place", "s", "about",
            "share", "this", "we", "or", "things", "welcome", "are", "i",
            "here", "be", "com", "discuss", "world",
            "our", "xyz", "t", "an", "more", "join", "post", "home", "where",
            "new", "what", "us", "not", "can", "no", "get", "let", "will", "space"
    );

    private List<Channel> channels;

    public CommonWordsInDescriptions(List<Channel> channels) {
        this.channels = channels;
    }

    public Map<String, Long> findMostCommonWords(int topN) {
        Map<String, Long> wordFrequency = this.channels.stream()
                .flatMap(channel -> Arrays.stream(channel.getDescription().toLowerCase().split("\\W+")))
                .filter(word -> !word.isEmpty() && !STOP_WORDS.contains(word))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return wordFrequency.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(topN)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
}
