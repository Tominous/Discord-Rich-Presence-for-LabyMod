package net.arikia.dev.drpc;

import com.sun.jna.Structure;
import net.arikia.dev.drpc.callbacks.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author Nicolas "Vatuu" Adamoglou
 * @version 1.5.1
 *
 * Object containing references to all event handlers registered. No callbacks are necessary,
 * every event handler is optional. Non-assigned handlers are being ignored.
 */
public class DiscordEventHandlers extends Structure {

    @Override
    public List<String> getFieldOrder(){
        return Arrays.asList("ready", "disconnected", "errored");
    }

    /**
     * Callback called when Discord-RPC was initialized successfully.
     */
    public ReadyCallback ready;

    /**
     * Callback called when the Discord connection was disconnected.
     */
    public DisconnectedCallback disconnected;

    /**
     * Callback called when a Discord error occurred.
     */
    public ErroredCallback errored;

    public static class Builder{

        DiscordEventHandlers h;

        public Builder(){
            h = new DiscordEventHandlers();
        }

        public Builder setReadyEventHandler(ReadyCallback r){
            h.ready = r;
            return this;
        }

        public Builder setDisconnectedEventHandler(DisconnectedCallback d){
            h.disconnected = d;
            return this;
        }

        public Builder setErroredEventHandler(ErroredCallback e){
            h.errored = e;
            return this;
        }

        public DiscordEventHandlers build() {
            return h;
        }
    }
}
