package dev.blackhig.zhebushigudu.lover.manager;

import java.util.Iterator;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.LinkedList;
import dev.blackhig.zhebushigudu.lover.features.command.commands.ReloadSoundCommand;
import dev.blackhig.zhebushigudu.lover.features.command.commands.UnloadCommand;
import dev.blackhig.zhebushigudu.lover.features.command.commands.ReloadCommand;
import dev.blackhig.zhebushigudu.lover.features.command.commands.HelpCommand;
import dev.blackhig.zhebushigudu.lover.features.command.commands.FriendCommand;
import dev.blackhig.zhebushigudu.lover.features.command.commands.ConfigCommand;
import dev.blackhig.zhebushigudu.lover.features.command.commands.PrefixCommand;
import dev.blackhig.zhebushigudu.lover.features.command.commands.ModuleCommand;
import dev.blackhig.zhebushigudu.lover.features.command.commands.BindCommand;
import dev.blackhig.zhebushigudu.lover.features.command.Command;
import java.util.ArrayList;
import dev.blackhig.zhebushigudu.lover.features.Feature;

public class CommandManager extends Feature
{
    private final ArrayList<Command> commands;
    private String clientMessage;
    private String prefix;
    
    public CommandManager() {
        super("Command");
        this.commands = new ArrayList<Command>();
        this.clientMessage = "<lover>";
        this.prefix = ".";
        this.commands.add(new BindCommand());
        this.commands.add(new ModuleCommand());
        this.commands.add(new PrefixCommand());
        this.commands.add(new ConfigCommand());
        this.commands.add(new FriendCommand());
        this.commands.add(new HelpCommand());
        this.commands.add(new ReloadCommand());
        this.commands.add(new UnloadCommand());
        this.commands.add(new ReloadSoundCommand());
    }
    
    public static String[] removeElement(final String[] input, final int indexToDelete) {
        final LinkedList<String> result = new LinkedList<String>();
        for (int i = 0; i < input.length; ++i) {
            if (i != indexToDelete) {
                result.add(input[i]);
            }
        }
        return result.toArray(input);
    }
    
    private static String strip(final String str, final String key) {
        if (str.startsWith(key) && str.endsWith(key)) {
            return str.substring(key.length(), str.length() - key.length());
        }
        return str;
    }
    
    public void executeCommand(final String command) {
        final String[] parts = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        final String name = parts[0].substring(1);
        final String[] args = removeElement(parts, 0);
        for (int i = 0; i < args.length; ++i) {
            if (args[i] != null) {
                args[i] = strip(args[i], "\"");
            }
        }
        for (final Command c : this.commands) {
            if (!c.getName().equalsIgnoreCase(name)) {
                continue;
            }
            c.execute(parts);
            return;
        }
        Command.sendMessage(ChatFormatting.GRAY + "Command not found, type 'help' for the commands list.");
    }
    
    public Command getCommandByName(final String name) {
        for (final Command command : this.commands) {
            if (!command.getName().equals(name)) {
                continue;
            }
            return command;
        }
        return null;
    }
    
    public ArrayList<Command> getCommands() {
        return this.commands;
    }
    
    public String getClientMessage() {
        return this.clientMessage;
    }
    
    public void setClientMessage(final String clientMessage) {
        this.clientMessage = clientMessage;
    }
    
    public String getPrefix() {
        return this.prefix;
    }
    
    public void setPrefix(final String prefix) {
        this.prefix = prefix;
    }
}
