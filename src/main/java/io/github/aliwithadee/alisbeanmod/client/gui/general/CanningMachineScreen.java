package io.github.aliwithadee.alisbeanmod.client.gui.general;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.aliwithadee.alisbeanmod.AlisBeanMod;
import io.github.aliwithadee.alisbeanmod.common.general.menu.CanningMachineMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class CanningMachineScreen extends AbstractContainerScreen<CanningMachineMenu> {

    private final ResourceLocation TEXTURE = new ResourceLocation(AlisBeanMod.MOD_ID,
            "textures/gui/canning_machine_gui.png");

    private int capacity;
    private int energy;

    public CanningMachineScreen(CanningMachineMenu container, Inventory inv, Component titleIn) {
        super(container, inv, titleIn);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        getEnergy();
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(poseStack, mouseX, mouseY);
    }

    public void getEnergy() {
        capacity = this.menu.getMaxEnergy();
        energy = this.menu.getEnergy();
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
        this.blit(poseStack, i + 95, j + 40, 176, 0, progress + 1, 14);

        // Energy Meter
        float e = capacity != 0 ? energy * 46f / capacity : 0;
        int e_offset = e > 0 && e < 1 ? 1 : (int)e;

        this.blit(poseStack, i + 25, j + 68 - e_offset, 176, 60 - e_offset, 10, e_offset + 1);
    }

    @Override
    protected void renderTooltip(PoseStack poseStack, int mouseX, int mouseY) {
        int i = this.getGuiLeft();
        int j = this.getGuiTop();

        if (mouseX >= (i + 25) && mouseX <= (i + 34)) {
            if (mouseY >= (j + 22) && mouseY <= (j + 67)) {
                TextComponent text = new TextComponent(energy + "BE/" + capacity + "BE");

                this.renderTooltip(poseStack, text, mouseX, mouseY);
            }
        }
        super.renderTooltip(poseStack, mouseX, mouseY);
    }
}
