package com.l2jrevival.gameserver.network.clientpackets;

import com.l2jrevival.gameserver.data.xml.AdminData;
import com.l2jrevival.gameserver.model.actor.instance.Player;

public final class RequestGmList extends L2GameClientPacket
{
	@Override
	protected void readImpl()
	{
	}
	
	@Override
	protected void runImpl()
	{
		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;
		
		AdminData.getInstance().sendListToPlayer(activeChar);
	}
}