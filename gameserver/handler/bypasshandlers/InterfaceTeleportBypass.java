package com.l2jrevival.gameserver.handler.bypasshandlers;

import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import com.l2jrevival.gameserver.data.xml.InterfaceTeleportData;
import com.l2jrevival.gameserver.handler.BypassHandler;
import com.l2jrevival.gameserver.model.actor.instance.Player;
import com.l2jrevival.gameserver.teleport.InterfaceTeleport;

public class InterfaceTeleportBypass implements BypassHandler
{
	@Override
	public void handleCommand(Player player, String command)
	{
		if (!isAllowed(player))
			return;
		
		final StringTokenizer stringTokenizer = new StringTokenizer(command, " ");
		stringTokenizer.nextToken();
		
		final String idString = stringTokenizer.nextToken().replaceAll("_", "");
		final int id = Integer.parseInt(idString);
		final InterfaceTeleport interfaceTeleport = InterfaceTeleportData.getInstance().getInterfaceTeleport(id);
		if (!player.destroyItemByItemId("Consume", interfaceTeleport.getPriceId(), interfaceTeleport.getPriceCount(), player, true))
			return;
		
		player.teleToLocation(interfaceTeleport.getLocation(), 0);
	}
	
	private final boolean isAllowed(Player player) 
	{
		if (!player.isNoble() && !player.isVip())
		{
			player.sendMessage("You must be a noble or a VIP to use this feature.");
			return false;
		}
		
		if (player.isSitting())
		{
			player.sendMessage("You cannot use the teleport while sitting.");
			return false;
		}
		
		if (player.isDead()) 
		{
			player.sendMessage("You cannot use the teleport while dead.");
			return false;
		}
		
		if (player.getPvpFlag() > 0)
		{
			player.sendMessage("You cannot use the teleport while in PvP mode.");
			return false;
		}
		
		if (player.getKarma() > 0)
		{
			player.sendMessage("You cannot use the teleport while in PK mode.");
			return false;
		}
		
		if (player.isInCombat())
		{
			player.sendMessage("You cannot use the teleport while in combat.");
			return false;
		}
		
		if (player.isInDuel())
		{
			player.sendMessage("You cannot use the teleport while in a duel.");
			return false;
		}
		
		if (player.isInOlympiadMode() || player.isInObserverMode()) 
		{
			player.sendMessage("You cannot use the teleport while in Olympiad.");
			return false;
		}
		
		if (player.isInJail())
		{
			player.sendMessage("You cannot use the teleport while in jail.");
			return false;
		}
		
		if (player.isInArenaEvent() || player.isEventObserver())
		{
			player.sendMessage("You cannot use the teleport while in an event.");
			return false;
		}
		
		return true;
	}

	@Override
	public List<String> getCommands()
	{
		return Arrays.asList("GkGo");
	}
}