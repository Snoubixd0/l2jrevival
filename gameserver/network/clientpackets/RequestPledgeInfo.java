package com.l2jrevival.gameserver.network.clientpackets;

import com.l2jrevival.gameserver.data.sql.ClanTable;
import com.l2jrevival.gameserver.model.actor.instance.Player;
import com.l2jrevival.gameserver.model.pledge.Clan;
import com.l2jrevival.gameserver.network.serverpackets.PledgeInfo;

public final class RequestPledgeInfo extends L2GameClientPacket
{
	private int _clanId;
	
	@Override
	protected void readImpl()
	{
		_clanId = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;
		
		final Clan clan = ClanTable.getInstance().getClan(_clanId);
		if (clan == null)
			return;
		
		activeChar.sendPacket(new PledgeInfo(clan));
	}
	
	@Override
	protected boolean triggersOnActionRequest()
	{
		return false;
	}
}