package mindcreation.bansystem.commands;

import lombok.SneakyThrows;
import mindcreation.bansystem.utils.UUIDUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import mindcreation.bansystem.Main;

public class UnbanCommand extends Command {

    public UnbanCommand() {
        super("unban", "bansystem.unban");
    }

    @SneakyThrows
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new TextComponent("You need to be a player!"));
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;

        if (args.length == 1) {
            if (UUIDUtils.getUUID(args[0]) != null) {
                if (Main.getMySQLManager().isBanned(UUIDUtils.getUUID(args[0]).toString())) {
                    Main.getMySQLManager().removeBan(UUIDUtils.getUUID(args[0]).toString());
                } else {
                    player.sendMessage(new TextComponent(ChatColor.RED + "This player is not banned: " + args[0]));
                }
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
