package deterno.fight.tgm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import deterno.fight.fightapi.client.TeamClient;
import deterno.fight.fightapi.client.http.HttpClient;
import deterno.fight.fighteapi.client.http.HttpClientConfig;
import deterno.fight.fightapi.client.offline.OfflineClient;
import deterno.fight.tgm.api.ApiManager;
import deterno.fight.tgm.command.CycleCommands;
import deterno.fight.tgm.damage.grave.GravePlugin;
import deterno.fight.tgm.damage.tracker.plugin.TrackerPlugin;
import deterno.fight.tgm.join.JoinManager;
import deterno.fight.tgm.map.MapInfo;
import deterno.fight.tgm.map.MapInfoDeserializer;
import deterno.fight.tgm.match.MatchManager;
import deterno.fight.tgm.match.MatchModule;
import deterno.fight.tgm.modules.GameRuleModule;
import deterno.fight.tgm.player.PlayerManager;
import com.sk89q.bukkit.util.CommandsManagerRegistration;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissionsException;
import com.sk89q.minecraft.util.commands.CommandsManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.List;

public class TGM extends JavaPlugin {
    public static TGM tgm;
    @Getter private Gson gson;
    @Getter private TeamClient teamClient;

    private CommandsManager<CommandSender> commands;
    private CommandsManagerRegistration commandManager;

    @Getter private MatchManager matchManager;
    @Getter private PlayerManager playerManager;
    @Getter private JoinManager joinManager;
    @Getter private TrackerPlugin tracker;
    @Getter private GravePlugin grave;
    @Getter private ApiManager apiManager;

    public static TGM get() {
        return tgm;
    }

    @Override
    public void onEnable() {
        tgm = this;
        FileConfiguration fileConfiguration = getConfig();
        saveDefaultConfig();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(MapInfo.class, new MapInfoDeserializer());
        this.gson = gsonBuilder.create();

        ConfigurationSection apiConfig = fileConfiguration.getConfigurationSection("api");
        if (apiConfig.getBoolean("enabled")) {
            teamClient = new HttpClient(new HttpClientConfig() {
                @Override
                public String getBaseUrl() {
                    return apiConfig.getString("url");
                }

                @Override
                public String getAuthToken() {
                    return apiConfig.getString("auth");
                }
            });
        } else {
            teamClient = new OfflineClient();
        }

        this.commands = new CommandsManager<CommandSender>() {
            @Override
            public boolean hasPermission(CommandSender sender, String perm) {
                return sender instanceof ConsoleCommandSender || sender.hasPermission(perm);
            }
        };

        matchManager = new MatchManager(fileConfiguration);
        playerManager = new PlayerManager();
        joinManager = new JoinManager();
//        playerListManager = new PlayerListManager();
        tracker = new TrackerPlugin(this);
        grave = new GravePlugin(this);
        apiManager = new ApiManager();

        this.commandManager = new CommandsManagerRegistration(this, this.commands);
        commandManager.register(CycleCommands.class);

        GameRuleModule.setGameRules(Bukkit.getWorlds().get(0)); //Set gamerules in main unused world

        try {
            matchManager.cycleNextMatch();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        try {
            this.commands.execute(commandLabel, args, sender, sender);
        } catch (CommandPermissionsException e) {
            if (sender instanceof Player) {
                sender.sendMessage(ChatColor.RED + "Insufficient permissions.");
            } else {
                sender.sendMessage(ChatColor.RED + "You do not have permission.");
            }
        } catch (com.sk89q.minecraft.util.commands.CommandUsageException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
            sender.sendMessage(ChatColor.RED + e.getUsage());
        } catch (CommandException e) {
            sender.sendMessage(e.getMessage());
        }
        return true;
    }

    public static void registerEvents(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, TGM.get());
    }

    @SuppressWarnings("unchecked")
    public <T extends MatchModule> T getModule(Class<T> clazz) {
        return matchManager.getMatch().getModule(clazz);
    }

    @SuppressWarnings("unchecked")
    public <T extends MatchModule> List<T> getModules(Class<T> clazz) {
        return matchManager.getMatch().getModules(clazz);
    }
}
