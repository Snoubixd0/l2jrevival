package com.l2jrevival.gameserver.scripting.tasks;

import com.l2jrevival.gameserver.model.World;
import com.l2jrevival.gameserver.model.actor.instance.Player;
import com.l2jrevival.gameserver.network.serverpackets.UserInfo;
import com.l2jrevival.gameserver.scripting.ScheduledQuest;

public final class Recommendation extends ScheduledQuest
{
	public Recommendation()
	{
		super(-1, "tasks");
	}
	
	@Override
	public final void onStart()
	{
		for (Player player : World.getInstance().getPlayers())
		{
			player.restartRecom();
			player.sendPacket(new UserInfo(player));
		}
		
		_log.config("Recommendation: Recommendation has been reset.");
	}
	
	@Override
	public final void onEnd()
	{
	}
}