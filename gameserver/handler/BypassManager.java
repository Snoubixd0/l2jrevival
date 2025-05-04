package com.l2jrevival.gameserver.handler;

import java.util.HashMap;
import java.util.Map;

import com.l2jrevival.gameserver.handler.bypasshandlers.AugmentBypass;
import com.l2jrevival.gameserver.handler.bypasshandlers.AutoSSBypass;
import com.l2jrevival.gameserver.handler.bypasshandlers.DispelBuff;
import com.l2jrevival.gameserver.handler.bypasshandlers.InterfaceTeleportBypass;


public final class BypassManager
{
    private final Map<Integer, BypassHandler> bypassHandlerMap = new HashMap<>();

    private BypassManager()
    {
    	registerHandler(new AugmentBypass());
    	registerHandler(new AutoSSBypass());
    	registerHandler(new DispelBuff());
    	registerHandler(new InterfaceTeleportBypass());
    }
    
    private final void registerHandler(BypassHandler bypassHandler)
    {
       bypassHandler.getCommands().forEach(command -> bypassHandlerMap.put(command.trim().intern().hashCode(), bypassHandler));
    }

    public final BypassHandler getBypassHandler(String command) 
    {
        return bypassHandlerMap.get(command.trim().intern().hashCode());
    }

    public final int size() 
    {
        return bypassHandlerMap.size();
    }

    public static final BypassManager getInstance() 
    {
        return SingletonHolder.INSTANCE;
    }

    private static final class SingletonHolder 
    {
        private static final BypassManager INSTANCE = new BypassManager();
    }
}