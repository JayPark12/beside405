package com.beside.mountain.service;

import com.beside.mountain.domain.MntiEntity;
import com.beside.define.GsonParserSvc;
import com.beside.mountain.dto.MntiListOutput;
import com.beside.mountain.repository.MntiRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class MntiListService {

    private final MntiRepository mntiRepository;

    public List<MntiListOutput> mntiList() throws URISyntaxException {
        List<MntiListOutput> mntiListOutput = new ArrayList<>();
        List<MntiEntity> mntiShufList = mntiRepository.findByMnti();
        //Collections.shuffle(mntiShufList);
        List<MntiEntity> mntiShufList7 =mntiShufList.stream().limit(7).collect(Collectors.toList());
        for (int i = 0; i < mntiShufList7.size(); i++)
        {
            List<String> potoFileSelect = potoFile(mntiShufList7.get(i).getMntiListNo() ,mntiShufList7.get(i).getMntiName());
            MntiListOutput mntiOutput = new MntiListOutput();
            mntiOutput.setMntiName(mntiShufList7.get(i).getMntiName());
            mntiOutput.setMntiListNo(mntiShufList7.get(i).getMntiListNo());
            mntiOutput.setMntiLevel(mntiShufList7.get(i).getMntiLevel());
            mntiOutput.setMntiAdd(mntiShufList7.get(i).getMntiAdd());

            if(potoFileSelect.size() != 0) {
                mntiOutput.setPotoFile(potoFileSelect.get(0));
            }

            mntiListOutput.add(mntiOutput);
        }
        return mntiListOutput;
    }

    public List<String> potoFile (String mntilistNo, String mntiName) throws URISyntaxException {

        String folderPath = "mntiPotoData/" +mntilistNo+"_"+mntiName;
        ClassLoader classLoader = MntiListService.class.getClassLoader();
        URL resource = classLoader.getResource(folderPath);

        if (resource != null) {
            Path path = Paths.get(resource.toURI());

            // 스트림을 사용하여 파일 목록 가져오기
            try (Stream<Path> paths = Files.walk(path, 1)) {
                return List.of(paths
                        .filter(Files::isRegularFile)
                        .map(Path::getFileName)
                        .map(Path::toString)
                        .toArray(String[]::new));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
