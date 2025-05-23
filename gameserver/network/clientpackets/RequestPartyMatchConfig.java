package com.l2jrevival.gameserver.network.clientpackets;

import com.l2jrevival.gameserver.model.actor.instance.Player;
import com.l2jrevival.gameserver.model.partymatching.PartyMatchRoom;
import com.l2jrevival.gameserver.model.partymatching.PartyMatchRoomList;
import com.l2jrevival.gameserver.model.partymatching.PartyMatchWaitingList;
import com.l2jrevival.gameserver.network.SystemMessageId;
import com.l2jrevival.gameserver.network.serverpackets.ActionFailed;
import com.l2jrevival.gameserver.network.serverpackets.ExPartyRoomMember;
import com.l2jrevival.gameserver.network.serverpackets.PartyMatchDetail;
import com.l2jrevival.gameserver.network.serverpackets.PartyMatchList;

public final class RequestPartyMatchConfig extends L2GameClientPacket
{
	private int _auto, _loc, _lvl;
	
	@Override
	protected void readImpl()
	{
		_auto = readD();
		_loc = readD();
		_lvl = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;
		
		if (!activeChar.isInPartyMatchRoom() && activeChar.getParty() != null && activeChar.getParty().getLeader() != activeChar)
		{
			activeChar.sendPacket(SystemMessageId.CANT_VIEW_PARTY_ROOMS);
			activeChar.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		if (activeChar.isInPartyMatchRoom())
		{
			// If Player is in Room show him room, not list
			PartyMatchRoomList list = PartyMatchRoomList.getInstance();
			if (list == null)
				return;
			
			PartyMatchRoom room = list.getPlayerRoom(activeChar);
			if (room == null)
				return;
			
			activeChar.sendPacket(new PartyMatchDetail(room));
			activeChar.sendPacket(new ExPartyRoomMember(room, 2));
			
			activeChar.setPartyRoom(room.getId());
			activeChar.broadcastUserInfo();
		}
		else
		{
			// Add to waiting list
			PartyMatchWaitingList.getInstance().addPlayer(activeChar);
			
			// Send Room list
			activeChar.sendPacket(new PartyMatchList(activeChar, _auto, _loc, _lvl));
		}
	}
}