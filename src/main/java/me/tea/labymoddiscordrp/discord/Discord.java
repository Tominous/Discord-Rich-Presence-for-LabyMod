package me.tea.labymoddiscordrp.discord;

import me.tea.labymoddiscordrp.LabyModDiscordRP;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;

public class Discord {

    private LabyModDiscordRP plugin;

    private boolean isShutdown = false;

    public Discord(LabyModDiscordRP plugin){
        this.plugin = plugin;

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("DiscordRPC shutting down.");
            DiscordRPC.discordShutdown();
        }));

        runAutoUpdate();
        runCallbacks();
    }

    public void shutdownRPC(){
        DiscordRPC.discordShutdown();
        isShutdown = true;
    }

    public void restartRPC(){
        DiscordEventHandlers handlers = new DiscordEventHandlers();

        handlers.ready = () -> {
            isShutdown = false;
            System.out.println("Discord to Minecraft connection is ready!");
        };

        handlers.errored = (s, x) -> {
            isShutdown = true;
            DiscordRPC.discordShutdown();
            System.out.println("Error: " + x);
        };

        handlers.disconnected = (s, x) -> {
            isShutdown = true;
            DiscordRPC.discordShutdown();
            System.out.println("Disconnected: " + x);
        };

        isShutdown = false;
        DiscordRPC.discordInitialize("414070670423490561", handlers, true);
    }

    private void runAutoUpdate(){
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {

                    if (!(isShutdown)) {
                        if (plugin.isOnServer())
                            updatePresence(plugin.getSettings().getServerDetailText(), plugin.getSettings().getServerStateText());
                        else
                            updatePresence(plugin.getSettings().getMenuDetailText(), plugin.getSettings().getMenuStateText());

                        Thread.sleep(1000);
                    }
                } catch(InterruptedException ignored){ }
            }
        }, "RPC-Update-Handler").start();
    }


    private void runCallbacks(){
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                DiscordRPC.discordRunCallbacks();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {}
            }
        }, "RPC-Callback-Handler").start();
    }

    private void updatePresence(String details, String state){
        if(isShutdown)
            return;

        DiscordRichPresence presence = new DiscordRichPresence();

        presence.smallImageText = "Minecraft";
        presence.largeImageKey = "minecraftlarge";

        details = details.replace("%server%", plugin.getServer()).replace("%players%", plugin.getPlayers());
        state = state.replace("%server%", plugin.getServer()).replace("%players%", plugin.getPlayers());

        if(!details.isEmpty())
            presence.details = details;

        if(!state.isEmpty())
            presence.state = state;

        if(plugin.getSettings().isTimeElapsedShowing())
            presence.startTimestamp = plugin.getTimeElapsed();

        DiscordRPC.discordUpdatePresence(presence);
    }
}
