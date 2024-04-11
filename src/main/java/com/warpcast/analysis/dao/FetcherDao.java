package com.warpcast.analysis.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warpcast.analysis.model.Channel;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FetcherDao {

    private static final String URL = "https://api.warpcast.com/v2/all-channels";
    private static final String POWER_BADGE_USERS_URL = "https://api.warpcast.com/v2/power-badge-users";
    private static final String BASE_DIR = "data";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(FetcherDao.class);

    private String getFileName() {
        LocalDate date = LocalDate.now();
        File dir = new File(BASE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return BASE_DIR + File.separator + "warpcast_data_" + date.format(DATE_FORMATTER) + ".json";
    }

    public void fetchDataAndSaveToFile() {
        HttpResponse<String> response = Unirest.get(URL)
                .header("accept", "application/json")
                .asString();
        String fileName = getFileName();

        if (response.getStatus() == 200) {
            try (FileWriter file = new FileWriter(fileName)) {
                file.write(response.getBody());
                file.flush();
                logger.info("Data saved to " + fileName);
            } catch (IOException e) {
                logger.error("Failed to save data: " + e.getMessage());
            }
        } else {
            logger.error("Failed to fetch data: " + response.getStatusText());
        }
    }

    public List<Channel> readData() throws IOException {
        String fileName = getFileName();
        File file = new File(fileName);
        if (!file.exists()) {
            throw new IOException("Data file does not exist: " + fileName);
        }
        try {
            JsonNode rootNode = mapper.readTree(file);
            JsonNode channelsNode = rootNode.path("result").path("channels");
            if (!channelsNode.isMissingNode() && channelsNode.isArray()) {
                return mapper.convertValue(channelsNode, new TypeReference<List<Channel>>(){});
            } else {
                throw new IOException("The expected data structure is not found or is not an array");
            }
        } catch (IOException e) {
            logger.error("Error reading data: ", e);
            throw e;
        }
    }

    public int getPowerBadgeUsersCount() {
        try {
            HttpResponse<String> response = Unirest.get(POWER_BADGE_USERS_URL)
                    .header("accept", "application/json")
                    .asString();

            if (response.getStatus() == 200) {
                JsonNode rootNode = mapper.readTree(response.getBody());
                JsonNode fidsNode = rootNode.path("result").path("fids");
                if (!fidsNode.isMissingNode() && fidsNode.isArray()) {
                    // If fids is an array, we can count its size.
                    return fidsNode.size();
                } else {
                    logger.error("Failed to parse the power badge users data.");
                    return 0;
                }
            } else {
                logger.error("Failed to fetch power badge users data: " + response.getStatusText());
                return 0;
            }
        } catch (Exception e) {
            logger.error("Error fetching power badge users count: ", e);
            return 0;
        }
    }
}
