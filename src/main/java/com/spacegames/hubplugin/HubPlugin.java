package com.spacegames.hubplugin;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.UUID;

@Plugin(
        id = "hubplugin",
        name = "HubPlugin",
        version = "1.0",
        description = "Plugin to do /hub!",
        url = "www.spacegames.space",
        authors = {"SpaceNiklas"}
)
public class HubPlugin {

    HashMap<UUID, RegisteredServer> reconnect = new HashMap<>();

    private final ProxyServer server;
    private final Logger logger;

    @Inject
    public HubPlugin(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;

        logger.info("Plugin is up!");
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        CommandManager cm = server.getCommandManager();
        cm.register(cm.metaBuilder("hub").aliases("lobby").build(), new HubCommand(server));

    }
    @Subscribe
    public void onPlayerLeave(DisconnectEvent e){
        Player p = e.getPlayer();
        reconnect.put(p.getUniqueId(), p.getCurrentServer().get().getServer());
    }
    @Subscribe
    public void onPlayerJoin(PostLoginEvent e){
        Player p = e.getPlayer();
        if(reconnect.get(p.getUniqueId()) == null || reconnect.get(p.getUniqueId()).getServerInfo().getName().equalsIgnoreCase("limbo")){
            p.createConnectionRequest(server.getServer("lobby").get()).connect();
        }else if(reconnect.get(p.getUniqueId()) != null){
            p.createConnectionRequest(reconnect.get(p.getUniqueId())).connect();

        }
    }
}
