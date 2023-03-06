package dev.blackhig.zhebushigudu.lover.notification;

import dev.blackhig.zhebushigudu.lover.lover;
import dev.blackhig.zhebushigudu.lover.util.AnimationUtils;
import dev.blackhig.zhebushigudu.lover.util.RenderUtil;
import dev.blackhig.zhebushigudu.lover.util.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

public class Notification
{
    public String text;
    public double width;
    public double height;
    public float x;
    Type type;
    public float y;
    public float position;
    public boolean in;
    AnimationUtils animationUtils;
    AnimationUtils yAnimationUtils;
    
    public Notification(final String text, final Type type) {
        this.height = 30.0;
        this.in = true;
        this.animationUtils = new AnimationUtils();
        this.yAnimationUtils = new AnimationUtils();
        this.text = text;
        this.type = type;
        this.width = Minecraft.getMinecraft().fontRenderer.getStringWidth(text) + 25;
        this.x = (float)this.width;
    }
    
    public void onRender() {
        int i = 0;
        for (final Notification notification : lover.notificationsManager.notifications) {
            if (notification == this) {
                break;
            }
            ++i;
        }
        this.y = this.yAnimationUtils.animate((float)((float)i * (this.height + 5.0)), this.y, 0.1f);
        final ScaledResolution sr = new ScaledResolution(Wrapper.getMinecraft());
        RenderUtil.drawRectS(sr.getScaledWidth() + this.x - this.width, sr.getScaledHeight() - 50 - this.y - this.height, sr.getScaledWidth() + this.x, sr.getScaledHeight() - 50 - this.y, new Color(0, 0, 0, 165).getRGB());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(this.text, (float)(sr.getScaledWidth() + this.x - this.width + 10.0), sr.getScaledHeight() - 50.0f - this.y - 18.0f, new Color(204, 204, 204, 232).getRGB());
    }
    
    public enum Type
    {
        Success, 
        Error, 
        Info
    }
}
