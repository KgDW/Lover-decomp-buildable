package dev.blackhig.zhebushigudu.lover.features.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.blackhig.zhebushigudu.lover.lover;
import net.minecraft.scoreboard.Team;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.client.network.NetworkPlayerInfo;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;

public class ExtraTab extends Module
{
    private static ExtraTab INSTANCE;
    public Setting<Integer> size;
    
    public ExtraTab() {
        super("ExtraTab", "Extends Tab.", Category.MISC, false, false, false);
        this.size = this.register(new Setting("Size", 250, 1, 1000));
        this.setInstance();
    }
    
    public static String getPlayerName(final NetworkPlayerInfo networkPlayerInfoIn) {
        final String name = (networkPlayerInfoIn.getDisplayName() != null) ? networkPlayerInfoIn.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName((Team)networkPlayerInfoIn.getPlayerTeam(), networkPlayerInfoIn.getGameProfile().getName());
        if (lover.friendManager.isFriend(name)) {
            return ChatFormatting.AQUA + name;
        }
        return name;
    }
    
    public static ExtraTab getINSTANCE() {
        if (ExtraTab.INSTANCE == null) {
            ExtraTab.INSTANCE = new ExtraTab();
        }
        return ExtraTab.INSTANCE;
    }
    
    private void setInstance() {
        ExtraTab.INSTANCE = this;
    }
    
    static {
        ExtraTab.INSTANCE = new ExtraTab();
    }
}
