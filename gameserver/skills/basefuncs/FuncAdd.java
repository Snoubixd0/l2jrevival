package com.l2jrevival.gameserver.skills.basefuncs;

import com.l2jrevival.gameserver.skills.Env;
import com.l2jrevival.gameserver.skills.Stats;

public class FuncAdd extends Func
{
	public FuncAdd(Stats pStat, int pOrder, Object owner, Lambda lambda)
	{
		super(pStat, pOrder, owner, lambda);
	}
	
	@Override
	public void calc(Env env)
	{
		if (cond == null || cond.test(env))
			env.addValue(_lambda.calc(env));
	}
}