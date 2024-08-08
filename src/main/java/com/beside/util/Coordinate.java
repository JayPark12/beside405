package com.beside.util;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.proj4j.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coordinate {
    private double x;
    private double y;

    public Coordinate(double[] coordinates) {

        if (coordinates != null && coordinates.length == 2) {
            double utmX = coordinates[0];
            double utmY = coordinates[1];

            // 대한민국 좌표정보 중부기준
            String ktmZone = "EPSG:5186";

            // 변환기 설정
            CRSFactory factory = new CRSFactory();
            CoordinateReferenceSystem ktmCrs = factory.createFromName(ktmZone);
            CoordinateReferenceSystem wgs84Crs = factory.createFromName("EPSG:4326");

            CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
            CoordinateTransform transform = ctFactory.createTransform(ktmCrs, wgs84Crs);

            ProjCoordinate ktm = new ProjCoordinate(utmX, utmY);
            ProjCoordinate latlon = new ProjCoordinate();
            transform.transform(ktm, latlon);
            //System.out.println("Latitude: " + latlon.y + ", Longitude: " + latlon.x);
            this.y = latlon.y;
            this.x = latlon.x;
        }
    }
}
