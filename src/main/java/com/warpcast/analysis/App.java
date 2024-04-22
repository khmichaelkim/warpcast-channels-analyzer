package com.warpcast.analysis;

import com.warpcast.analysis.dao.FetcherDao;
import com.warpcast.analysis.analysis.AnalysisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {

        // Configured HttpClient to ignore warnings that do not affect functionality. Redirecting JUL to SLF4J
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        AnalysisManager analysisManager = new AnalysisManager();
        FetcherDao fetcherDao = new FetcherDao();

        fetcherDao.fetchDataAndSaveToFile();

        analysisManager.printTopChannels(analysisManager.getTopChannelsByFollowers());
        analysisManager.getChannelsWithMinFollowers();
        analysisManager.printMostCommonWords(analysisManager.getMostCommonWords());
        System.out.println("Power badge users count: " + fetcherDao.getPowerBadgeUsersCount());
    }
}
