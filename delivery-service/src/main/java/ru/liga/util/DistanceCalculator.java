package ru.liga.util;

import lombok.experimental.UtilityClass;
import ru.liga.dto.DistanceDto;

@UtilityClass
public class DistanceCalculator {
    private static final double EARTH_RADIUS = 6371; // километров

    /**
     * Method calculates the difference between two sets of coordinates according to Haversine formula
     *
     * @param couriersCoordinates    the coordinates of a courier
     * @param destinationCoordinates the coordinates of a destination
     * @return the distance between two sets of coordinates
     */
    public static double calculateDistance(DistanceDto couriersCoordinates, DistanceDto destinationCoordinates) {
        double courierLatitude = couriersCoordinates.getLatitude();
        double courierLongitude = couriersCoordinates.getLongitude();
        double destinationLatitude = destinationCoordinates.getLatitude();
        double destinationLongitude = destinationCoordinates.getLongitude();

        double differenceLatitude = Math.toRadians(destinationLatitude - courierLatitude);
        double differenceLongitude = Math.toRadians(destinationLongitude - courierLongitude);

        double squareHalfDistance = Math.sin(differenceLatitude / 2) * Math.sin(differenceLatitude / 2) +
                Math.cos(Math.toRadians(courierLatitude)) * Math.cos(Math.toRadians(destinationLatitude)) *
                        Math.sin(differenceLongitude / 2) * Math.sin(differenceLongitude / 2);

        double centralAngle = 2 * Math.atan2(Math.sqrt(squareHalfDistance), Math.sqrt(1 - squareHalfDistance));

        return EARTH_RADIUS * centralAngle;
    }
}

