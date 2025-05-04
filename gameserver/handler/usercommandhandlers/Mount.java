package com.l2jrevival.gameserver.handler.usercommandhandlers;

import com.l2jrevival.gameserver.handler.IUserCommandHandler;
import com.l2jrevival.gameserver.model.actor.instance.Player;

/**
 * Support for /mount command.
 * @author Tempy
 */
public class Mount implements IUserCommandHandler
{
	private static final int[] COMMAND_IDS =
	{
		61
	};
	
	@Override
	public boolean useUserCommand(int id, Player activeChar)
	{
		return activeChar.mountPlayer(activeChar.getPet());
	}
	
	@Override
	public int[] getUserCommandList()
	{
		return COMMAND_IDS;
	}
}