package com.l2jrevival.gameserver.skills.l2skills;

import com.l2jrevival.gameserver.model.L2Skill;
import com.l2jrevival.gameserver.model.WorldObject;
import com.l2jrevival.gameserver.model.actor.Creature;
import com.l2jrevival.gameserver.network.serverpackets.ActionFailed;
import com.l2jrevival.gameserver.templates.StatsSet;

public class L2SkillDefault extends L2Skill
{
	public L2SkillDefault(StatsSet set)
	{
		super(set);
	}
	
	@Override
	public void useSkill(Creature caster, WorldObject[] targets)
	{
		caster.sendPacket(ActionFailed.STATIC_PACKET);
		caster.sendMessage("Skill " + getId() + " [" + getSkillType() + "] isn't implemented.");
	}
}