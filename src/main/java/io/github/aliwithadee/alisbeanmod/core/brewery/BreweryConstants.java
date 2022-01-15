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
    public static final int MAX_RATING = 5;

    public static final int ING_DIFF_GREAT = 2;
    public static final int ING_DIFF_FINE = 3;
    public static final int ING_DIFF_POOR = 4;

    public static final int COOK_DIFF_GREAT = 1;
    public static final int COOK_DIFF_FINE = 2;
    public static final int COOK_DIFF_POOR = 3;

    public static final int DISTILL_DIFF_GREAT = 1;
    public static final int DISTILL_DIFF_FINE = 2;
    public static final int DISTILL_DIFF_POOR = 3;

    public static final int AGE_DIFF_GREAT = 2;
    public static final int AGE_DIFF_FINE = 3;
    public static final int AGE_DIFF_POOR = 4;

    // TODO: Bean mod config file
}
