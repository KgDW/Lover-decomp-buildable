package dev.blackhig.zhebushigudu.lover.features.modules.misc;

import dev.blackhig.zhebushigudu.lover.features.modules.Module;
import dev.blackhig.zhebushigudu.lover.util.ChatUtil;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AutoQueue extends Module
{
    private static AutoQueue m_instance;
    Map<String, String> m_questionAndAnswer;
    private boolean m_firstRun;
    
    public AutoQueue() {
        super("AutoQueue", "Automatically queue in 2b2t.xin", Category.MISC, true, false, false);
        this.m_questionAndAnswer = new HashMap<String, String>() {
            {
                this.put("\u6d7c\u72bb\ufffd\ufffd", "B");
                this.put("\u6fb6\u0445\ue188\u701b\ufffd", "C");
                this.put("\u95c2\ue046\u6578\u9351\u8bb3\u8151", "B");
                this.put("\u7039\u6a3b\u67df\u7487\u621d\u6095", "C");
                this.put("\u93c1\u677f\u74e7ID", "A");
                this.put("\u940f\ue0a4\u5299\u5bee\ufffd", "B");
                this.put("\u9357\u6943\u6450", "A");
                this.put("\u6d60\ufffd\u6d94\u581d\u59e9\u9417\ufffd", "B");
                this.put("\u7f07\u5a47\u2518", "B");
                this.put("\u93b8\u6828\u5e22", "C");
                this.put("\u9351\u5b2e\u6d42", "C");
                this.put("\u9366\u581d\u6e74", "B");
                this.put("\u9351\u72b3\u7278\u7ecc\u6d2a\u68ff", "A");
                this.put("\u93c8\ue0a2\u5956\u6d94\u5b2c\u6e82", "A");
                this.put("\u7efe\u3222\u7176\u940f\ue0a3\u59b8", "B");
                this.put("\u9351\u72bb\u3009", "A");
                this.put("\u95b7\ufffd", "B");
                this.put("\u95c4\u52ef\u74df\u95b2\ufffd", "B");
                this.put("\u5bee\ufffd\u93c8\u5d85\u52fe\u6d60\ufffd", "A");
            }
        };
        this.m_firstRun = true;
        this.setInstance();
    }
    
    public static AutoQueue getINSTANCE() {
        if (AutoQueue.m_instance == null) {
            AutoQueue.m_instance = new AutoQueue();
        }
        return AutoQueue.m_instance;
    }
    
    private void setInstance() {
        AutoQueue.m_instance = this;
    }
    
    @Override
    public void onEnable() {
        if (this.playerNullCheck()) {
            return;
        }
        if (Objects.requireNonNull(AutoQueue.mc.getCurrentServerData()).serverIP == null && !AutoQueue.mc.getCurrentServerData().serverIP.equals("2b2t.xin")) {
            ChatUtil.PrivateMessageSender.sendModuleNotifyMessagePersist(this.getName(), "Only support 2b2t.xin!");
            this.disable();
            return;
        }
        if (this.m_firstRun) {
            if (SystemTray.isSupported()) {
                ChatUtil.PrivateMessageSender.sendModuleNotifyMessagePersist(this.getName(), "Start Queueing!");
            }
            else {
                System.err.println("System tray not supported!");
            }
            this.m_firstRun = false;
        }
    }
    
    private boolean playerNullCheck() {
        return false;
    }
    
    @SubscribeEvent
    public void onGuiUpdate(final GuiScreenEvent event) {
        if (fullNullCheck()) {
            return;
        }
        if (event.getGui() instanceof GuiDownloadTerrain) {
            System.err.println("System tray not supported!");
            this.disable();
        }
    }
    
    @SubscribeEvent
    public void onClientChatReceived(final ClientChatReceivedEvent event) {
        if (!Objects.requireNonNull(AutoQueue.mc.getCurrentServerData()).serverIP.equals("2b2t.xin")) {
            return;
        }
        if (event.getMessage().getUnformattedText().contains("\u6402")) {
            final String s = event.getMessage().getUnformattedText().substring(15, 17);
            int sec;
            if (s.contains(" ")) {
                sec = Integer.parseInt(s.substring(0, 1));
            }
            else {
                sec = Integer.parseInt(s);
            }
            if (SystemTray.isSupported() && sec <= 2) {
                return;
            }
        }
        final String msg = event.getMessage().getUnformattedText();
        this.m_questionAndAnswer.entrySet().stream().filter(p -> msg.contains(p.getKey())).findFirst().ifPresent(Answer -> AutoQueue.mc.player.connection.sendPacket(new CPacketChatMessage((String)Answer.getValue())));
    }
    
    static {
        AutoQueue.m_instance = new AutoQueue();
    }
}
