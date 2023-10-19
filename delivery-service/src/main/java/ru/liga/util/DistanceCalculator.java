package ru.liga.util;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class DistanceCalculator {
    private static final double EARTH_RADIUS = 6371; // километров

    public static double calculateDistance(List<Double> couriersCoord, List<Double> destinationCoord) {
        double lat1 = couriersCoord.get(0);
        double lon1 = couriersCoord.get(1);
        double lat2 = destinationCoord.get(0);
        double lon2 = destinationCoord.get(1);

        double difLat = Math.toRadians(lat2 - lat1);
        double difLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(difLat / 2) * Math.sin(difLat / 2) + Math.cos(Math.toRadians(lat1)) *
                Math.cos(Math.toRadians(lat2)) * Math.sin(difLon / 2) * Math.sin(difLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }
}

