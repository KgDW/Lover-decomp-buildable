package dev.blackhig.zhebushigudu.lover.features.command.commands;

import java.util.Iterator;
import com.mojang.realmsclient.gui.ChatFormatting;
import dev.blackhig.zhebushigudu.lover.lover;
import dev.blackhig.zhebushigudu.lover.features.command.Command;

public class HelpCommand extends Command
{
    public HelpCommand() {
        super("help");
    }
    
    @Override
    public void execute(final String[] commands) {
        Command.sendMessage("Commands: ");
        for (final Command command : lover.commandManager.getCommands()) {
            Command.sendMessage(ChatFormatting.GRAY + lover.commandManager.getPrefix() + command.getName());
        }
    }
}
