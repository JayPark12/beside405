package com.beside.define;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GsonParserSvc {
    private final ObjectMapper objectMapper;

    public GsonParserSvc(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String GsonParser (String response){
        String imgFilename = null;
        List<String> imgFilenameList = new ArrayList<>();
        try {
            JsonNode rootNode = objectMapper.readTree(response);

            JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

            if (itemsNode.isArray()) {
                for (JsonNode item : itemsNode) {
                    imgFilename = item.path("imgfilename").asText();
                    imgFilenameList.add(imgFilename);
                }
            }
            if (imgFilenameList.size() > 1) {   // 한개 이상이면 첫번째 사진 나중에 예약하기 들어갈때 다건으로 들어가야함
                imgFilename = imgFilenameList.get(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return imgFilename;
    }

}
