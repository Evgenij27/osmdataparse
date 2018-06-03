package org.helio.document;

import java.math.BigDecimal;

public class Bounds {
    public static final String MINLAT = "minlat";
    public static final String MAXLAT = "maxlat";
    public static final String MINLON = "minlon";
    public static final String MAXLON = "maxlon";

    private final BigDecimal minLatitude;
    private final BigDecimal maxLatitude;
    private final BigDecimal minLongtitude;
    private final BigDecimal maxLongtitude;

    public Bounds(BigDecimal minLatitude, BigDecimal maxLatitude, BigDecimal minLongtitude, BigDecimal maxLongtitude) {
        this.minLatitude = minLatitude;
        this.maxLatitude = maxLatitude;
        this.minLongtitude = minLongtitude;
        this.maxLongtitude = maxLongtitude;
    }

    @Override
    public String toString() {
        return "Bounds{" +
                "minLatitude=" + minLatitude +
                ", maxLatitude=" + maxLatitude +
                ", minLongtitude=" + minLongtitude +
                ", maxLongtitude=" + maxLongtitude +
                '}';
    }

    public BigDecimal getMinLatitude() {
        return minLatitude;
    }

    public BigDecimal getMaxLatitude() {
        return maxLatitude;
    }

    public BigDecimal getMinLongtitude() {
        return minLongtitude;
    }

    public BigDecimal getMaxLongtitude() {
        return maxLongtitude;
    }
}
