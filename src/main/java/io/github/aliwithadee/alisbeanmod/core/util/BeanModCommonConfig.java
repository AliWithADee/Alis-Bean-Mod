package io.github.aliwithadee.alisbeanmod.core.util;

import net.minecraftforge.common.ForgeConfigSpec;

public class BeanModCommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> MINUTES_PER_BARREL_YEAR;
    public static final ForgeConfigSpec.ConfigValue<Integer> MAX_RECIPE_DIFFERENCE;
    public static final ForgeConfigSpec.ConfigValue<Integer> MAX_RATING;

    public static final ForgeConfigSpec.ConfigValue<Integer> ING_DIFF_GREAT;
    public static final ForgeConfigSpec.ConfigValue<Integer> ING_DIFF_FINE;
    public static final ForgeConfigSpec.ConfigValue<Integer> ING_DIFF_POOR;

    public static final ForgeConfigSpec.ConfigValue<Integer> COOK_DIFF_GREAT;
    public static final ForgeConfigSpec.ConfigValue<Integer> COOK_DIFF_FINE;
    public static final ForgeConfigSpec.ConfigValue<Integer> COOK_DIFF_POOR;

    public static final ForgeConfigSpec.ConfigValue<Integer> DISTILL_DIFF_GREAT;
    public static final ForgeConfigSpec.ConfigValue<Integer> DISTILL_DIFF_FINE;
    public static final ForgeConfigSpec.ConfigValue<Integer> DISTILL_DIFF_POOR;

    public static final ForgeConfigSpec.ConfigValue<Integer> AGE_DIFF_GREAT;
    public static final ForgeConfigSpec.ConfigValue<Integer> AGE_DIFF_FINE;
    public static final ForgeConfigSpec.ConfigValue<Integer> AGE_DIFF_POOR;

    public static final ForgeConfigSpec.ConfigValue<Float> BASE_ALCOHOL_INCREASE;

    public static final ForgeConfigSpec.ConfigValue<Float> MIN_STRENGTH;
    public static final ForgeConfigSpec.ConfigValue<Float> MAX_STRENGTH;

    public static final ForgeConfigSpec.ConfigValue<Float> MAX_ALCOHOL;

    public static final ForgeConfigSpec.ConfigValue<Integer> ALCOHOL_DECREASE_TICKS;

    static {
        BUILDER.comment(" ====== Alis Bean Mod Config ======")
                .push("Brewery");

        BUILDER.push("General");

        MINUTES_PER_BARREL_YEAR = BUILDER
                .comment(" How many minecraft minutes does it take for the ageing barrel to age a drink.",
                        " Default = 1440",
                        " (1440 minecraft minutes  =  1 barrel age year)")
                .define("MINUTES_PER_BARREL_YEAR", 20); // TODO: Don't forget to set this to actual value

        MAX_RECIPE_DIFFERENCE = BUILDER
                .comment(" Defines how far the ingredients in the cooking pot can be from the correct ingredients of a valid recipe.",
                        " If you exceed this limit, the drink will become scuffed.",
                        " Default = 25")
                .define("MAX_RECIPE_DIFFERENCE", 25);

        BASE_ALCOHOL_INCREASE = BUILDER
                .comment(" Base value to increase player alcohol levels by, before any drink modifiers are added.",
                        " Default = 10.0")
                .define("BASE_ALCOHOL_INCREASE", 10f);

        MIN_STRENGTH = BUILDER
                .comment(" Minimum strength a drink can have. 0% strength is allowed for non-alcoholic drinks.",
                        " Default = 0.1")
                .define("MIN_STRENGTH", 0.1f);

        MAX_STRENGTH = BUILDER
                .comment(" Maximum strength a drink can have.",
                        " Default = 3.0")
                .define("MAX_STRENGTH", 3.0f);

        MAX_ALCOHOL = BUILDER
                .comment(" Maximum alcohol a player can take before effects become fatal.",
                        " Default = 100.0")
                .define("MAX_ALCOHOL", 100f);

        ALCOHOL_DECREASE_TICKS = BUILDER
                .comment(" Number of ticks before player alcohol decreases by 1.",
                        " Default = 1175")
                .define("ALCOHOL_DECREASE_TICKS", 50);

        BUILDER.pop();

        BUILDER.push("Rating");

        MAX_RATING = BUILDER
                .comment(" Maximum rating drinks can reach.",
                        " Default = 5")
                .define("MAX_RATING", 5);

        ING_DIFF_GREAT = BUILDER
                .comment(" Maximum ingredient error to reach \"Great\" rating.",
                        " Default = 2")
                .define("ING_DIFF_GREAT", 2);

        ING_DIFF_FINE = BUILDER
                .comment(" Maximum ingredient error to reach \"Fine\" rating.",
                        " Default = 3")
                .define("ING_DIFF_FINE", 3);

        ING_DIFF_POOR = BUILDER
                .comment(" Maximum ingredient error to reach \"Poor\" rating.",
                        " Default = 4")
                .define("ING_DIFF_POOR", 4);


        COOK_DIFF_GREAT = BUILDER
                .comment(" Maximum cooking error to reach \"Great\" rating (in minutes).",
                        " Default = 1")
                .define("COOK_DIFF_GREAT", 1);

        COOK_DIFF_FINE = BUILDER
                .comment(" Maximum cooking error to reach \"Fine\" rating (in minutes).",
                        " Default = 2")
                .define("COOK_DIFF_FINE", 2);

        COOK_DIFF_POOR = BUILDER
                .comment(" Maximum cooking error to reach \"Poor\" rating (in minutes).",
                        " Default = 3")
                .define("COOK_DIFF_POOR", 3);


        DISTILL_DIFF_GREAT = BUILDER
                .comment(" Maximum distilling error to reach \"Great\" rating.",
                        " Default = 1")
                .define("DISTILL_DIFF_GREAT", 1);

        DISTILL_DIFF_FINE = BUILDER
                .comment(" Maximum distilling error to reach \"Fine\" rating.",
                        " Default = 2")
                .define("DISTILL_DIFF_FINE", 2);

        DISTILL_DIFF_POOR = BUILDER
                .comment(" Maximum distilling error to reach \"Poor\" rating.",
                        " Default = 3")
                .define("DISTILL_DIFF_POOR", 3);


        AGE_DIFF_GREAT = BUILDER
                .comment(" Maximum barrel ageing error to reach \"Great\" rating (in years).",
                        " Default = 2")
                .define("AGE_DIFF_GREAT", 2);

        AGE_DIFF_FINE = BUILDER
                .comment(" Maximum barrel ageing error to reach \"Fine\" rating (in years).",
                        " Default = 3")
                .define("AGE_DIFF_FINE", 3);

        AGE_DIFF_POOR = BUILDER
                .comment(" Maximum barrel ageing error to reach \"Poor\" rating (in years).",
                        " Default = 4")
                .define("AGE_DIFF_POOR", 4);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
