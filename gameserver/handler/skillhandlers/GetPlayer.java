package com.l2jrevival.gameserver.handler.skillhandlers;

import com.l2jrevival.gameserver.handler.ISkillHandler;
import com.l2jrevival.gameserver.model.L2Skill;
import com.l2jrevival.gameserver.model.WorldObject;
import com.l2jrevival.gameserver.model.actor.Creature;
import com.l2jrevival.gameserver.model.actor.instance.Player;
import com.l2jrevival.gameserver.templates.skills.L2SkillType;

/**
 * Mobs can teleport players to them.
 */
public class GetPlayer implements ISkillHandler
{
	private static final L2SkillType[] SKILL_IDS =
	{
		L2SkillType.GET_PLAYER
	};
	
	@Override
	public void useSkill(Creature activeChar, L2Skill skill, WorldObject[] targets)
	{
		if (activeChar.isAlikeDead())
			return;
		
		for (WorldObject target : targets)
		{
			final Player victim = target.getActingPlayer();
			if (victim == null || victim.isAlikeDead())
				continue;
			
			victim.teleToLocation(activeChar.getX(), activeChar.getY(), activeChar.getZ(), 0);
		}
	}
	
	@Override
	public L2SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}