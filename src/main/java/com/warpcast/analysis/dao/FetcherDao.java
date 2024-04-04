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
import java.util.List;

public class FetcherDao {

    private static final String URL = "https://api.warpcast.com/v2/all-channels";
    public static final String FILE_NAME = "warpcast_data_" + LocalDate.now() + ".json";
    private ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(FetcherDao.class);

    public void fetchDataAndSaveToFile() {
        HttpResponse<String> response = Unirest.get(URL)
                .header("accept", "application/json")
                .asString();

        if (response.getStatus() == 200) {
            try (FileWriter file = new FileWriter(FILE_NAME)) {
                file.write(response.getBody());
                file.flush();
                logger.info("Data saved to " + FILE_NAME);
            } catch (IOException e) {
                logger.error("Failed to save data: " + e.getMessage());
            }
        } else {
            logger.error("Failed to fetch data: " + response.getStatusText());
        }
    }

    public List<Channel> readData() throws IOException {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            throw new IOException("Data file does not exist: " + FILE_NAME);
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
}
