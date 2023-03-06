package dev.blackhig.zhebushigudu.lover.features.modules.player;

import dev.blackhig.zhebushigudu.lover.lover;
import java.util.Objects;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemTool;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemArmor;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import dev.blackhig.zhebushigudu.lover.util.RenderUtil;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import dev.blackhig.zhebushigudu.lover.features.Feature;
import dev.blackhig.zhebushigudu.lover.event.events.Render3DEvent;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;

public class NameTags extends Module
{
    static final boolean assertionsDisabled;
    private final Setting<Boolean> invisible;
    
    public NameTags() {
        super("NameTags", "Better Nametags", Category.PLAYER, false, false, false);
        this.invisible = this.register(new Setting<>("Invisibles", true));
    }
    
    @Override
    public void onRender3D(final Render3DEvent event) {
        if (!Feature.fullNullCheck()) {
            for (final EntityPlayer player : NameTags.mc.world.playerEntities) {
                if (player.getDistance(NameTags.mc.player) <= 30.0 && !player.equals(NameTags.mc.player) && player.isEntityAlive()) {
                    if (player.isInvisible() && !this.invisible.getValue()) {
                        continue;
                    }
                    this.renderNameTag(player, this.interpolate(player.lastTickPosX, player.posX, event.getPartialTicks()) - NameTags.mc.getRenderManager().viewerPosX, this.interpolate(player.lastTickPosY, player.posY, event.getPartialTicks()) - NameTags.mc.getRenderManager().viewerPosY, this.interpolate(player.lastTickPosZ, player.posZ, event.getPartialTicks()) - NameTags.mc.getRenderManager().viewerPosZ, event.getPartialTicks());
                }
            }
        }
    }
    
    private void renderNameTag(final EntityPlayer player, final double x, final double y, final double z, final float delta) {
        final double tempY = y + (player.isSneaking() ? 0.5 : 0.7);
        final Entity camera = NameTags.mc.getRenderViewEntity();
        if (NameTags.assertionsDisabled || camera != null) {
            assert camera != null;
            final double originalPositionX = camera.posX;
            final double originalPositionY = camera.posY;
            final double originalPositionZ = camera.posZ;
            camera.posX = this.interpolate(camera.prevPosX, camera.posX, delta);
            camera.posY = this.interpolate(camera.prevPosY, camera.posY, delta);
            camera.posZ = this.interpolate(camera.prevPosZ, camera.posZ, delta);
            final String displayTag = this.getDisplayTag(player);
            final double distance = camera.getDistance(x + NameTags.mc.getRenderManager().viewerPosX, y + NameTags.mc.getRenderManager().viewerPosY, z + NameTags.mc.getRenderManager().viewerPosZ);
            final int width = this.renderer.getStringWidth(displayTag) / 2;
            final double scale = (distance <= 6.0) ? 0.0245 : ((0.0018 + 4.5 * (distance)) / 1000.0);
            GlStateManager.pushMatrix();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enablePolygonOffset();
            GlStateManager.doPolygonOffset(1.0f, -1500000.0f);
            GlStateManager.disableLighting();
            GlStateManager.translate((float)x, (float)tempY + 1.4f, (float)z);
            GlStateManager.rotate(-NameTags.mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(NameTags.mc.getRenderManager().playerViewX, (NameTags.mc.gameSettings.thirdPersonView == 2) ? -1.0f : 1.0f, 0.0f, 0.0f);
            GlStateManager.scale(-scale, -scale, scale);
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.enableBlend();
            RenderUtil.drawRect((float)(-width - 2), (float)(-(this.renderer.getFontHeight() + 1)), width + 2.0f, 1.5f, 1426063360);
            GlStateManager.disableBlend();
            final ItemStack renderMainHand = player.getHeldItemMainhand().copy();
            GlStateManager.pushMatrix();
            int xOffset = -8;
            for (ItemStack itemStack : player.inventory.armorInventory) {
                if (itemStack == null) {
                    continue;
                }
                xOffset -= 8;
            }
            final int xOffset2 = xOffset - 8;
            this.renderItemStack(player.getHeldItemOffhand().copy(), xOffset2);
            int xOffset3 = xOffset2 + 16;
            for (final ItemStack stack2 : player.inventory.armorInventory) {
                if (stack2 == null) {
                    continue;
                }
                this.renderItemStack(stack2.copy(), xOffset3);
                xOffset3 += 16;
            }
            this.renderItemStack(renderMainHand, xOffset3);
            GlStateManager.popMatrix();
            this.renderer.drawStringWithShadow(displayTag, (float)(-width), (float)(-(this.renderer.getFontHeight() - 1)), this.getDisplayColour(player));
            camera.posX = originalPositionX;
            camera.posY = originalPositionY;
            camera.posZ = originalPositionZ;
            GlStateManager.enableDepth();
            GlStateManager.disableBlend();
            GlStateManager.disablePolygonOffset();
            GlStateManager.doPolygonOffset(1.0f, 1500000.0f);
            GlStateManager.popMatrix();
            return;
        }
        throw new AssertionError();
    }
    
    private void renderItemStack(final ItemStack stack, final int x) {
        GlStateManager.pushMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.clear(256);
        RenderHelper.enableStandardItemLighting();
        NameTags.mc.getRenderItem().zLevel = -150.0f;
        GlStateManager.disableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.disableCull();
        NameTags.mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, -26);
        NameTags.mc.getRenderItem().renderItemOverlays(NameTags.mc.fontRenderer, stack, x, -26);
        NameTags.mc.getRenderItem().zLevel = 0.0f;
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableCull();
        GlStateManager.enableAlpha();
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        GlStateManager.disableDepth();
        this.renderEnchantmentText(stack, x);
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GlStateManager.popMatrix();
    }
    
    private void renderEnchantmentText(final ItemStack stack, final int x) {
        int enchantmentY = -26 - 8;
        if (stack.getItem() == Items.GOLDEN_APPLE && stack.hasEffect()) {
            this.renderer.drawStringWithShadow("God", (float)(x * 2), (float)enchantmentY, -3977919);
            enchantmentY -= 8;
        }
        if (stack.getItem() instanceof ItemArmor || stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemTool || stack.getItem() instanceof ItemShield) {
            final int dmg = 100 - (int)(100.0f * (1.0f - (stack.getMaxDamage() - (float)stack.getItemDamage()) / stack.getMaxDamage()));
            final ChatFormatting color = (dmg >= 60) ? ChatFormatting.GREEN : ((dmg >= 25) ? ChatFormatting.YELLOW : ChatFormatting.RED);
            this.renderer.drawStringWithShadow(color + "" + dmg + "%", (float)(x * 2), (float)enchantmentY, -1);
        }
    }
    
    private String getDisplayTag(final EntityPlayer player) {
        String name2 = player.getDisplayName().getFormattedText();
        if (name2.contains(NameTags.mc.getSession().getUsername())) {
            name2 = "You";
        }
        final float health;
        final ChatFormatting color = ((health = player.getHealth() + player.getAbsorptionAmount()) > 18.0f) ? ChatFormatting.GREEN : ((health > 16.0f) ? ChatFormatting.DARK_GREEN : ((health > 12.0f) ? ChatFormatting.YELLOW : ((health > 8.0f) ? ChatFormatting.GOLD : ((health > 5.0f) ? ChatFormatting.RED : ChatFormatting.DARK_RED))));
        String pingStr = "";
        try {
            pingStr = pingStr + Objects.requireNonNull(NameTags.mc.getConnection()).getPlayerInfo(player.getUniqueID()).getResponseTime() + "ms ";
        }
        catch (final Exception ignored) {}
        final String name3 = (Math.floor(health) == health) ? (name2 + color + ' ' + ((health > 0.0f) ? Integer.valueOf((int)Math.floor(health)) : "dead")) : (name2 + color + ' ' + ((health > 0.0f) ? Integer.valueOf((int)health) : "dead"));
        return pingStr + name3;
    }
    
    private int getDisplayColour(final EntityPlayer player) {
        int colour = -1;
        if (lover.friendManager.isFriend(player)) {
            return -11157267;
        }
        if (player.isInvisible()) {
            colour = -1113785;
        }
        else if (player.isSneaking()) {
            colour = -6481515;
        }
        return colour;
    }
    
    private double interpolate(final double previous, final double current, final float delta) {
        return (current - previous) * delta + previous;
    }
    
    static {
        assertionsDisabled = !NameTags.class.desiredAssertionStatus();
    }
}
