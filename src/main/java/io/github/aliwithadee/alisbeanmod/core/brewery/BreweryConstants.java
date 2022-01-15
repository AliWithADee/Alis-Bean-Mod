package io.github.aliwithadee.alisbeanmod.core.brewery;

public class BreweryConstants {
    // 1 tick               = 3.6 minecraft seconds
    // 1 minecraft second   = 0.0138 seconds
    // 1 minecraft minute   = 0.83 seconds

    // 1 minecraft second   = 0.27 ticks
    // 1 minecraft minute   = 16 ticks
    // 1 minecraft hour     = 1000 ticks
    // 1 minecraft day      = 24000 ticks

    // 1 minecraft day  =  1 barrel age year
    public static final int MINUTES_PER_BARREL_YEAR = 20; // TODO: Don't forget to set this to actual value
    public static final int MAX_RECIPE_DIFFERENCE = 25;
}
