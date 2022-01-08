package io.github.aliwithadee.alisbeanmod.core.data.recipe.general;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.aliwithadee.alisbeanmod.AlisBeanMod;
import io.github.aliwithadee.alisbeanmod.core.init.ModBlocks;
import io.github.aliwithadee.alisbeanmod.core.init.ModRecipeTypes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class CanningMachineRecipe implements Recipe<Container> {

    public static final ResourceLocation TYPE_ID = new ResourceLocation(AlisBeanMod.MOD_ID, "canning");

    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> inputs;
    private final int processTime;

    public CanningMachineRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> inputs, int processTime) {
        this.id = id;
        this.output = output;
        this.inputs = inputs;
        this.processTime = processTime;
    }

    // Returns whether the state of the tile entity inventory matches an existing recipe.
    // matches() is called by the recipeManager() in the tile entity class, when we try to get the recipe
    @Override
    public boolean matches(Container inv, Level level) {
        // Check if the first ingredient matches whatever is actually in the first slot of the inventory
        if(inputs.get(0).test(inv.getItem(0))) {
            return inputs.get(1).test(inv.getItem(1));
        }

        return false;
    }

    @Override
    public ItemStack assemble(Container pContainer) {
        return output;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return output.copy();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return inputs;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeType<?> getType() {
        return Registry.RECIPE_TYPE.getOptional(TYPE_ID).get();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.CANNING_SERIALIZER.get();
    }

    public int getProcessTime() {
        return processTime;
    }

    public ItemStack getIcon() {
        return new ItemStack(ModBlocks.CANNING_MACHINE.get());
    }

    public static class CanningMachineRecipeType implements RecipeType<CanningMachineRecipe> {

        @Override
        public String toString() {
            return CanningMachineRecipe.TYPE_ID.toString();
        }
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>>
            implements RecipeSerializer<CanningMachineRecipe> {

        // This serializes a .json file and converts it into a CanningMachineRecipe
        @Override
        public CanningMachineRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients"); // Get inputs array

            NonNullList<Ingredient> inputs = NonNullList.withSize(2, Ingredient.EMPTY);
            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i))); // Get input ingredients
            }

            // Get output item stack
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
            int processTime = GsonHelper.getAsInt(json, "processtime"); // Get process time

            return new CanningMachineRecipe(recipeId, output, inputs, processTime);
        }

        // Read and write methods for the Network need to be symmetrical,
        // and they need to be read in the same order that they were written
        @Nullable
        @Override
        public CanningMachineRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {

            NonNullList<Ingredient> inputs = NonNullList.withSize(buffer.readInt(), Ingredient.EMPTY); // Read size of inputs
            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(buffer)); // Read input ingredients
            }

            ItemStack output = buffer.readItem(); // Read output ItemStack
            int processTime = buffer.readInt(); // Read process time

            return new CanningMachineRecipe(recipeId, output, inputs, processTime);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, CanningMachineRecipe recipe) {
            buffer.writeInt(recipe.getIngredients().size()); // Write size of inputs list
            for (Ingredient ing : recipe.getIngredients()) {
                ing.toNetwork(buffer); // Write input ingredients
            }
            buffer.writeItemStack(recipe.getResultItem(), false); // Write output ItemStack
            buffer.writeInt(recipe.processTime); // Write process time
        }
    }
}
