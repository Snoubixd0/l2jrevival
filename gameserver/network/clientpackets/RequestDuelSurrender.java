package com.l2jrevival.gameserver.network.clientpackets;

import com.l2jrevival.gameserver.instancemanager.DuelManager;

/**
 * Format:(ch)
 * @author -Wooden-
 */
public final class RequestDuelSurrender extends L2GameClientPacket
{
	@Override
	protected void readImpl()
	{
	}
	
	@Override
	protected void runImpl()
	{
		DuelManager.getInstance().doSurrender(getClient().getActiveChar());
	}
}