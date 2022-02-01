package mindcreation.bansystem.listener;

import lombok.SneakyThrows;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import mindcreation.bansystem.Main;
import mindcreation.bansystem.ban.BanReason;

import java.sql.Date;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class LoginListener implements Listener {

    @EventHandler
    public void onLoginEvent(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        if (isBanned(player)) {
            player.disconnect(new TextComponent(getBanMessage(player)));
        }
    }

    @SneakyThrows
    private boolean isBanned(ProxiedPlayer player) {
        String uuid = player.getUniqueId().toString();
        ResultSet res = Main.getMySQLManager().query("SELECT * FROM BanSystem WHERE uuid='" + uuid + "'");
        if (!res.first()) return false;
        switch (BanReason.getBanReason(res.getInt("reason"))) {
            case SMALL:
                if (res.getDate("date").before(addDays(res.getDate("date"), 1))) {
                    return true;
                } else {
                    Main.getMySQLManager().removeBan(uuid);
                    return false;
                }
            case MIDDLE:
                if (res.getDate("date").before(addDays(res.getDate("date"), 7))) {
                    return true;
                } else {
                    Main.getMySQLManager().removeBan(uuid);
                    return false;
                }
            case BIG:
            case PERMANENT:
                return true;
        }
        return false;
    }

    @SneakyThrows
    private String getBanMessage(ProxiedPlayer player) {
        String uuid = player.getUniqueId().toString();
        ResultSet res = Main.getMySQLManager().query("SELECT * FROM BanSystem WHERE uuid='" + uuid + "'");
        int reasonId = res.getInt("reason");
        return ChatColor.RED + "Du bist gebannt!" +
                "\n " +
                "\nGrund: " + BanReason.getBanReason(reasonId).getReasonString();
    }

    private java.util.Date addDays(Date date, int days) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);

        return cal.getTime();
    }

}
