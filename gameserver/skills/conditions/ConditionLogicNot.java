package com.l2jrevival.gameserver.skills.conditions;

import com.l2jrevival.gameserver.skills.Env;

/**
 * The Class ConditionLogicNot.
 * @author mkizub
 */
public class ConditionLogicNot extends Condition
{
	
	private final Condition _condition;
	
	/**
	 * Instantiates a new condition logic not.
	 * @param condition the condition
	 */
	public ConditionLogicNot(Condition condition)
	{
		_condition = condition;
		if (getListener() != null)
			_condition.setListener(this);
	}
	
	/**
	 * Sets the listener.
	 * @param listener the new listener
	 * @see com.l2jrevival.gameserver.skills.conditions.Condition#setListener(com.l2jrevival.gameserver.skills.conditions.ConditionListener)
	 */
	@Override
	void setListener(ConditionListener listener)
	{
		if (listener != null)
			_condition.setListener(this);
		else
			_condition.setListener(null);
		super.setListener(listener);
	}
	
	/**
	 * Test impl.
	 * @param env the env
	 * @return true, if successful
	 * @see com.l2jrevival.gameserver.skills.conditions.Condition#testImpl(com.l2jrevival.gameserver.skills.Env)
	 */
	@Override
	public boolean testImpl(Env env)
	{
		return !_condition.test(env);
	}
}
