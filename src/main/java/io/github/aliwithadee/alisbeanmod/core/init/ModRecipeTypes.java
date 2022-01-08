package io.github.aliwithadee.alisbeanmod.core.init;

import io.github.aliwithadee.alisbeanmod.AlisBeanMod;
import io.github.aliwithadee.alisbeanmod.core.data.recipe.general.CanningMachineRecipe;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipeTypes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, AlisBeanMod.MOD_ID);

    // Recipe Serializers
    public static final RegistryObject<CanningMachineRecipe.Serializer> CANNING_SERIALIZER =
            RECIPE_SERIALIZERS.register("canning", CanningMachineRecipe.Serializer::new);

    // Recipe Types
    public static RecipeType<CanningMachineRecipe> CANNING_RECIPE =
            registerType(CanningMachineRecipe.TYPE_ID, new CanningMachineRecipe.CanningMachineRecipeType());

    public static <T extends Recipe<?>> RecipeType<T> registerType(ResourceLocation id, RecipeType<T> type) {
        Registry.register(Registry.RECIPE_TYPE, id, type);
        return type;
    }

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
    }
}
