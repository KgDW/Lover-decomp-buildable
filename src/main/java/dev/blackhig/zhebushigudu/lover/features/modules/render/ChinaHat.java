package dev.blackhig.zhebushigudu.lover.features.modules.render;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.Entity;
import java.awt.Color;
import dev.blackhig.zhebushigudu.lover.event.events.Render3DEvent;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;

public class ChinaHat extends Module
{
    public Setting<Integer> red;
    public Setting<Integer> green;
    public Setting<Integer> blue;
    public Setting<Integer> red2;
    public Setting<Integer> green2;
    public Setting<Integer> blue2;
    public Setting<Integer> points;
    public Setting<Boolean> firstP;
    
    public ChinaHat() {
        super("ChinaHat", "", Category.RENDER, true, false, false);
        this.red = this.register(new Setting<>("Red", 0, 0, 255));
        this.green = this.register(new Setting<>("Green", 255, 0, 255));
        this.blue = this.register(new Setting<>("Blue", 0, 0, 255));
        this.red2 = this.register(new Setting<>("Red2", 0, 0, 255));
        this.green2 = this.register(new Setting<>("Green2", 255, 0, 255));
        this.blue2 = this.register(new Setting<>("Blue2", 0, 0, 255));
        this.points = this.register(new Setting<>("Points", 12, 4, 64));
        this.firstP = this.register(new Setting<>("FirstPerson", false));
    }
    
    @Override
    public void onRender3D(final Render3DEvent render3DEvent) {
        float f = 0.0f;
        if (ChinaHat.mc.gameSettings.thirdPersonView != 0 || this.firstP.getValue()) {
            for (int i = 0; i < 400; ++i) {
                f = (float)getGradientOffset(new Color(this.red2.getValue(), this.green2.getValue(), this.blue2.getValue(), 255), new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), 255), Math.abs(System.currentTimeMillis() / 7L - i / 2) / 120.0).getRGB();
                if (ChinaHat.mc.player.isElytraFlying()) {
                    drawHat(ChinaHat.mc.player, 0.009 + i * 0.0014, render3DEvent.getPartialTicks(), this.points.getValue(), 2.0f, 1.1f - i * 7.85E-4f - (ChinaHat.mc.player.isSneaking() ? 0.07f : 0.03f), (int)f);
                }
                else if (ChinaHat.mc.player.isSneaking()) {
                    drawHat(ChinaHat.mc.player, 0.009 + i * 0.0014, render3DEvent.getPartialTicks(), this.points.getValue(), 2.0f, 1.1f - i * 7.85E-4f - (ChinaHat.mc.player.isSneaking() ? 0.07f : 0.03f), (int)f);
                }
                else {
                    drawHat(ChinaHat.mc.player, 0.009 + i * 0.0014, render3DEvent.getPartialTicks(), this.points.getValue(), 2.0f, 2.2f - i * 7.85E-4f - (ChinaHat.mc.player.isSneaking() ? 0.07f : 0.03f), (int)f);
                }
            }
        }
    }
    
    public static void drawHat(final Entity entity, final double d, final float f, final int n, final float f2, final float f3, final int n2) {
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glDepthMask(false);
        GL11.glLineWidth(f2);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2929);
        GL11.glBegin(3);
        final float f4 = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * Minecraft.getMinecraft().timer.renderPartialTicks;
        final float f5 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * Minecraft.getMinecraft().timer.renderPartialTicks;
        final double d2 = interpolate(entity.prevPosX, entity.posX, f) - Minecraft.getMinecraft().getRenderManager().viewerPosX;
        final double d3 = interpolate(entity.prevPosY + f3, entity.posY + f3, f) - Minecraft.getMinecraft().getRenderManager().viewerPosY;
        final double d4 = interpolate(entity.prevPosZ, entity.posZ, f) - Minecraft.getMinecraft().getRenderManager().viewerPosZ;
        GL11.glColor4f(new Color(n2).getRed() / 255.0f, new Color(n2).getGreen() / 255.0f, new Color(n2).getBlue() / 255.0f, 0.15f);
        for (int i = 0; i <= n; ++i) {
            GL11.glVertex3d(d2 + d * Math.cos(i * 3.141592653589793 * 2.0 / n), d3, d4 + d * Math.sin(i * 3.141592653589793 * 2.0 / n));
        }
        GL11.glEnd();
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glEnable(2929);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
    }
    
    public static Color getGradientOffset(final Color color, final Color color2, double d) {
        if (d > 1.0) {
            final double d2 = d % 1.0;
            final int n = (int)d;
            d = ((n % 2 == 0) ? d2 : (1.0 - d2));
        }
        final double d2 = 1.0 - d;
        final int n = (int)(color.getRed() * d2 + color2.getRed() * d);
        final int n2 = (int)(color.getGreen() * d2 + color2.getGreen() * d);
        final int n3 = (int)(color.getBlue() * d2 + color2.getBlue() * d);
        return new Color(n, n2, n3);
    }
    
    public static double interpolate(final double d, final double d2, final double d3) {
        return d + (d2 - d) * d3;
    }
}
