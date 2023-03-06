package dev.blackhig.zhebushigudu.lover.features.modules.misc;

import dev.blackhig.zhebushigudu.lover.event.events.Render2DEvent;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;
import dev.blackhig.zhebushigudu.lover.features.modules.client.ClickGui;
import dev.blackhig.zhebushigudu.lover.util.ColorUtil;
import dev.blackhig.zhebushigudu.lover.util.RenderUtil;
import dev.blackhig.zhebushigudu.lover.util.Timer;
import dev.blackhig.zhebushigudu.lover.util.Util;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ToolTips extends Module
{
    private static final ResourceLocation SHULKER_GUI_TEXTURE;
    private static ToolTips INSTANCE;
    public Map<EntityPlayer, ItemStack> spiedPlayers;
    public Map<EntityPlayer, Timer> playerTimers;

    public ToolTips() {
        super("ShulkerViewer", "Several tweaks for tooltips.", Category.MISC, true, false, false);
        this.spiedPlayers = new ConcurrentHashMap<>();
        this.playerTimers = new ConcurrentHashMap<>();
        this.setInstance();
    }
    
    public static ToolTips getInstance() {
        if (ToolTips.INSTANCE == null) {
            ToolTips.INSTANCE = new ToolTips();
        }
        return ToolTips.INSTANCE;
    }
    
    public static void displayInv(final ItemStack stack, final String name) {
        try {
            final Item item = stack.getItem();
            final TileEntityShulkerBox entityBox = new TileEntityShulkerBox();
            final ItemShulkerBox shulker = (ItemShulkerBox)item;
            entityBox.blockType = shulker.getBlock();
            entityBox.setWorld(ToolTips.mc.world);
            assert stack.getTagCompound() != null;
            ItemStackHelper.loadAllItems(stack.getTagCompound().getCompoundTag("BlockEntityTag"), entityBox.items);
            entityBox.readFromNBT(stack.getTagCompound().getCompoundTag("BlockEntityTag"));
            entityBox.setCustomName((name == null) ? stack.getDisplayName() : name);
            new Thread(() -> {
                try {
                    Thread.sleep(200L);
                }
                catch (final InterruptedException ignored) {}
                ToolTips.mc.player.displayGUIChest(entityBox);
            }).start();
        }
        catch (final Exception ignored) {}
    }
    
    private void setInstance() {
        ToolTips.INSTANCE = this;
    }
    
    @Override
    public void onUpdate() {
        if (fullNullCheck()) {
            return;
        }
        for (final EntityPlayer player : ToolTips.mc.world.playerEntities) {
            if (player != null && player.getHeldItemMainhand().getItem() instanceof ItemShulkerBox) {
                if (ToolTips.mc.player == player) {
                    continue;
                }
                final ItemStack stack = player.getHeldItemMainhand();
                this.spiedPlayers.put(player, stack);
            }
        }
    }
    
    @Override
    public void onRender2D(final Render2DEvent event) {
        if (fullNullCheck()) {
            return;
        }
        final int x = -3;
        int y = 124;
        for (final EntityPlayer player : ToolTips.mc.world.playerEntities) {
            if (this.spiedPlayers.get(player) == null) {
                continue;
            }
            if (player.getHeldItemMainhand() == null || !(player.getHeldItemMainhand().getItem() instanceof ItemShulkerBox)) {
                final Timer playerTimer = this.playerTimers.get(player);
                if (playerTimer == null) {
                    final Timer timer = new Timer();
                    timer.reset();
                    this.playerTimers.put(player, timer);
                }
                else if (playerTimer.passedS(3.0)) {
                    continue;
                }
            }
            else {
                final Timer playerTimer;
                if (player.getHeldItemMainhand().getItem() instanceof ItemShulkerBox && (playerTimer = this.playerTimers.get(player)) != null) {
                    playerTimer.reset();
                    this.playerTimers.put(player, playerTimer);
                }
            }
            final ItemStack stack = this.spiedPlayers.get(player);
            this.renderShulkerToolTip(stack, x, y, player.getName());
            y += 78;
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void makeTooltip(final ItemTooltipEvent event) {
    }
    
    public void renderShulkerToolTip(final ItemStack stack, final int x, final int y, final String name) {
        final NBTTagCompound tagCompound = stack.getTagCompound();
        final NBTTagCompound blockEntityTag;
        if (tagCompound != null && tagCompound.hasKey("BlockEntityTag", 10) && (blockEntityTag = tagCompound.getCompoundTag("BlockEntityTag")).hasKey("Items", 9)) {
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            Util.mc.getTextureManager().bindTexture(ToolTips.SHULKER_GUI_TEXTURE);
            RenderUtil.drawTexturedRect(x, y, 0, 0, 176, 16, 500);
            RenderUtil.drawTexturedRect(x, y + 16, 0, 16, 176, 57, 500);
            RenderUtil.drawTexturedRect(x, y + 16 + 54, 0, 160, 176, 8, 500);
            GlStateManager.disableDepth();
            final Color color = new Color(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue(), 200);
            this.renderer.drawStringWithShadow((name == null) ? stack.getDisplayName() : name, (float)(x + 8), (float)(y + 6), ColorUtil.toRGBA(color));
            GlStateManager.enableDepth();
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableColorMaterial();
            GlStateManager.enableLighting();
            final NonNullList nonnulllist = NonNullList.withSize(27, (Object)ItemStack.EMPTY);
            ItemStackHelper.loadAllItems(blockEntityTag, nonnulllist);
            for (int i = 0; i < nonnulllist.size(); ++i) {
                final int iX = x + i % 9 * 18 + 8;
                final int iY = y + i / 9 * 18 + 18;
                final ItemStack itemStack = (ItemStack)nonnulllist.get(i);
                ToolTips.mc.getItemRenderer().itemRenderer.zLevel = 501.0f;
                RenderUtil.itemRender.renderItemAndEffectIntoGUI(itemStack, iX, iY);
                RenderUtil.itemRender.renderItemOverlayIntoGUI(ToolTips.mc.fontRenderer, itemStack, iX, iY, (String)null);
                ToolTips.mc.getItemRenderer().itemRenderer.zLevel = 0.0f;
            }
            GlStateManager.disableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
    
    static {
        SHULKER_GUI_TEXTURE = new ResourceLocation("textures/gui/container/shulker_box.png");
        ToolTips.INSTANCE = new ToolTips();
    }
}
