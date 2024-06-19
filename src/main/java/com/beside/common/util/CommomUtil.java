package com.beside.common.util;

import com.beside.mountain.service.MntiListService;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;
@Component
public class CommomUtil {

    public static List<String> potoFile (String mntilistNo, String mntiName) throws URISyntaxException {

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
