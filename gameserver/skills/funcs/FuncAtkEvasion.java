package com.l2jrevival.gameserver.skills.funcs;

import com.l2jrevival.gameserver.skills.Env;
import com.l2jrevival.gameserver.skills.Formulas;
import com.l2jrevival.gameserver.skills.Stats;
import com.l2jrevival.gameserver.skills.basefuncs.Func;

public class FuncAtkEvasion extends Func
{
	static final FuncAtkEvasion _fae_instance = new FuncAtkEvasion();
	
	public static Func getInstance()
	{
		return _fae_instance;
	}
	
	private FuncAtkEvasion()
	{
		super(Stats.EVASION_RATE, 0x10, null, null);
	}
	
	@Override
	public void calc(Env env)
	{
		env.addValue(Formulas.BASE_EVASION_ACCURACY[env.getCharacter().getDEX()] + env.getCharacter().getLevel());
	}
}