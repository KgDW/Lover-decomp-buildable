package dev.blackhig.zhebushigudu.lover.features.modules.player;

import net.minecraft.block.BlockShulkerBox;
import net.minecraft.init.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import dev.blackhig.zhebushigudu.lover.event.events.PacketEvent;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;

public class AntiContainer extends Module
{
    public Setting<Boolean> Chest;
    public Setting<Boolean> EnderChest;
    public Setting<Boolean> Trapped_Chest;
    public Setting<Boolean> Hopper;
    public Setting<Boolean> Dispenser;
    public Setting<Boolean> Furnace;
    public Setting<Boolean> Beacon;
    public Setting<Boolean> Crafting_Table;
    public Setting<Boolean> Anvil;
    public Setting<Boolean> Enchanting_table;
    public Setting<Boolean> Brewing_Stand;
    public Setting<Boolean> ShulkerBox;
    
    public AntiContainer() {
        super("AntiContainer", "Do not display containers", Category.PLAYER, true, false, false);
        this.Chest = this.register(new Setting<>("Chest", true));
        this.EnderChest = this.register(new Setting<>("EnderChest", true));
        this.Trapped_Chest = this.register(new Setting<>("Trapped_Chest", true));
        this.Hopper = this.register(new Setting<>("Hopper", true));
        this.Dispenser = this.register(new Setting<>("Dispenser", true));
        this.Furnace = this.register(new Setting<>("Furnace", true));
        this.Beacon = this.register(new Setting<>("Beacon", true));
        this.Crafting_Table = this.register(new Setting<>("Crafting_Table", true));
        this.Anvil = this.register(new Setting<>("Anvil", true));
        this.Enchanting_table = this.register(new Setting<>("Enchanting_table", true));
        this.Brewing_Stand = this.register(new Setting<>("Brewing_Stand", true));
        this.ShulkerBox = this.register(new Setting<>("ShulkerBox", true));
    }
    
    @SubscribeEvent
    public void onCheck(final PacketEvent.Send packet) {
        if (packet.packet instanceof CPacketPlayerTryUseItemOnBlock) {
            final BlockPos pos = ((CPacketPlayerTryUseItemOnBlock)packet.packet).getPos();
            if (this.check(pos)) {
                packet.setCanceled(true);
            }
        }
    }
    
    public boolean check(final BlockPos pos) {
        return (Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.CHEST && this.Chest.getValue()) || (Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.ENDER_CHEST && this.EnderChest.getValue()) || (Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.TRAPPED_CHEST && this.Trapped_Chest.getValue()) || (Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.HOPPER && this.Hopper.getValue()) || (Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.DISPENSER && this.Dispenser.getValue()) || (Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.FURNACE && this.Furnace.getValue()) || (Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.BEACON && this.Beacon.getValue()) || (Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.CRAFTING_TABLE && this.Crafting_Table.getValue()) || (Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.ANVIL && this.Anvil.getValue()) || (Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.ENCHANTING_TABLE && this.Enchanting_table.getValue()) || (Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.BREWING_STAND && this.Brewing_Stand.getValue()) || (Minecraft.getMinecraft().world.getBlockState(pos).getBlock() instanceof BlockShulkerBox && this.ShulkerBox.getValue());
    }
}
