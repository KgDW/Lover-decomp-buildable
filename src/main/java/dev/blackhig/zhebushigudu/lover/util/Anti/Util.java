package dev.blackhig.zhebushigudu.lover.util.Anti;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.util.text.TextComponentBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import dev.blackhig.zhebushigudu.lover.util.BlockUtil;
import net.minecraft.block.BlockEnderChest;
import dev.blackhig.zhebushigudu.lover.util.InventoryUtil;
import net.minecraft.util.math.Vec3i;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.block.BlockObsidian;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.client.Minecraft;

public class Util
{
    public static Minecraft mc;
    
    public static void doBurrow() {
        Util.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Util.mc.player.posX, Util.mc.player.posY + 0.419999986886978, Util.mc.player.posZ, false));
        Util.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Util.mc.player.posX, Util.mc.player.posY + 0.7531999805212015, Util.mc.player.posZ, false));
        Util.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Util.mc.player.posX, Util.mc.player.posY + 1.001335979112147, Util.mc.player.posZ, false));
        Util.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Util.mc.player.posX, Util.mc.player.posY + 1.166109260938214, Util.mc.player.posZ, false));
        final int a = Util.mc.player.inventory.currentItem;
        Util.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(findHotbarBlock(BlockObsidian.class)));
        Util.mc.player.inventory.currentItem = findHotbarBlock(BlockObsidian.class);
        Util.mc.playerController.updateController();
        burrowPlace(false);
        Util.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(a));
        Util.mc.player.inventory.currentItem = a;
        Util.mc.playerController.updateController();
        Util.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Util.mc.player.posX, Util.mc.player.posY + 2.200000047683719, Util.mc.player.posZ, true));
    }
    
    public static void sendMessage(final String message) {
        if (Util.mc.player != null) {
            Util.mc.player.sendMessage((ITextComponent)new ChatMessage(message));
        }
    }
    
    public static void rightClickBlock(final BlockPos pos, final Vec3d vec, final EnumHand hand, final EnumFacing direction, final boolean packet) {
        if (packet) {
            final float f = (float)(vec.x - pos.getX());
            final float f2 = (float)(vec.y - pos.getY());
            final float f3 = (float)(vec.z - pos.getZ());
            Util.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f2, f3));
        }
        else {
            Util.mc.playerController.processRightClickBlock(Util.mc.player, Util.mc.world, pos, direction, vec, hand);
        }
        Util.mc.player.swingArm(EnumHand.MAIN_HAND);
        Util.mc.rightClickDelayTimer = 4;
    }
    
    public static void burrowPlace(final boolean rotate) {
        final BlockPos neighbour = new BlockPos(Util.mc.player.posX, Util.mc.player.posY, Util.mc.player.posZ).down();
        final Vec3d hitVec = new Vec3d((Vec3i)neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(EnumFacing.UP.getDirectionVec()).scale(0.5));
        rightClickBlock(neighbour, hitVec, EnumHand.MAIN_HAND, EnumFacing.UP, true);
        Util.mc.player.swingArm(EnumHand.MAIN_HAND);
    }
    
    public static void placeBlock(final Vec3d pos, final boolean rotate) {
        final BlockPos blockpos = new BlockPos((int)pos.x, (int)pos.y, (int)pos.z);
        final Vec3d hitVec = new Vec3d((Vec3i)blockpos).add(0.5, 0.5, 0.5).add(new Vec3d(EnumFacing.UP.getDirectionVec()).scale(0.5));
        rightClickBlock(blockpos, hitVec, EnumHand.MAIN_HAND, EnumFacing.UP, true);
    }
    
    public static void placeBlock(final BlockPos pos) {
        final int originalSlot = Util.mc.player.inventory.currentItem;
        final int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        final int eChestSot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
        Util.mc.player.inventory.currentItem = ((obbySlot == -1) ? eChestSot : obbySlot);
        Util.mc.playerController.updateController();
        BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, false, true, Util.mc.player.isSneaking());
        Util.mc.player.inventory.currentItem = originalSlot;
        Util.mc.playerController.updateController();
    }
    
    public static int findHotbarBlock(final Class<?> clazz) {
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = Util.mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY) {
                if (clazz.isInstance(stack.getItem())) {
                    return i;
                }
                if (stack.getItem() instanceof ItemBlock) {
                    if (clazz.isInstance(((ItemBlock)stack.getItem()).getBlock())) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }
    
    static {
        Util.mc = Minecraft.getMinecraft();
    }
    
    public static class ChatMessage extends TextComponentBase
    {
        private final String text;
        
        public ChatMessage(final String text) {
            final Pattern pattern = Pattern.compile("&[0123456789abcdefrlosmk]");
            final Matcher matcher = pattern.matcher(text);
            final StringBuffer stringBuffer = new StringBuffer();
            while (matcher.find()) {
                final String replacement = matcher.group().substring(1);
                matcher.appendReplacement(stringBuffer, replacement);
            }
            matcher.appendTail(stringBuffer);
            this.text = stringBuffer.toString();
        }
        
        public String getUnformattedComponentText() {
            return this.text;
        }
        
        public ITextComponent createCopy() {
            return null;
        }
        
        public ITextComponent shallowCopy() {
            return (ITextComponent)new ChatMessage(this.text);
        }
    }
}
