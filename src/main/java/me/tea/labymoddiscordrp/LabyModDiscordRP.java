package me.tea.labymoddiscordrp;

import me.tea.labymoddiscordrp.discord.Discord;
import me.tea.labymoddiscordrp.settings.UserSettings;
import net.labymod.api.LabyModAddon;
import net.labymod.core.LabyModCore;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.ControlElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.settings.elements.StringElement;
import net.labymod.utils.Material;
import net.minecraft.client.Minecraft;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class LabyModDiscordRP extends LabyModAddon {

    private UserSettings settings;
    public Discord discord;

    private boolean isOnServer = false;
    private String server;

    private int players;

    private long timeElapsed;

    public UserSettings getSettings() {
        return settings;
    }

    public boolean isOnServer(){
        return isOnServer;
    }

    public String getPlayers(){
        if(!isOnServer || LabyModCore.getMinecraft().getConnection() == null)
            return "0";

        if(!(getSettings().isShowingPlayers()))
            return "[Hidden]";

        players = (LabyModCore.getMinecraft().getConnection() == null ? 0 : LabyModCore.getMinecraft().getConnection().getPlayerInfoMap().size()-1);

        return String.valueOf(players);
    }

    public String getServer(){
        if(!isOnServer)
            return "[N/A]";

        if(!(getSettings().isShowingServer()))
            return "[Hidden]";

        return server;
    }

    public long getTimeElapsed(){
        return timeElapsed;
    }

    @Override
    public void onEnable() {
        settings = new UserSettings(this);
        discord = new Discord(this);
        timeElapsed = System.currentTimeMillis() / 1000;
        players = 0;

        this.getApi().getEventManager().registerOnJoin(serverData -> {
            isOnServer = true;

            if(!getSettings().isShowNumberedIPs() && serverData.getIp().replace(".", "").matches("\\d+")){
                server = "[Hidden]";
            }else
                server = serverData.getIp().toLowerCase();
        });

        this.getApi().getEventManager().registerOnQuit(serverData -> isOnServer = false);
    }

    @Override
    public void onDisable() {
        discord.shutdownRPC();
    }

    @Override
    public void loadConfig() {
        if(getConfig().has("rpcEnabled"))
            settings.setRpcEnabled(getConfig().get("rpcEnabled").getAsBoolean());
        else {
            getConfig().addProperty("rpcEnabled", true);
            discord.restartRPC();
        }

        if(getConfig().has("showServer"))
            settings.setShowServer(getConfig().get("showServer").getAsBoolean());
        else
            getConfig().addProperty("showServer", true);

        if(getConfig().has("showPlayers"))
            settings.setShowPlayers(getConfig().get("showPlayers").getAsBoolean());
        else
            getConfig().addProperty("showPlayers", true);

        if(getConfig().has("showNumericalIP"))
            settings.setShowNumberedIPs(getConfig().get("showNumericalIP").getAsBoolean());
        else
            getConfig().addProperty("showNumericalIP", false);

        if(getConfig().has("showTimeElapsed"))
            settings.setShowTimeElapsed(getConfig().get("showTimeElapsed").getAsBoolean());
        else
            getConfig().addProperty("showTimeElapsed", true);

        if(getConfig().has("rpcMenuDetail"))
            settings.setMenuDetailText(getConfig().get("rpcMenuDetail").getAsString());
        else
            getConfig().addProperty("rpcMenuDetail", settings.getMenuDetailText());

        if(getConfig().has("rpcMenuState"))
            settings.setMenuStateText(getConfig().get("rpcMenuState").getAsString());
        else
            getConfig().addProperty("rpcMenuState", settings.getMenuStateText());

        if(getConfig().has("rpcServerDetail"))
            settings.setServerDetailText(getConfig().get("rpcServerDetail").getAsString());
        else
            getConfig().addProperty("rpcServerDetail", settings.getServerDetailText());

        if(getConfig().has("rpcServerState"))
            settings.setServerStateText(getConfig().get("rpcServerState").getAsString());
        else
            getConfig().addProperty("rpcServerState", settings.getServerStateText());

        saveConfig();
    }

    @Override
    protected void fillSettings( List<SettingsElement> subSettings) {
        // Enable Addon.
        subSettings.add(new BooleanElement("Enable DiscordRPC", new ControlElement.IconData(Material.LEVER), state -> {
            getConfig().addProperty("rpcEnabled", state);
            saveConfig();

            settings.setRpcEnabled(state);
        }, settings.isRpcEnabled()));

        // Show Server.
        subSettings.add(new BooleanElement("Show Server", new ControlElement.IconData(Material.LEVER), state -> {
            getConfig().addProperty("showServer", state);
            saveConfig();

            settings.setShowServer(state);
        }, settings.isShowingServer()));

        // Show Player Count.
        subSettings.add(new BooleanElement("Show Player Count", new ControlElement.IconData(Material.LEVER), state -> {
            getConfig().addProperty("showPlayers", state);
            saveConfig();

            settings.setShowPlayers(state);
        }, settings.isShowingPlayers()));

        // Show Numerical IPs.
        subSettings.add(new BooleanElement("Show Numerical IP", new ControlElement.IconData(Material.LEVER), state -> {
            getConfig().addProperty("showNumericalIP", state);
            saveConfig();

            settings.setShowNumberedIPs(state);
        }, settings.isShowNumberedIPs()));

        // Time Elapsed.
        subSettings.add(new BooleanElement("Show Time Elapsed", new ControlElement.IconData(Material.LEVER), state -> {
            getConfig().addProperty("showTimeElapsed", state);
            saveConfig();

            settings.setShowTimeElapsed(state);
        }, settings.isTimeElapsedShowing()));

        // Menu detail.
        StringElement menuDetail = new StringElement("Menu Detail Message:", new ControlElement.IconData(Material.PAPER), settings.getMenuDetailText(), message -> {
            settings.setMenuDetailText(message);
            getConfig().addProperty("rpcMenuDetail", message);

            saveConfig();
        });

        // Menu state.
        StringElement menuState = new StringElement("Menu State Message:", new ControlElement.IconData(Material.PAPER), settings.getMenuStateText(), message -> {
            settings.setMenuStateText(message);
            getConfig().addProperty("rpcMenuState", message);

            saveConfig();
        });

        // Server detail.
        StringElement ServerDetail = new StringElement("Server Detail Message:", new ControlElement.IconData(Material.PAPER), settings.getServerDetailText(), message -> {
            settings.setServerDetailText(message);
            getConfig().addProperty("rpcServerDetail", message);

            saveConfig();
        });

        // Server state.
        StringElement ServerState = new StringElement("Server State Message:", new ControlElement.IconData(Material.PAPER), settings.getServerStateText(), message -> {
            settings.setServerStateText(message);
            getConfig().addProperty("rpcServerState", message);

            saveConfig();
        });

        subSettings.add(menuDetail);
        subSettings.add(menuState);
        subSettings.add(ServerDetail);
        subSettings.add(ServerState);
    }
}
