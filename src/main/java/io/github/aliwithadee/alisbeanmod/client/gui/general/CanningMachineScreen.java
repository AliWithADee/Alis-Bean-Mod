package io.github.aliwithadee.alisbeanmod.client.gui.general;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.aliwithadee.alisbeanmod.AlisBeanMod;
import io.github.aliwithadee.alisbeanmod.common.general.menu.CanningMachineMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CanningMachineScreen extends AbstractContainerScreen<CanningMachineMenu> {

    private final ResourceLocation TEXTURE = new ResourceLocation(AlisBeanMod.MOD_ID,
            "textures/gui/canning_machine_gui.png");

    public CanningMachineScreen(CanningMachineMenu container, Inventory inv, Component titleIn) {
        super(container, inv, titleIn);
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
        int progress = this.menu.getCanningProgress();
        this.blit(poseStack, i + 95, j + 44, 176, 0, progress + 1, 14);
    }
}
