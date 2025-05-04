package com.l2jrevival.gameserver.network.clientpackets;

import com.l2jrevival.gameserver.model.actor.instance.Player;
import com.l2jrevival.gameserver.scripting.Quest;
import com.l2jrevival.gameserver.scripting.QuestState;
import com.l2jrevival.gameserver.scripting.ScriptManager;

public final class RequestQuestAbort extends L2GameClientPacket
{
	private int _questId;
	
	@Override
	protected void readImpl()
	{
		_questId = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;
		
		final Quest qe = ScriptManager.getInstance().getQuest(_questId);
		if (qe == null)
			return;
		
		final QuestState qs = activeChar.getQuestState(qe.getName());
		if (qs != null)
			qs.exitQuest(true);
	}
}