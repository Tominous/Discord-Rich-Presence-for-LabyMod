package me.tea.labymoddiscordrp.discord;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import me.tea.labymoddiscordrp.LabyModDiscordRP;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Discord {

    private LabyModDiscordRP plugin;

    private DiscordRPC rpc;
    private boolean isShutdown;

    private boolean hasCallbacksStarted = false;
    private boolean hasAutoUpdateStarted = false;

    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public Discord(LabyModDiscordRP plugin){
        this.plugin = plugin;
        isShutdown = false;

        restartRPC();
    }

    public void shutdownRPC(){
        rpc.Discord_Shutdown();
        isShutdown = true;
        hasAutoUpdateStarted = false;
    }

    public void restartRPC(){
        rpc = DiscordRPC.INSTANCE;

        DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.ready = () -> {
            isShutdown = false;
            System.out.println("Minecraft to Discord connection is ready!");
        };

        handlers.errored = (s, x) -> System.out.println("Error: " + x);
        handlers.disconnected = (s, x) -> System.out.println("Disconnected: " + x);

        rpc.Discord_Initialize("414070670423490561", handlers, true, "");

        runAutoUpdate();
        runCallbacks();
    }

    private void runAutoUpdate(){
        if(hasAutoUpdateStarted)
            return;

        hasAutoUpdateStarted = true;

        Runnable callbackRunnable = () -> {
            if(!(hasAutoUpdateStarted))
                return;

            try{
                if(!(isShutdown)){
                    if(plugin.isOnServer())
                        updatePresence(plugin.getSettings().getServerDetailText(), plugin.getSettings().getServerStateText());
                    else
                        updatePresence(plugin.getSettings().getMenuDetailText(), plugin.getSettings().getMenuStateText());
                }

                Thread.sleep(3);
            }catch(InterruptedException ignored){}
        };

        executor.scheduleAtFixedRate(callbackRunnable, 0, 1, TimeUnit.SECONDS);
    }


    private void runCallbacks(){
        if(hasCallbacksStarted)
            return;

        hasCallbacksStarted = true;

        Runnable callbackRunnable = () -> {
            if(!(hasCallbacksStarted))
                return;

            rpc.Discord_RunCallbacks();
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                rpc.Discord_Shutdown();
                isShutdown = true;
                hasCallbacksStarted = false;
            }
        };

        executor.scheduleAtFixedRate(callbackRunnable, 0, 500, TimeUnit.MILLISECONDS);
    }

    private void updatePresence(String details, String state){
        if(isShutdown)
            return;

        DiscordRichPresence presence = new DiscordRichPresence();

        presence.smallImageText = "Minecraft";
        presence.largeImageKey = "minecraftlarge";
        state = state.replace("%server%", plugin.getServer()).replace("%players%", plugin.getPlayers());

        if(!details.isEmpty())
            presence.details = details;

        if(!state.isEmpty())
            presence.state = state;

        if(plugin.getSettings().isTimeElapsedShowing())
            presence.startTimestamp = plugin.getTimeElapsed();

        rpc.Discord_UpdatePresence(presence);
    }
}
