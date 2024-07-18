package com.beside.util;

import com.beside.mountain.service.MountainService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class CommonUtil {

    public static String getCurrentTime() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(date);
    }

    public static String getCurrentDate() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(date);
    }

    public static String getMsgId() {
        Random random = new Random();
        int randomNumber = random.nextInt(999999);
        String formattedNumber = String.format("%06d", randomNumber);
        return getCurrentTime() + formattedNumber;
    }

    //String 형식의 날짜와 시간을 LocalDateTime 형식으로 변환
    public static LocalDateTime getDateTime(String inputDate) {
        int year = Integer.parseInt(inputDate.substring(0, 4));
        int month = Integer.parseInt(inputDate.substring(4, 6));
        int day = Integer.parseInt(inputDate.substring(6, 8));
        int hour = Integer.parseInt(inputDate.substring(8, 10));
        int minute = Integer.parseInt(inputDate.substring(10, 12));
        return LocalDateTime.of(year, month, day, hour, minute);
    }

    public static List<String> potoFile (String mntilistNo, String mntiName) throws URISyntaxException {

        String folderPath = "mntiPotoData/" +mntilistNo+"_"+mntiName;
        ClassLoader classLoader = MountainService.class.getClassLoader();
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

    public static String getImageByMountain(String fileName) throws IOException {
        String uploadDir = "src/main/resources/static/images/";

        File folder = new File(uploadDir);
        File[] matchingFiles = folder.listFiles((dir, name) -> name.contains(fileName) && isImageFile(name));

        if (matchingFiles == null || matchingFiles.length == 0) {
            return null;
        }

        File imgFile = matchingFiles[0];  // 첫 번째 매칭 파일 선택 (여러 개일 경우)

        byte[] imageBytes;
        try (InputStream in = new FileInputStream(imgFile)) {
            imageBytes = StreamUtils.copyToByteArray(in);
        }

        // 바이트 배열을 String 형식으로 변환
        return Base64.getEncoder().encodeToString(imageBytes);
    }


    private static boolean isImageFile(String fileName) {
        String lowerCaseName = fileName.toLowerCase();
        return lowerCaseName.endsWith(".jpg") || lowerCaseName.endsWith(".jpeg") || lowerCaseName.endsWith(".png") || lowerCaseName.endsWith(".gif");
    }


}
