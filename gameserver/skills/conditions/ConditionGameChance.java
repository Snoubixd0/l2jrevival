package com.l2jrevival.gameserver.skills.conditions;

import com.l2jrevival.commons.random.Rnd;

import com.l2jrevival.gameserver.skills.Env;

/**
 * @author Advi
 */
public class ConditionGameChance extends Condition
{
	private final int _chance;
	
	public ConditionGameChance(int chance)
	{
		_chance = chance;
	}
	
	@Override
	public boolean testImpl(Env env)
	{
		return Rnd.get(100) < _chance;
	}
}
