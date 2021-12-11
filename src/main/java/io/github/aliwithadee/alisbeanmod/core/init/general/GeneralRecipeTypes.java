package io.github.aliwithadee.alisbeanmod.core.init.general;

import io.github.aliwithadee.alisbeanmod.AlisBeanMod;
import io.github.aliwithadee.alisbeanmod.core.data.recipe.general.CanningMachineRecipe;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class GeneralRecipeTypes {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, AlisBeanMod.MOD_ID);

    // Register Recipe Serializers
    public static final RegistryObject<CanningMachineRecipe.Serializer> CANNING_SERIALIZER =
            RECIPE_SERIALIZERS.register("canning", CanningMachineRecipe.Serializer::new);

    // Recipe Types
    public static RecipeType<CanningMachineRecipe> CANNING_RECIPE =
            new CanningMachineRecipe.CanningMachineRecipeType();

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);

        // Register Recipe Types
        Registry.register(Registry.RECIPE_TYPE, CanningMachineRecipe.TYPE_ID, CANNING_RECIPE);
    }
}
