package com.l2jrevival.gameserver.handler;

import java.util.List;

import com.l2jrevival.commons.logging.CLogger;
import com.l2jrevival.gameserver.model.actor.instance.Player;

public interface BypassHandler
{
	final CLogger LOGGER = new CLogger(BypassHandler.class.getName());
	
    void handleCommand(Player player, String command);
    
    List<String> getCommands();
}