package io.github.aliwithadee.alisbeanmod.core.events;

import io.github.aliwithadee.alisbeanmod.AlisBeanMod;
import io.github.aliwithadee.alisbeanmod.common.general.block.ObeliskBlock;
import io.github.aliwithadee.alisbeanmod.core.cooking.drinks.alcohol.AlcoholCapabilityAttacher;
import io.github.aliwithadee.alisbeanmod.core.cooking.drinks.alcohol.CapabilityAlcohol;
import io.github.aliwithadee.alisbeanmod.core.cooking.drinks.alcohol.IAlcoholCapability;
import io.github.aliwithadee.alisbeanmod.core.util.BeanModCommonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AlisBeanMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventBusEvents {
    public static int playerTicks = 0;

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        AlcoholCapabilityAttacher.attach(event);
    }

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isServer()) {
            if (event.phase == TickEvent.Phase.END) {
                playerTicks++;
            } else if (event.phase == TickEvent.Phase.START) {
                LazyOptional<IAlcoholCapability> cap = event.player.getCapability(CapabilityAlcohol.ALCOHOL_CAPABILITY);
                cap.ifPresent((alcoholCap) -> {
                    if (alcoholCap.getAlcohol() > 0) {
                        if (playerTicks >= BeanModCommonConfig.ALCOHOL_DECREASE_TICKS.get()) {
                            playerTicks = 0;
                            alcoholCap.removeAlcohol(1f);
                            System.out.println("Alcohol decreased to: " + alcoholCap.getAlcohol());
                        }
                    }
                });
            }
        }
    }

    private static void tillSoil(Level level, BlockPos pos, Player player) {
        level.playSound(player, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
        if (!level.isClientSide) {
            level.setBlock(pos, Blocks.FARMLAND.defaultBlockState(), 11);
        }
    }

    @SubscribeEvent
    public static void onUseHoe(UseHoeEvent event) {
        UseOnContext context = event.getContext();
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        if (state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.DIRT) || state.is(Blocks.DIRT_PATH)) {
            BlockState stateAbove = level.getBlockState(pos.above());
            if (stateAbove.getBlock() instanceof ObeliskBlock) {
                tillSoil(level, pos, context.getPlayer());
                event.setResult(Event.Result.ALLOW);
            }
        } else if (state.getBlock() instanceof ObeliskBlock) {
            BlockState stateBelow = level.getBlockState(pos.below());
            if (stateBelow.is(Blocks.GRASS_BLOCK) || stateBelow.is(Blocks.DIRT) || stateBelow.is(Blocks.DIRT_PATH)) {
                tillSoil(level, pos.below(), context.getPlayer());
                event.setResult(Event.Result.ALLOW);
            }
        }
    }
}
