package dev.blackhig.zhebushigudu.lover.util;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import com.mojang.realmsclient.gui.ChatFormatting;
import dev.blackhig.zhebushigudu.lover.lover;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.client.Minecraft;

public class ChatUtil
{
    public static char SECTIONSIGN;
    private static final int ChatLineId = 777;
    
    public static void printChatMessage(final String message) {
        printRawChatMessage(ChatUtil.SECTIONSIGN + "7[" + ChatUtil.SECTIONSIGN + "b" + "Lover" + ChatUtil.SECTIONSIGN + "7] " + ChatUtil.SECTIONSIGN + "r" + message);
    }
    
    private static void ChatMessage(final String message) {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString(message));
    }
    
    public static void printRawChatMessage(final String message) {
        if (Minecraft.getMinecraft().player == null) {
            return;
        }
        ChatMessage(message);
    }
    
    public static void printErrorChatMessage(final String message) {
        printRawChatMessage(ChatUtil.SECTIONSIGN + "7[" + ChatUtil.SECTIONSIGN + "4" + ChatUtil.SECTIONSIGN + "lERROR" + ChatUtil.SECTIONSIGN + "7] " + ChatUtil.SECTIONSIGN + "r" + message);
    }
    
    static {
        ChatUtil.SECTIONSIGN = '\u00a7';
    }
    
    public static class PrivateMessageSender
    {
        public static void sendPrivateMessage(final String message, final boolean delete) {
            if (Module.fullNullCheck()) {
                return;
            }
            if (delete) {
                Util.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion((ITextComponent)new TextComponentString(message), 777);
            }
            else {
                Util.mc.ingameGUI.getChatGUI().printChatMessage((ITextComponent)new TextComponentString(message));
            }
        }
        
        public static void sendModuleNotifyMessageNoPersist(final String moduleName, final String message) {
            sendPrivateMessage(lover.commandManager.getClientMessage() + ChatFormatting.WHITE + " [" + ChatFormatting.BLUE + moduleName + ChatFormatting.WHITE + "] " + ChatFormatting.GRAY + message, true);
        }
        
        public static void sendModuleNotifyMessagePersist(final String moduleName, final String message) {
            sendPrivateMessage(lover.commandManager.getClientMessage() + ChatFormatting.WHITE + " [" + ChatFormatting.BLUE + moduleName + ChatFormatting.WHITE + "] " + ChatFormatting.GRAY + message, false);
        }
    }
    
    public static class PublicMessageSender
    {
        public static void sendMessageToSever(final String message) {
            if (Module.fullNullCheck()) {
                return;
            }
            Util.mc.player.connection.sendPacket(new CPacketChatMessage(message));
        }
    }
}
