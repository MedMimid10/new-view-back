package com.example.newView.model.dto;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class CoordinatesConverter implements AttributeConverter<double[], String> {
    private static final String SEPARATOR = ",";

    @Override
    public String convertToDatabaseColumn(double[] coordinates) {
        if (coordinates == null || coordinates.length != 2) {
            return null;
        }
        return String.format("%f%s%f", coordinates[0], SEPARATOR, coordinates[1]);
    }

    @Override
    public double[] convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return null;
        }
        String[] parts = dbData.split(SEPARATOR);
        if (parts.length != 2) {
            return null;
        }
        try {
            return new double[]{
                    Double.parseDouble(parts[0]),
                    Double.parseDouble(parts[1])
            };
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

