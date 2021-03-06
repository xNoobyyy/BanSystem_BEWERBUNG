package mindcreation.bansystem;

import lombok.SneakyThrows;
import mindcreation.bansystem.commands.BanCommand;
import mindcreation.bansystem.commands.KickCommand;
import mindcreation.bansystem.commands.UnbanCommand;
import mindcreation.bansystem.listener.LoginListener;
import mindcreation.bansystem.mysql.MySQLManager;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;

public final class Main extends Plugin {

    private static MySQLManager sqlManager;
    private static Main instance;
    private static Configuration configuration;

    @SneakyThrows
    @Override
    public void onEnable() {

        boolean configureDatabase = false;

        instance = this;

        configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));

        if (configuration.getString("database.host") == null) {
            configuration.set("database.host", "host");
            configureDatabase = true;
        }
        if (configuration.getString("database.port") == null) {
            configuration.set("database.port", "3306");
            configureDatabase = true;
        }
        if (configuration.getString("database.database") == null) {
            configuration.set("database.database", "BanSystem");
            configureDatabase = true;
        }
        if (configuration.getString("database.user") == null) {
            configuration.set("database.user", "root");
            configureDatabase = true;
        }
        if (configuration.getString("database.password") == null) {
            configuration.set("database.password", "password");
            configureDatabase = true;
        }

        ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, new File(getDataFolder(), "config.yml"));

        if (configureDatabase) {
            this.getProxy().getConsole().sendMessage(new TextComponent("[BanSystem] Please configure the MySQL Database in the config.yml"));
        }

        sqlManager = new MySQLManager(
                configuration.getString("database.host"),
                configuration.getString("database.port"),
                configuration.getString("database.database"),
                configuration.getString("database.user"),
                configuration.getString("database.password")
        );

        getProxy().getPluginManager().registerListener(this, new LoginListener());
        getProxy().getPluginManager().registerCommand(this, new BanCommand());
        getProxy().getPluginManager().registerCommand(this, new UnbanCommand());
        getProxy().getPluginManager().registerCommand(this, new KickCommand());
    }

    @Override
    public void onDisable() {
        getMySQLManager().disconnect();
    }

    public static Main getInstance() {
        return instance;
    }

    public static MySQLManager getMySQLManager() {
        return sqlManager;
    }

}
