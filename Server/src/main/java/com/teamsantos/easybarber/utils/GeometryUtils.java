package com.teamsantos.easybarber.utils;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

public class GeometryUtils {
    public static final int SRID = 4326; // LatLng
    private final static WKTReader wktReader = new WKTReader();

    private static Geometry wktToGeometry(String wellKnownText) throws ParseException {
        Geometry geometry = null;
        geometry = wktReader.read(wellKnownText);
        return geometry;
    }

    public static Point parseLocation(double latitude, double longitude) throws ParseException {
        Geometry geometry = GeometryUtils.wktToGeometry(String.format("POINT (%s %s)", longitude, latitude));
        Point p = (Point) geometry;
        p.setSRID(4326);
        return p;
    }
}
