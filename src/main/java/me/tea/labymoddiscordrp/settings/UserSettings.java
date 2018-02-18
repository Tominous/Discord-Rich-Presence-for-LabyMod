package me.tea.labymoddiscordrp.settings;

public class UserSettings {

    private boolean rpcEnabled;
    private boolean showServer;
    private boolean showTimeElapsed;
    private boolean showPlayers;

    private String menuStateText;
    private String menuDetailText;

    private String serverStateText;
    private String serverDetailText;

    public UserSettings(){
        this.rpcEnabled = true;
        this.showServer = true;
        this.showTimeElapsed = true;
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

    public boolean isTimeElapsedShowing(){
        return showTimeElapsed;
    }

    public void setShowTimeElapsed(boolean state){
        this.showTimeElapsed = state;
    }

    public String getMenuStateText() {
        return menuStateText;
    }

    public void setMenuStateText(String menuStateText) {
        this.menuStateText = menuStateText;
    }

    public String getMenuDetailText() {
        return menuDetailText;
    }

    public void setMenuDetailText(String menuDetailText) {
        this.menuDetailText = menuDetailText;
    }

    public String getServerStateText() {
        return serverStateText;
    }

    public void setServerStateText(String serverStateText) {
        this.serverStateText = serverStateText;
    }

    public String getServerDetailText() {
        return serverDetailText;
    }

    public void setServerDetailText(String serverDetailText) {
        this.serverDetailText = serverDetailText;
    }
}
