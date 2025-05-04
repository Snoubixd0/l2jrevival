package com.l2jrevival.gameserver.network.clientpackets;

import com.l2jrevival.gameserver.model.actor.instance.Player;
import com.l2jrevival.gameserver.scripting.QuestState;

public class RequestTutorialPassCmdToServer extends L2GameClientPacket
{
	String _bypass;
	
	@Override
	protected void readImpl()
	{
		_bypass = readS();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getActiveChar();
		if (player == null)
			return;
		
		QuestState qs = player.getQuestState("Tutorial");
		if (qs != null)
			qs.getQuest().notifyEvent(_bypass, null, player);
	}
}