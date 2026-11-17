package gov.nysenate;

/**
 * A senator's site color palette, as hex colors (e.g. "#FFFFFF").
 * @param name the palette name, e.g. "default"
 * @param lgt the light color
 * @param med the medium color
 * @param drk the dark color
 */
public record Palette(String name, String lgt, String med, String drk) {}
