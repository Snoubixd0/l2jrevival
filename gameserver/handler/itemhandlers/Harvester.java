package com.l2jrevival.gameserver.handler.itemhandlers;

import com.l2jrevival.Config;
import com.l2jrevival.gameserver.data.SkillTable;
import com.l2jrevival.gameserver.handler.IItemHandler;
import com.l2jrevival.gameserver.model.L2Skill;
import com.l2jrevival.gameserver.model.actor.Playable;
import com.l2jrevival.gameserver.model.actor.instance.Monster;
import com.l2jrevival.gameserver.model.actor.instance.Player;
import com.l2jrevival.gameserver.model.item.instance.ItemInstance;
import com.l2jrevival.gameserver.network.SystemMessageId;

public class Harvester implements IItemHandler
{
	@Override
	public void useItem(Playable playable, ItemInstance item, boolean forceUse)
	{
		if (!(playable instanceof Player))
			return;
		
		if (!Config.ALLOW_MANOR)
			return;
		
		if (!(playable.getTarget() instanceof Monster))
		{
			playable.sendPacket(SystemMessageId.INCORRECT_TARGET);
			return;
		}
		
		final Monster _target = (Monster) playable.getTarget();
		if (_target == null || !_target.isDead())
		{
			playable.sendPacket(SystemMessageId.INCORRECT_TARGET);
			return;
		}
		
		final L2Skill skill = SkillTable.getInstance().getInfo(2098, 1);
		if (skill != null)
			playable.useMagic(skill, false, false);
	}
}