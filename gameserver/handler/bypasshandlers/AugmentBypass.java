package com.l2jrevival.gameserver.handler.bypasshandlers;

import java.util.Arrays;
import java.util.List;

import com.l2jrevival.gameserver.handler.BypassHandler;
import com.l2jrevival.gameserver.model.actor.instance.Player;
import com.l2jrevival.gameserver.network.SystemMessageId;
import com.l2jrevival.gameserver.network.serverpackets.ExShowVariationMakeWindow;

public class AugmentBypass implements BypassHandler
{
	@Override
	public void handleCommand(Player player, String command)
	{
		player.sendPacket(SystemMessageId.SELECT_THE_ITEM_TO_BE_AUGMENTED);
		player.sendPacket(ExShowVariationMakeWindow.STATIC_PACKET);
	}

	@Override
	public List<String> getCommands()
	{
		return Arrays.asList("_daniloAugment");
	}
}