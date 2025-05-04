package com.l2jrevival.bossInstancedEvent;



import com.l2jrevival.gameserver.handler.IVoicedCommandHandler;
import com.l2jrevival.gameserver.model.actor.instance.Player;

import com.l2jrevival.bossInstancedEvent.BossEvent.EventState;

/**
 * @author Zaun
 */
public class BossEventCMD implements IVoicedCommandHandler
{
	
	@Override
	public boolean useVoicedCommand(String command, Player activeChar, String params)
	{
		if (command.startsWith("bossevent"))
		{
			if (BossEvent.getInstance().getState() != EventState.REGISTRATION)
			{
				activeChar.sendMessage("Boss Event is not running!");
				return false;
			}
			if (!BossEvent.getInstance().isRegistered(activeChar))
			{
				if (BossEvent.getInstance().addPlayer(activeChar))
				{
					activeChar.sendMessage("You have been successfully registered in Boss Event!");
				}
				
			}
			else
			{
				if (BossEvent.getInstance().removePlayer(activeChar))
				{
					activeChar.sendMessage("You have been successfully removed of Boss Event!");
				}
			}
		}
		return false;
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		
		return new String[]
		{
			"bossevent"
		};
	}
	
}