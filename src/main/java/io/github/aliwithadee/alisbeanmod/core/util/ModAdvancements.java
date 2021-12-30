package io.github.aliwithadee.alisbeanmod.core.util;

import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.ServerLifecycleHooks;

public class ModAdvancements {
    public static Advancement getAdvancement(ResourceLocation id) {
        return ServerLifecycleHooks.getCurrentServer().getAdvancements().getAdvancement(id);
    }

    public static boolean completeAdvancement(ServerPlayer player, ResourceLocation id, String criterion) {
        Advancement adv = getAdvancement(id);

        if (adv != null)
            return player.getAdvancements().award(adv, criterion);

        return false;
    }

    public static boolean playerHasAdvancement(ServerPlayer player, ResourceLocation id) {
        Advancement adv = getAdvancement(id);

        if (adv != null)
            return player.getAdvancements().getOrStartProgress(adv).isDone();

        return false;
    }
}
