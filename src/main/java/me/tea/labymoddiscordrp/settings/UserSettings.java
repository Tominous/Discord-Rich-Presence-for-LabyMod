package me.tea.labymoddiscordrp.settings;

import me.tea.labymoddiscordrp.LabyModDiscordRP;

public class UserSettings {

    private LabyModDiscordRP plugin;

    private boolean rpcEnabled;
    private boolean showServer;
    private boolean showTimeElapsed;
    private boolean showNumberedIPs;
    private boolean showPlayers;

    private String menuDetailText;
    private String menuStateText;

    private String serverDetailText;
    private String serverStateText;

    public UserSettings(LabyModDiscordRP plugin){
        this.plugin = plugin;
        this.rpcEnabled = true;
        this.showServer = true;
        this.showTimeElapsed = true;
        this.showNumberedIPs = false;
        this.showPlayers = true;

        menuDetailText = "Idle on main menu.";
        menuStateText = "";

        serverDetailText = "Playing on a Multiplayer Server.";
        serverStateText = "Server: %server% with %players% other players.";
    }

    public boolean isRpcEnabled() {
        return rpcEnabled;
    }

    public void setRpcEnabled(boolean rpcEnabled) {
        this.rpcEnabled = rpcEnabled;

        if(rpcEnabled){
            plugin.discord.restartRPC();
        }else{
            plugin.discord.shutdownRPC();
        }
    }

    public boolean isShowingServer(){
        return showServer;
    }

    public void setShowServer(boolean state){
        this.showServer = state;
    }

    public boolean isShowingPlayers(){
        return showPlayers;
    }

    public void setShowPlayers(boolean state){
        this.showPlayers = state;
    }

    public boolean isShowNumberedIPs(){
        return showNumberedIPs;
    }

    public void setShowNumberedIPs(boolean state){
        this.showNumberedIPs = state;
    }

    public boolean isTimeElapsedShowing(){
        return showTimeElapsed;
    }

    public void setShowTimeElapsed(boolean state){
        this.showTimeElapsed = state;
    }

    public String getMenuDetailText() {
        return menuDetailText;
    }

    public void setMenuDetailText(String menuDetailText) {
        this.menuDetailText = menuDetailText;
    }

    public String getMenuStateText() {
        return menuStateText;
    }

    public void setMenuStateText(String menuStateText) {
        this.menuStateText = menuStateText;
    }

    public String getServerDetailText() {
        return serverDetailText;
    }

    public void setServerDetailText(String serverDetailText) {
        this.serverDetailText = serverDetailText;
    }

    public String getServerStateText() {
        return serverStateText;
    }

    public void setServerStateText(String serverStateText) {
        this.serverStateText = serverStateText;
    }
}
