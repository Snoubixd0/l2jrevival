package com.l2jrevival.gameserver.skills.conditions;

import com.l2jrevival.gameserver.skills.Env;

/**
 * The Class ConditionPlayerIsHero.
 */
public class ConditionPlayerIsHero extends Condition
{
	private final boolean _val;
	
	/**
	 * Instantiates a new condition player is hero.
	 * @param val the val
	 */
	public ConditionPlayerIsHero(boolean val)
	{
		_val = val;
	}
	
	@Override
	public boolean testImpl(Env env)
	{
		if (env.getPlayer() == null)
			return false;
		
		return (env.getPlayer().isHero() == _val);
	}
}