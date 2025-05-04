package com.l2jrevival.gameserver.handler.itemhandlers;

import java.util.concurrent.TimeUnit;

import com.l2jrevival.gameserver.handler.IItemHandler;
import com.l2jrevival.gameserver.model.actor.Playable;
import com.l2jrevival.gameserver.model.actor.instance.Player;
import com.l2jrevival.gameserver.model.item.instance.ItemInstance;
import com.l2jrevival.gameserver.model.item.kind.Item;

public class AutofarmTime implements IItemHandler
{
	@Override
	public void useItem(Playable playable, ItemInstance item, boolean forceUse)
	{
		if (!(playable instanceof Player))
			return;
		
		final Player player = playable.getActingPlayer();
		if (player == null)
			return;
		
		if (!player.destroyItem("Consume", item, 1, player, true))
			return;
		
		final Item itemTemplate = item.getItem();
		player.setAutofarmTimeLeft(player.getAutofarmTimeLeft() + TimeUnit.HOURS.toMillis(itemTemplate.getAutofarmTime()));
		player.sendMessage("You have received " + itemTemplate.getAutofarmTime() + " hour(s) of autofarm time.");
	}
}