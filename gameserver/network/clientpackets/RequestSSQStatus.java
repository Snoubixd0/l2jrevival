package com.l2jrevival.gameserver.network.clientpackets;

import com.l2jrevival.gameserver.instancemanager.SevenSigns;
import com.l2jrevival.gameserver.model.actor.instance.Player;
import com.l2jrevival.gameserver.network.serverpackets.SSQStatus;

/**
 * Seven Signs Record Update Request packet type id 0xc7 format: cc
 * @author Tempy
 */
public final class RequestSSQStatus extends L2GameClientPacket
{
	private int _page;
	
	@Override
	protected void readImpl()
	{
		_page = readC();
	}
	
	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;
		
		if ((SevenSigns.getInstance().isSealValidationPeriod() || SevenSigns.getInstance().isCompResultsPeriod()) && _page == 4)
			return;
		
		activeChar.sendPacket(new SSQStatus(activeChar.getObjectId(), _page));
	}
}