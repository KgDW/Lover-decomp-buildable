package dev.blackhig.zhebushigudu.lover.features.modules.misc;

import dev.blackhig.zhebushigudu.lover.features.modules.Module;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.lover;
import dev.blackhig.zhebushigudu.lover.util.Timer;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import net.minecraft.network.play.client.CPacketChatMessage;

public class AutoPen
        extends Module {
    public static ArrayList dictionary = new ArrayList();
    Random random = new Random();
    Timer coolDown = new Timer();
    Setting<Integer> delay = this.register(new Setting<>("Delay", 100, 0, 5000));
    Setting<Boolean> rndChar = this.register(new Setting<>("AntiSpam", true));
    Setting<Boolean> whisper = this.register(new Setting<>("Whisper", false));
    Setting<String> name = this.register(new Setting<>("Name", "sb", v -> this.whisper.getValue()));

    public AutoPen() {
        super("AutoFuck", "nmsl", Module.Category.PLAYER, true, false, false);
    }

    @Override
    public void onLogout() {
        this.disable();
    }

    @Override
    public void onUpdate() {
        if (!this.coolDown.passedMs(this.delay.getValue())) {
            return;
        }
        this.coolDown.reset();
        int index = this.random.nextInt(dictionary.size());
        StringBuilder sb = new StringBuilder();
        if (this.whisper.getValue()) {
            sb.append("/tell ");
            sb.append(this.name.getValue());
            sb.append(" ");
        }
        sb.append(dictionary.get(index));
        if (this.rndChar.getValue()) {
            sb.append("[").append((char)(this.random.nextInt(24) + 65)).append((char)(this.random.nextInt(24) + 97)).append("]");
        }
        AutoPen.mc.player.connection.sendPacket(new CPacketChatMessage(sb.toString()));
    }

    static {
        try {
            BufferedReader buff = new BufferedReader(new InputStreamReader(Objects.requireNonNull(lover.class.getClassLoader().getResourceAsStream("dictionary.txt")), "GBK"));
            dictionary = (ArrayList)buff.lines().collect(Collectors.toList());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
 