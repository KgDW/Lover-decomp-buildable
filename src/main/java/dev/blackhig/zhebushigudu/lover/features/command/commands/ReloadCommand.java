package dev.blackhig.zhebushigudu.lover.features.command.commands;

import dev.blackhig.zhebushigudu.lover.lover;
import dev.blackhig.zhebushigudu.lover.features.command.Command;

public class ReloadCommand extends Command
{
    public ReloadCommand() {
        super("reload", new String[0]);
    }
    
    @Override
    public void execute(final String[] commands) {
        lover.reload();
    }
}
