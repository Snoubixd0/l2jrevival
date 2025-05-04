package com.l2jrevival.gameserver.network.clientpackets;

import com.l2jrevival.gameserver.model.actor.instance.Player;
import com.l2jrevival.gameserver.model.partymatching.PartyMatchRoom;
import com.l2jrevival.gameserver.model.partymatching.PartyMatchRoomList;

public class RequestDismissPartyRoom extends L2GameClientPacket
{
	private int _roomid;
	@SuppressWarnings("unused")
	private int _data2;
	
	@Override
	protected void readImpl()
	{
		_roomid = readD();
		_data2 = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;
		
		PartyMatchRoom room = PartyMatchRoomList.getInstance().getRoom(_roomid);
		if (room == null)
			return;
		
		PartyMatchRoomList.getInstance().deleteRoom(_roomid);
	}
}