package net.stardomga.stardoms_colors.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.SheepEntityRenderState;

@Environment(EnvType.CLIENT)
public class CustomSheepEntityRenderState extends SheepEntityRenderState {
    private int customColor = -1;
    private boolean hasCustomColor = false;

    public void setCustomColor(int color) {
        this.customColor = color;
        this.hasCustomColor = true;
    }

    public int getCustomColor() {
        return this.customColor;
    }

    public boolean hasCustomColor() {
        return this.hasCustomColor;
    }
}