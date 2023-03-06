package dev.blackhig.zhebushigudu.lover.util.mainmenu;

import java.awt.Color;

public class FontManager
{
    public static Boolean ready;
    public static CFontRenderer iconFont;
    public static CFontRenderer fontRenderer;
    
    public static void init() {
        FontManager.iconFont = new CFontRenderer(new CFont.CustomFont("/assets/minecraft/fonts/Icon.ttf", 22.0f, 0), true, false);
        FontManager.fontRenderer = new CFontRenderer(new CFont.CustomFont("/assets/minecraft/fonts/Comfortaa-Bold.ttf", 18.0f, 0), true, false);
    }
    
    public static int getWidth(final String str) {
        return FontManager.fontRenderer.getStringWidth(str);
    }
    
    public static int getHeight() {
        return FontManager.fontRenderer.getHeight() + 2;
    }
    
    public static void draw(final String str, final int x, final int y, final int color) {
        FontManager.fontRenderer.drawString(str, (float)x, (float)y, color);
    }
    
    public static void draw(final String str, final int x, final int y, final Color color) {
        FontManager.fontRenderer.drawString(str, (float)x, (float)y, color.getRGB());
    }
    
    public static int getIconWidth() {
        return FontManager.iconFont.getStringWidth("q");
    }
    
    public static int getIconHeight() {
        return FontManager.iconFont.getHeight();
    }
    
    public static void drawIcon(final int x, final int y, final int color) {
        FontManager.iconFont.drawString("q", (float)x, (float)y, color);
    }
    
    public static void drawIcon(final int x, final int y, final Color color) {
        FontManager.iconFont.drawString("q", (float)x, (float)y, color.getRGB());
    }
    
    static {
        FontManager.ready = false;
    }
}
