package net.mindcreation.bansystem.commands;

import lombok.SneakyThrows;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.mindcreation.bansystem.utils.UUIDUtils;
import net.mindcreation.bansystem.Main;
import net.mindcreation.bansystem.ban.BanReason;

public class BanCommand extends Command {

    public BanCommand() {
        super("ban", "bansystem.ban");
    }

    @SneakyThrows
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new TextComponent("You need to be a player!"));
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;

        if (args.length == 0) {
            sendReasons(player);
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reasons")) {
                sendReasons(player);
            } else {
                sendInvalidSyntax(player);
            }
        } else if (args.length == 2) {
            if (UUIDUtils.getUUID(args[0]) != null) {
                if (BanReason.getBanReason(getInteger(args[1])) != null) {
                    if (BanReason.getBanReason(getInteger(args[1])).hasPermission(player) || player.hasPermission("bansystem.*") || player.hasPermission("bansystem.reasons.*")) {
                        Main.getMySQLManager().insertBan(UUIDUtils.getUUID(args[0]).toString(), BanReason.getBanReason(getInteger(args[1])));
                        if (Main.getInstance().getProxy().getPlayer(UUIDUtils.getUUID(args[0])) != null) {
                            Main.getInstance().getProxy().getPlayer(UUIDUtils.getUUID(args[0])).disconnect(new TextComponent(getBanMessage(BanReason.getBanReason(getInteger(args[1])))));
                        }
                    } else {
                        sendNoPermission(player);
                    }
                } else {
                    player.sendMessage(new TextComponent(ChatColor.RED + "Invalid BanReason-ID: " + args[1]));
                    sendReasons(player);
                }
            } else {
                player.sendMessage(new TextComponent(ChatColor.RED + "Player not found: " + args[0]));
            }
        } else {
            sendInvalidSyntax(player);
        }
    }

    private void sendReasons(ProxiedPlayer player) {
        player.sendMessage(new TextComponent(ChatColor.YELLOW + "" + ChatColor.BOLD + "Reasons:\n" +
                ChatColor.YELLOW + "1 " + ChatColor.DARK_GRAY + "- " + ChatColor.YELLOW + "Leichtes Vergehen (1 Tag)\n" +
                ChatColor.YELLOW + "2 " + ChatColor.DARK_GRAY + "- " + ChatColor.YELLOW + "Mittleres Vergehen (1 Woche)\n" +
                ChatColor.YELLOW + "3 " + ChatColor.DARK_GRAY + "- " + ChatColor.YELLOW + "Schweres Vergehen (Permanent)\n" +
                ChatColor.YELLOW + "4 " + ChatColor.DARK_GRAY + "- " + ChatColor.YELLOW + "Permanent (Permanent)"));
    }

    private void sendInvalidSyntax(ProxiedPlayer player) {
        player.sendMessage(new TextComponent(ChatColor.RED + "Invalid Syntax!"));
    }

    private void sendNoPermission(ProxiedPlayer player) {
        player.sendMessage(new TextComponent(ChatColor.RED + "You don't have the permission to ban with this reason!"));
    }

    private boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    private int getInteger(String string) {
        if (!isInteger(string)) return -1;
        return Integer.parseInt(string);
    }

    @SneakyThrows
    private String getBanMessage(BanReason reason) {
        return ChatColor.RED + "Du bist gebannt!" +
                "\n " +
                "\nGrund: " + reason.getReasonString();
    }

}
