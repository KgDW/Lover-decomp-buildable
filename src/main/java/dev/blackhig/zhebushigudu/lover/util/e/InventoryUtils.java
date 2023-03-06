package dev.blackhig.zhebushigudu.lover.util.e;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;

public class InventoryUtils
{
    protected static Minecraft mc;
    public static short actionNumber;
    private static int currentItem;
    
    public static int getSlot() {
        return InventoryUtils.mc.player.inventory.currentItem;
    }
    
    public static void setSlot(final int slot) {
        if (slot > 8 || slot < 0) {
            return;
        }
        InventoryUtils.mc.player.inventory.currentItem = slot;
    }
    
    public static int getPlaceableItem() {
        final ArrayList<ItemStack> item = new ArrayList<ItemStack>();
        for (int i1 = 0; i1 < 9; ++i1) {
            if (((ItemStack)InventoryUtils.mc.player.inventory.mainInventory.get(i1)).getItem() instanceof ItemBlock) {
                item.add((ItemStack)InventoryUtils.mc.player.inventory.mainInventory.get(i1));
            }
        }
        item.sort((a, b) -> b.getCount() - a.getCount());
        if (item.size() >= 1) {
            return InventoryUtils.mc.player.inventory.mainInventory.indexOf((Object)item.get(0));
        }
        return -1;
    }
    
    public static int pickItem(final int item) {
        final ArrayList<ItemStack> filter = new ArrayList<ItemStack>();
        for (int i1 = 0; i1 < 9; ++i1) {
            if (Item.getIdFromItem(((ItemStack)InventoryUtils.mc.player.inventory.mainInventory.get(i1)).getItem()) == item) {
                filter.add((ItemStack)InventoryUtils.mc.player.inventory.mainInventory.get(i1));
            }
        }
        if (filter.size() >= 1) {
            return InventoryUtils.mc.player.inventory.mainInventory.indexOf((Object)filter.get(0));
        }
        return -1;
    }
    
    public static int pickItem(final int item, final boolean allowInventory) {
        final ArrayList<ItemStack> filter = new ArrayList<ItemStack>();
        for (int i1 = 0; i1 < (allowInventory ? InventoryUtils.mc.player.inventory.mainInventory.size() : 9); ++i1) {
            if (Item.getIdFromItem(((ItemStack)InventoryUtils.mc.player.inventory.mainInventory.get(i1)).getItem()) == item) {
                filter.add((ItemStack)InventoryUtils.mc.player.inventory.mainInventory.get(i1));
            }
        }
        if (filter.size() >= 1) {
            return InventoryUtils.mc.player.inventory.mainInventory.indexOf((Object)filter.get(0));
        }
        return -1;
    }
    
    public static void moveItem(final int before, final int after) {
        InventoryUtils.mc.playerController.windowClick(InventoryUtils.mc.player.inventoryContainer.windowId, before, 0, ClickType.PICKUP, (EntityPlayer)InventoryUtils.mc.player);
        InventoryUtils.mc.playerController.windowClick(InventoryUtils.mc.player.inventoryContainer.windowId, after, 0, ClickType.PICKUP, (EntityPlayer)InventoryUtils.mc.player);
        InventoryUtils.mc.playerController.windowClick(InventoryUtils.mc.player.inventoryContainer.windowId, before, 0, ClickType.PICKUP, (EntityPlayer)InventoryUtils.mc.player);
    }
    
    public static void clickWindow(final int slotIn, final ClickType type) {
    }
    
    public static void push() {
        InventoryUtils.currentItem = InventoryUtils.mc.player.inventory.currentItem;
    }
    
    public static void pop() {
        InventoryUtils.mc.player.inventory.currentItem = InventoryUtils.currentItem;
    }
    
    static {
        InventoryUtils.mc = Minecraft.getMinecraft();
        InventoryUtils.actionNumber = 0;
    }
}
