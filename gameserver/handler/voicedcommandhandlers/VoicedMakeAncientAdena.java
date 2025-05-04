/*
 * Copyright (C) 2004-2016 L2J DataPack
 * 
 * This file is part of L2J DataPack.
 * 
 * L2J DataPack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J DataPack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jrevival.gameserver.handler.voicedcommandhandlers;

import com.l2jrevival.gameserver.handler.IVoicedCommandHandler;
import com.l2jrevival.gameserver.model.actor.instance.Player;
import com.l2jrevival.gameserver.model.item.instance.ItemInstance;

/**
 * Voiced command to exchange all Seal Stones to Ancient Adena.<br>
 * Created for Share Competition 2018.
 * @author Benman from MaxCheaters.com
 */
public class VoicedMakeAncientAdena implements IVoicedCommandHandler
{
	
	private static final String[] _voicedCommands =
	{
		"aa",
		"makeaa"
	};
	private static final int ANCIENT_ADENA = 5575;
	private static final int BLUE_SEAL_STONE = 6360;
	private static final int GREEN_SEAL_STONE = 6361;
	private static final int RED_SEAL_STONE = 6362;
	
	@Override
	public boolean useVoicedCommand(String command, Player activeChar, String params)
	{
		if (command.equalsIgnoreCase("aa") || command.equalsIgnoreCase("makeaa"))
		{
			final ItemInstance redStones = activeChar.getInventory().getItemByItemId(RED_SEAL_STONE);
			final ItemInstance greenStones = activeChar.getInventory().getItemByItemId(GREEN_SEAL_STONE);
			final ItemInstance blueStones = activeChar.getInventory().getItemByItemId(BLUE_SEAL_STONE);
			
			int redStonesCount = 0;
			int blueStonesCount = 0;
			int greenStonesCount = 0;
			
			int count = 0;
			int aa = 0;
			
			if (redStones != null)
			{
				redStonesCount += redStones.getCount();
				if (activeChar.destroyItem("MakeAA", redStones, null, true))
				{
					count += redStonesCount;
					aa += redStonesCount * 10;
				}
			}
			if (greenStones != null)
			{
				greenStonesCount += greenStones.getCount();
				if (activeChar.destroyItem("MakeAA", greenStones, null, true))
				{
					count += greenStonesCount;
					aa += greenStonesCount * 5;
				}
			}
			if (blueStones != null)
			{
				blueStonesCount += blueStones.getCount();
				if (activeChar.destroyItem("MakeAA", blueStones, null, true))
				{
					count += blueStonesCount;
					aa += blueStonesCount * 3;
				}
			}
			if (count == 0)
			{
				activeChar.sendMessage("You do not have any seal stones to exchange.");
				return false;
			}
			activeChar.addItem("MakeAA", ANCIENT_ADENA, aa, activeChar, true);
			activeChar.sendMessage("You have successfully exchanged " + count + " seal stones!");
		}
		return true;
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return _voicedCommands;
	}
}