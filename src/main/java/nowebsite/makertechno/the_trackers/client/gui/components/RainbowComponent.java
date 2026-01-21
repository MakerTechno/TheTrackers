package nowebsite.makertechno.the_trackers.client.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class RainbowComponent extends BaseComponent {
    private final int intervalColor;
    private int intervalCount;
    private float hue;
    private float r = 1.0F, g = 1.0F, b = 1.0F;
    public RainbowComponent(IRenderElement element, int intervalColor) {
        super(element);
        this.intervalColor = intervalColor;
    }
    public static boolean isValidPatterns(int intervalColor) {
        return intervalColor > 1;
    }
    @Override
    public void render(@NotNull GuiGraphics graphics, float partialTick) {
        if (++intervalCount >= intervalColor) {
            osc();
            intervalCount = 0;
        }
        RenderSystem.setShaderColor(r, g, b, 1.0f);
        super.render(graphics, partialTick);
    }

    private void osc() {
        hue += 0.01F;
        if (hue > 1f) hue -= 1f;
        int rgb = Color.HSBtoRGB(hue, 1f, 1f);

        r = ((rgb >> 16) & 0xFF) / 255f;
        g = ((rgb >> 8) & 0xFF) / 255f;
        b = (rgb & 0xFF) / 255f;
    }
}
