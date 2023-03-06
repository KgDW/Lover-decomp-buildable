package dev.blackhig.zhebushigudu.lover.features.command.commands;

import dev.blackhig.zhebushigudu.lover.lover;
import com.mojang.realmsclient.gui.ChatFormatting;
import dev.blackhig.zhebushigudu.lover.features.command.Command;

public class PrefixCommand extends Command
{
    public PrefixCommand() {
        super("prefix", new String[] { "<char>" });
    }
    
    @Override
    public void execute(final String[] commands) {
        if (commands.length == 1) {
            Command.sendMessage(ChatFormatting.GREEN + "Current prefix is " + lover.commandManager.getPrefix());
            return;
        }
        lover.commandManager.setPrefix(commands[0]);
        Command.sendMessage("Prefix changed to " + ChatFormatting.GRAY + commands[0]);
    }
}
