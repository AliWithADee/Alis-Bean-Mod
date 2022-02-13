package io.github.aliwithadee.alisbeanmod.core.util;

import io.github.aliwithadee.alisbeanmod.AlisBeanMod;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(modid = AlisBeanMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BeanModConfig {
    public static final Config CONFIG;
    public static final ForgeConfigSpec SPEC;
    static {
        final Pair<Config, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Config::new);
        CONFIG = specPair.getLeft();
        SPEC = specPair.getRight();
    }

    public static int MINUTES_PER_BARREL_YEAR;
    public static int MAX_RECIPE_DIFFERENCE;
    public static int MAX_RATING;

    public static int ING_DIFF_GREAT;
    public static int ING_DIFF_FINE;
    public static int ING_DIFF_POOR;

    public static int COOK_DIFF_GREAT;
    public static int COOK_DIFF_FINE;
    public static int COOK_DIFF_POOR;

    public static int DISTILL_DIFF_GREAT;
    public static int DISTILL_DIFF_FINE;
    public static int DISTILL_DIFF_POOR;

    public static int AGE_DIFF_GREAT;
    public static int AGE_DIFF_FINE;
    public static int AGE_DIFF_POOR;

    public static float BASE_ALCOHOL_INCREASE;
    public static float MIN_STRENGTH;
    public static float MAX_STRENGTH;
    public static float MAX_ALCOHOL;
    public static int ALCOHOL_DECREASE_TICKS;

    @SubscribeEvent
    public static void onModConfigEvent(final ModConfigEvent event) {
        if (event.getConfig().getSpec() == BeanModConfig.SPEC) {
            bakeConfig();
        }
    }

    public static void bakeConfig() {
        MINUTES_PER_BARREL_YEAR = CONFIG.MINUTES_PER_BARREL_YEAR.get();
        MAX_RECIPE_DIFFERENCE = CONFIG.MAX_RECIPE_DIFFERENCE.get();
        MAX_RATING = CONFIG.MAX_RATING.get();

        ING_DIFF_GREAT = CONFIG.ING_DIFF_GREAT.get();
        ING_DIFF_FINE = CONFIG.ING_DIFF_FINE.get();
        ING_DIFF_POOR = CONFIG.ING_DIFF_POOR.get();

        COOK_DIFF_GREAT = CONFIG.COOK_DIFF_GREAT.get();
        COOK_DIFF_FINE = CONFIG.COOK_DIFF_FINE.get();
        COOK_DIFF_POOR = CONFIG.COOK_DIFF_POOR.get();

        DISTILL_DIFF_GREAT = CONFIG.DISTILL_DIFF_GREAT.get();
        DISTILL_DIFF_FINE = CONFIG.DISTILL_DIFF_FINE.get();
        DISTILL_DIFF_POOR = CONFIG.DISTILL_DIFF_POOR.get();

        AGE_DIFF_GREAT = CONFIG.AGE_DIFF_GREAT.get();
        AGE_DIFF_FINE = CONFIG.AGE_DIFF_FINE.get();
        AGE_DIFF_POOR = CONFIG.AGE_DIFF_POOR.get();

        BASE_ALCOHOL_INCREASE = CONFIG.BASE_ALCOHOL_INCREASE.get();
        MIN_STRENGTH = CONFIG.MIN_STRENGTH.get();
        MAX_STRENGTH = CONFIG.MAX_STRENGTH.get();
        MAX_ALCOHOL = CONFIG.MAX_ALCOHOL.get();
        ALCOHOL_DECREASE_TICKS = CONFIG.ALCOHOL_DECREASE_TICKS.get();
    }

    public static class Config {
        public final ForgeConfigSpec.ConfigValue<Integer> MINUTES_PER_BARREL_YEAR;
        public final ForgeConfigSpec.ConfigValue<Integer> MAX_RECIPE_DIFFERENCE;
        public final ForgeConfigSpec.ConfigValue<Integer> MAX_RATING;

        public final ForgeConfigSpec.ConfigValue<Integer> ING_DIFF_GREAT;
        public final ForgeConfigSpec.ConfigValue<Integer> ING_DIFF_FINE;
        public final ForgeConfigSpec.ConfigValue<Integer> ING_DIFF_POOR;

        public final ForgeConfigSpec.ConfigValue<Integer> COOK_DIFF_GREAT;
        public final ForgeConfigSpec.ConfigValue<Integer> COOK_DIFF_FINE;
        public final ForgeConfigSpec.ConfigValue<Integer> COOK_DIFF_POOR;

        public final ForgeConfigSpec.ConfigValue<Integer> DISTILL_DIFF_GREAT;
        public final ForgeConfigSpec.ConfigValue<Integer> DISTILL_DIFF_FINE;
        public final ForgeConfigSpec.ConfigValue<Integer> DISTILL_DIFF_POOR;

        public final ForgeConfigSpec.ConfigValue<Integer> AGE_DIFF_GREAT;
        public final ForgeConfigSpec.ConfigValue<Integer> AGE_DIFF_FINE;
        public final ForgeConfigSpec.ConfigValue<Integer> AGE_DIFF_POOR;

        public final ForgeConfigSpec.ConfigValue<Float> BASE_ALCOHOL_INCREASE;

        public final ForgeConfigSpec.ConfigValue<Float> MIN_STRENGTH;
        public final ForgeConfigSpec.ConfigValue<Float> MAX_STRENGTH;

        public final ForgeConfigSpec.ConfigValue<Float> MAX_ALCOHOL;

        public final ForgeConfigSpec.ConfigValue<Integer> ALCOHOL_DECREASE_TICKS;

        public Config(ForgeConfigSpec.Builder builder) {
            builder.comment(" ====== Alis Bean Mod Config ======")
                    .push("Brewery");

            builder.push("General");

            MINUTES_PER_BARREL_YEAR = builder
                    .comment(" How many minecraft minutes does it take for the ageing barrel to age a drink.",
                            " Default = 1440",
                            " (1440 minecraft minutes  =  1 barrel age year)")
                    .define("MINUTES_PER_BARREL_YEAR", 20); // TODO: Don't forget to set this to actual value

            MAX_RECIPE_DIFFERENCE = builder
                    .comment(" Defines how far the ingredients in the cooking pot can be from the correct ingredients of a valid recipe.",
                            " If you exceed this limit, the drink will become scuffed.",
                            " Default = 25")
                    .define("MAX_RECIPE_DIFFERENCE", 25);

            BASE_ALCOHOL_INCREASE = builder
                    .comment(" Base value to increase player alcohol levels by, before any drink modifiers are added.",
                            " Default = 10.0")
                    .define("BASE_ALCOHOL_INCREASE", 10f);

            MIN_STRENGTH = builder
                    .comment(" Minimum strength a drink can have. 0% strength is allowed for non-alcoholic drinks.",
                            " Default = 0.1")
                    .define("MIN_STRENGTH", 0f);

            MAX_STRENGTH = builder
                    .comment(" Maximum strength a drink can have.",
                            " Default = 3.0")
                    .define("MAX_STRENGTH", 3f);

            MAX_ALCOHOL = builder
                    .comment(" Maximum alcohol a player can take before effects become fatal.",
                            " Default = 100.0")
                    .define("MAX_ALCOHOL", 100f);

            ALCOHOL_DECREASE_TICKS = builder
                    .comment(" Number of ticks before player alcohol decreases by 1.",
                            " Default = 1175")
                    .define("ALCOHOL_DECREASE_TICKS", 50);

            builder.pop();

            builder.push("Rating");

            MAX_RATING = builder
                    .comment(" Maximum rating drinks can reach.",
                            " Default = 5")
                    .define("MAX_RATING", 5);

            ING_DIFF_GREAT = builder
                    .comment(" Maximum ingredient error to reach \"Great\" rating.",
                            " Default = 2")
                    .define("ING_DIFF_GREAT", 2);

            ING_DIFF_FINE = builder
                    .comment(" Maximum ingredient error to reach \"Fine\" rating.",
                            " Default = 3")
                    .define("ING_DIFF_FINE", 3);

            ING_DIFF_POOR = builder
                    .comment(" Maximum ingredient error to reach \"Poor\" rating.",
                            " Default = 4")
                    .define("ING_DIFF_POOR", 4);


            COOK_DIFF_GREAT = builder
                    .comment(" Maximum cooking error to reach \"Great\" rating (in minutes).",
                            " Default = 1")
                    .define("COOK_DIFF_GREAT", 1);

            COOK_DIFF_FINE = builder
                    .comment(" Maximum cooking error to reach \"Fine\" rating (in minutes).",
                            " Default = 2")
                    .define("COOK_DIFF_FINE", 2);

            COOK_DIFF_POOR = builder
                    .comment(" Maximum cooking error to reach \"Poor\" rating (in minutes).",
                            " Default = 3")
                    .define("COOK_DIFF_POOR", 3);


            DISTILL_DIFF_GREAT = builder
                    .comment(" Maximum distilling error to reach \"Great\" rating.",
                            " Default = 1")
                    .define("DISTILL_DIFF_GREAT", 1);

            DISTILL_DIFF_FINE = builder
                    .comment(" Maximum distilling error to reach \"Fine\" rating.",
                            " Default = 2")
                    .define("DISTILL_DIFF_FINE", 2);

            DISTILL_DIFF_POOR = builder
                    .comment(" Maximum distilling error to reach \"Poor\" rating.",
                            " Default = 3")
                    .define("DISTILL_DIFF_POOR", 3);


            AGE_DIFF_GREAT = builder
                    .comment(" Maximum barrel ageing error to reach \"Great\" rating (in years).",
                            " Default = 2")
                    .define("AGE_DIFF_GREAT", 2);

            AGE_DIFF_FINE = builder
                    .comment(" Maximum barrel ageing error to reach \"Fine\" rating (in years).",
                            " Default = 3")
                    .define("AGE_DIFF_FINE", 3);

            AGE_DIFF_POOR = builder
                    .comment(" Maximum barrel ageing error to reach \"Poor\" rating (in years).",
                            " Default = 4")
                    .define("AGE_DIFF_POOR", 4);

            builder.pop();
        }
    }
}
