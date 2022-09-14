package com.spacegames.hubplugin;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;

public final class HubCommand implements SimpleCommand {

    private final ProxyServer server;

    public HubCommand(ProxyServer server) {
        this.server = server;
    }

    @Override
    public void execute(Invocation invocation) {
        if(invocation.source() instanceof Player){
            Player player = (Player) invocation.source();
            player.createConnectionRequest(server.getServer("lobby").get()).connect();
        }
    }
}