package com.warpcast.analysis;

import com.warpcast.analysis.dao.FetcherDao;
import com.warpcast.analysis.analysis.AnalysisManager;


public class App {
    public static void main(String[] args) {

        AnalysisManager analysisManager = new AnalysisManager();
        FetcherDao fetcherDao = new FetcherDao();

        fetcherDao.fetchDataAndSaveToFile();

        analysisManager.printTopChannels(analysisManager.getTopChannelsByFollowers());
        analysisManager.getChannelsWithMinFollowers();
        analysisManager.printMostCommonWords(analysisManager.getMostCommonWords());
        System.out.println("Power badge users count: " + fetcherDao.getPowerBadgeUsersCount());
    }
}
