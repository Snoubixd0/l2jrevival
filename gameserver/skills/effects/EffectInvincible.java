package com.l2jrevival.gameserver.skills.effects;

import com.l2jrevival.gameserver.model.L2Effect;
import com.l2jrevival.gameserver.skills.Env;
import com.l2jrevival.gameserver.templates.skills.L2EffectType;

public class EffectInvincible extends L2Effect
{
	public EffectInvincible(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.INVINCIBLE;
	}
	
	@Override
	public boolean onStart()
	{
		getEffected().setIsInvul(true);
		return super.onStart();
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
	
	@Override
	public void onExit()
	{
		getEffected().setIsInvul(false);
		super.onExit();
	}
}