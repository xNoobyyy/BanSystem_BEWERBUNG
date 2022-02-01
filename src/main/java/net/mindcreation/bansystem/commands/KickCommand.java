package net.mindcreation.bansystem.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.mindcreation.bansystem.Main;

import java.util.Arrays;
import java.util.List;

public class KickCommand extends Command {

    public KickCommand() {
        super("kick", "bansystem.kick");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new TextComponent("You need to be a player!"));
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;

        if (args.length >= 1) {
            if (Main.getInstance().getProxy().getPlayer(args[0]) != null) {
                ProxiedPlayer target = Main.getInstance().getProxy().getPlayer(args[0]);
                String reason = "Kicked";
                if (args.length >= 2) {
                    List<String> reasonArgs = Arrays.asList(args);
                    reasonArgs.remove(0);
                    reason = String.join(" ", reasonArgs);
                }
                target.disconnect(new TextComponent(ChatColor.RED + "You have been kicked!\n" + ChatColor.RED + "Reason:" + reason));
            } else {
                player.sendMessage(new TextComponent(ChatColor.RED + "Player not found: " + args[0]));
            }
        } else {
            sendInvalidSyntax(player);
        }
    }

    private void sendInvalidSyntax(ProxiedPlayer player) {
        player.sendMessage(new TextComponent(ChatColor.RED + "Invalid Syntax!"));
    }

}
