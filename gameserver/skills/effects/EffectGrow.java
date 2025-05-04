package com.l2jrevival.gameserver.skills.effects;

import com.l2jrevival.gameserver.model.L2Effect;
import com.l2jrevival.gameserver.model.actor.Npc;
import com.l2jrevival.gameserver.skills.AbnormalEffect;
import com.l2jrevival.gameserver.skills.Env;
import com.l2jrevival.gameserver.templates.skills.L2EffectType;

public class EffectGrow extends L2Effect
{
	public EffectGrow(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.BUFF;
	}
	
	@Override
	public boolean onStart()
	{
		if (getEffected() instanceof Npc)
		{
			Npc npc = (Npc) getEffected();
			npc.setCollisionRadius(npc.getCollisionRadius() * 1.19);
			
			getEffected().startAbnormalEffect(AbnormalEffect.GROW);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
	
	@Override
	public void onExit()
	{
		if (getEffected() instanceof Npc)
		{
			Npc npc = (Npc) getEffected();
			npc.setCollisionRadius(npc.getTemplate().getCollisionRadius());
			
			getEffected().stopAbnormalEffect(AbnormalEffect.GROW);
		}
	}
}