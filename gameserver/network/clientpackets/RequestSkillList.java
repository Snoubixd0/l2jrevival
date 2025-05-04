package com.l2jrevival.gameserver.network.clientpackets;

import com.l2jrevival.gameserver.model.actor.instance.Player;

public final class RequestSkillList extends L2GameClientPacket
{
	@Override
	protected void readImpl()
	{
	}
	
	@Override
	protected void runImpl()
	{
		final Player cha = getClient().getActiveChar();
		if (cha == null)
			return;
		
		cha.sendSkillList();
	}
}