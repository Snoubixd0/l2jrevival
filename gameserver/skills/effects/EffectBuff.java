package com.l2jrevival.gameserver.skills.effects;

import com.l2jrevival.gameserver.model.L2Effect;
import com.l2jrevival.gameserver.skills.Env;
import com.l2jrevival.gameserver.templates.skills.L2EffectType;

/**
 * @author mkizub
 */
public class EffectBuff extends L2Effect
{
	public EffectBuff(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.BUFF;
	}
	
	@Override
	public boolean onActionTime()
	{
		// just stop this effect
		return false;
	}
}