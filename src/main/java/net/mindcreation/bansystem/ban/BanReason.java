package net.mindcreation.bansystem.ban;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public enum BanReason {

    SMALL(1, "bansystem.reason.1", "Leichtes Verhalten"),
    MIDDLE(2, "bansystem.reason.2", "Mittleres Verhalten"),
    BIG(3, "bansystem.reason.3", "Schweres Verhalten"),
    PERMANENT(4, "bansystem.reason.4", "Permanent");

    private int id;
    private String permission;
    private String reasonString;

    BanReason(int id, String permission, String reasonString) {
        this.id = id;
        this.permission = permission;
        this.reasonString = reasonString;
    }

    public int getId() {
        return id;
    }

    public String getPermission() {
        return permission;
    }

    public String getReasonString() {
        return reasonString;
    }

    public boolean hasPermission(ProxiedPlayer player) {
        return player.hasPermission(getPermission());
    }

    public static BanReason getBanReason(int id) {
        for (BanReason reason : values()) {
            if (reason.id == id)
                return reason;
        }
        return null;
    }

}
