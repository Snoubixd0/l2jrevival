package com.l2jrevival.gameserver.handler.chathandlers;

import com.l2jrevival.gameserver.handler.IChatHandler;
import com.l2jrevival.gameserver.model.actor.instance.Player;
import com.l2jrevival.gameserver.network.SystemMessageId;
import com.l2jrevival.gameserver.network.clientpackets.Say2;
import com.l2jrevival.gameserver.network.serverpackets.CreatureSay;

public class ChatClan implements IChatHandler
{
	private static final int[] COMMAND_IDS =
	{
		4
	};
	
	@Override
	public void handleChat(int type, Player activeChar, String target, String text)
	{
		if (activeChar.getClan() == null)
			return;
		
		if (activeChar.ChatProtection(activeChar.getHWID()) && activeChar.isChatBlocked()&& ((activeChar.getChatBanTimer()-1500) > System.currentTimeMillis()))
		{
			if (((activeChar.getChatBanTimer() - System.currentTimeMillis()) / 1000) >= 60)
				activeChar.sendChatMessage(0, Say2.TELL, "SYS", "Your chat was suspended for " + (activeChar.getChatBanTimer() - System.currentTimeMillis()) / (1000*60) + " minute(s).");
			else
				activeChar.sendChatMessage(0, Say2.TELL, "SYS", "Your chat was suspended for " + (activeChar.getChatBanTimer() - System.currentTimeMillis()) / 1000 + " second(s).");

			activeChar.sendPacket(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
			return;
		}
		
		activeChar.getClan().broadcastToOnlineMembers(new CreatureSay(activeChar.getObjectId(), type, activeChar.getName(), text));
	}
	
	@Override
	public int[] getChatTypeList()
	{
		return COMMAND_IDS;
	}
}