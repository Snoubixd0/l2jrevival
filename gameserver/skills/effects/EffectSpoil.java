package com.l2jrevival.gameserver.skills.effects;

import com.l2jrevival.gameserver.model.L2Effect;
import com.l2jrevival.gameserver.model.actor.ai.CtrlEvent;
import com.l2jrevival.gameserver.model.actor.instance.Monster;
import com.l2jrevival.gameserver.model.actor.instance.Player;
import com.l2jrevival.gameserver.network.SystemMessageId;
import com.l2jrevival.gameserver.network.serverpackets.SystemMessage;
import com.l2jrevival.gameserver.skills.Env;
import com.l2jrevival.gameserver.skills.Formulas;
import com.l2jrevival.gameserver.templates.skills.L2EffectType;

/**
 * This is the Effect support for spoil, originally done by _drunk_
 * @author Ahmed
 */
public class EffectSpoil extends L2Effect
{
	public EffectSpoil(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.SPOIL;
	}
	
	@Override
	public boolean onStart()
	{
		if (!(getEffector() instanceof Player))
			return false;
		
		if (!(getEffected() instanceof Monster))
			return false;
		
		final Monster target = (Monster) getEffected();
		if (target.isDead())
			return false;
		
		if (target.getSpoilerId() != 0)
		{
			getEffector().sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ALREADY_SPOILED));
			return false;
		}
		
		if (Formulas.calcMagicSuccess(getEffector(), target, getSkill()))
		{
			target.setSpoilerId(getEffector().getObjectId());
			getEffector().sendPacket(SystemMessage.getSystemMessage(SystemMessageId.SPOIL_SUCCESS));
		}
		target.getAI().notifyEvent(CtrlEvent.EVT_ATTACKED, getEffector());
		return true;
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
}