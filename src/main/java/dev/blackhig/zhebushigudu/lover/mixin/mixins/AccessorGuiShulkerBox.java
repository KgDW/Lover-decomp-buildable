package dev.blackhig.zhebushigudu.lover.mixin.mixins;

import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.inventory.IInventory;
import net.minecraft.client.gui.inventory.GuiShulkerBox;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ GuiShulkerBox.class })
public interface AccessorGuiShulkerBox
{
    @Accessor("inventory")
    IInventory getInventory();
}
