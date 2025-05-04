package com.l2jrevival.gameserver.handler.bypasshandlers;

import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import com.l2jrevival.gameserver.handler.BypassHandler;
import com.l2jrevival.gameserver.model.L2Effect;
import com.l2jrevival.gameserver.model.L2Skill;
import com.l2jrevival.gameserver.model.actor.instance.Player;

public class DispelBuff implements BypassHandler
{
	@Override
	public void handleCommand(Player player, String command)
	{
		final StringTokenizer stringTokenizer = new StringTokenizer(command, " ");
		stringTokenizer.nextToken();
		final int buffId = Integer.parseInt(stringTokenizer.nextToken());
		final L2Effect abstractEffect = player.getFirstEffect(buffId);
		if (abstractEffect == null)
			return;
		
		final L2Skill skill = abstractEffect.getSkill();
		if (skill == null || skill.isOffensive())
			return;
		
		abstractEffect.exit();
		player.updateAndBroadcastStatus(2);
	}

	@Override
	public List<String> getCommands()
	{
		return Arrays.asList("BuffEngine_Dispel");
	}
}