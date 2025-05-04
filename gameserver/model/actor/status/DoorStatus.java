package com.l2jrevival.gameserver.model.actor.status;

import com.l2jrevival.gameserver.model.actor.instance.Door;

public class DoorStatus extends CreatureStatus
{
	public DoorStatus(Door activeChar)
	{
		super(activeChar);
	}
	
	@Override
	public Door getActiveChar()
	{
		return (Door) super.getActiveChar();
	}
}