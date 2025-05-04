package com.l2jrevival.gameserver.handler.skillhandlers;

import com.l2jrevival.gameserver.handler.ISkillHandler;
import com.l2jrevival.gameserver.model.L2Skill;
import com.l2jrevival.gameserver.model.WorldObject;
import com.l2jrevival.gameserver.model.actor.Creature;
import com.l2jrevival.gameserver.model.actor.ai.CtrlEvent;
import com.l2jrevival.gameserver.model.actor.instance.Monster;
import com.l2jrevival.gameserver.model.actor.instance.Player;
import com.l2jrevival.gameserver.network.SystemMessageId;
import com.l2jrevival.gameserver.network.serverpackets.SystemMessage;
import com.l2jrevival.gameserver.skills.Formulas;
import com.l2jrevival.gameserver.templates.skills.L2SkillType;

/**
 * @author _drunk_
 */
public class Spoil implements ISkillHandler
{
	private static final L2SkillType[] SKILL_IDS =
	{
		L2SkillType.SPOIL
	};
	
	@Override
	public void useSkill(Creature activeChar, L2Skill skill, WorldObject[] targets)
	{
		if (!(activeChar instanceof Player))
			return;
		
		if (targets == null)
			return;
		
		for (WorldObject tgt : targets)
		{
			if (!(tgt instanceof Monster))
				continue;
			
			final Monster target = (Monster) tgt;
			if (target.isDead())
				continue;
			
			if (target.getSpoilerId() != 0)
			{
				activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ALREADY_SPOILED));
				continue;
			}
			
			if (Formulas.calcMagicSuccess(activeChar, (Creature) tgt, skill))
			{
				target.setSpoilerId(activeChar.getObjectId());
				activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.SPOIL_SUCCESS));
			}
			else
				activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_RESISTED_YOUR_S2).addCharName(target).addSkillName(skill.getId()));
			
			target.getAI().notifyEvent(CtrlEvent.EVT_ATTACKED, activeChar);
		}
	}
	
	@Override
	public L2SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}