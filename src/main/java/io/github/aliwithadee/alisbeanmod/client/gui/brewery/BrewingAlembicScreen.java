package io.github.aliwithadee.alisbeanmod.client.gui.brewery;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.aliwithadee.alisbeanmod.AlisBeanMod;
import io.github.aliwithadee.alisbeanmod.common.cooking.block.BrewingAlembicBE;
import io.github.aliwithadee.alisbeanmod.common.cooking.menu.BrewingAlembicMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class BrewingAlembicScreen extends AbstractContainerScreen<BrewingAlembicMenu> {

    private final ResourceLocation TEXTURE = new ResourceLocation(AlisBeanMod.MOD_ID,
            "textures/gui/brewing_alembic_gui.png");
    private static final int[] BUBBLE_HEIGHTS = new int[]{0, 5, 10, 15, 19, 23, 28};

    public BrewingAlembicScreen(BrewingAlembicMenu container, Inventory inv, Component titleIn) {
        super(container, inv, titleIn);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        int i = this.getGuiLeft();
        int j = this.getGuiTop();

        // Main GUI
        this.blit(poseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);

        // Progress bar
        int ticks = this.menu.getDistillTicks();
        int progress = ticks != 0 ? (ticks * 29) / BrewingAlembicBE.TICKS_TO_DISTILL : 0;
        this.blit(poseStack, i + 95, j + 21, 176, 28, 58, progress + 1);

        // Bubbles
        int height = BUBBLE_HEIGHTS[ticks / 2 % 7];
        if (this.menu.slotHasInput(0)) {
            this.blit(poseStack, i + 27, j + 48 - height, 176, 28 - height, 11, height);
        }
        if (this.menu.slotHasInput(1)) {
            this.blit(poseStack, i + 47, j + 48 - height, 176, 28 - height, 11, height);
        }
        if (this.menu.slotHasInput(2)) {
            this.blit(poseStack, i + 67, j + 48 - height, 176, 28 - height, 11, height);
        }
    }
}
