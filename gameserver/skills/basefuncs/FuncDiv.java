package com.l2jrevival.gameserver.skills.basefuncs;

import com.l2jrevival.gameserver.skills.Env;
import com.l2jrevival.gameserver.skills.Stats;

public class FuncDiv extends Func
{
	public FuncDiv(Stats pStat, int pOrder, Object owner, Lambda lambda)
	{
		super(pStat, pOrder, owner, lambda);
	}
	
	@Override
	public void calc(Env env)
	{
		if (cond == null || cond.test(env))
			env.divValue(_lambda.calc(env));
	}
}