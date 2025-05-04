package com.l2jrevival.gameserver.handler.bypasshandlers;

import java.util.List;
import java.util.StringTokenizer;

import com.l2jrevival.gameserver.handler.BypassHandler;
import com.l2jrevival.gameserver.model.actor.instance.Player;
import com.l2jrevival.gameserver.model.item.instance.ItemInstance;
import com.l2jrevival.gameserver.network.SystemMessageId;
import com.l2jrevival.gameserver.network.serverpackets.ExAutoSoulShot;
import com.l2jrevival.gameserver.network.serverpackets.SystemMessage;


public final class AutoSSBypass implements BypassHandler
{
	@Override
	public void handleCommand(Player player, String command)
	{
		final StringTokenizer stringTokenizer = new StringTokenizer(command, " ");
		stringTokenizer.nextToken();
		final int itemId = Integer.parseInt(stringTokenizer.nextToken().split("=")[1]);
		final int type = Integer.parseInt(stringTokenizer.nextToken().split("=")[1]);
		handleAutoSS(player, itemId, type);
	}

	private void handleAutoSS(Player activeChar, int itemId, int type)
	{
		if (!activeChar.isInStoreMode() && activeChar.getActiveRequester() == null && !activeChar.isDead())
		{
			ItemInstance item = activeChar.getInventory().getItemByItemId(itemId);
			if (item == null)
				return;
			
			if (type == 1)
			{
				// Fishingshots are not automatic on retail
				if (itemId < 6535 || itemId > 6540)
				{
					// Attempt to charge first shot on activation
					if (itemId == 6645 || itemId == 6646 || itemId == 6647)
					{
						if (activeChar.getPet() != null)
						{
							// Cannot activate bss automation during Olympiad.
							if (itemId == 6647 && activeChar.isInOlympiadMode())
							{
								activeChar.sendPacket(SystemMessageId.THIS_ITEM_IS_NOT_AVAILABLE_FOR_THE_OLYMPIAD_EVENT);
								return;
							}
							
							if (itemId == 6645)
							{
								if (activeChar.getPet().getSoulShotsPerHit() > item.getCount())
								{
									activeChar.sendPacket(SystemMessageId.NOT_ENOUGH_SOULSHOTS_FOR_PET);
									return;
								}
							}
							else
							{
								if (activeChar.getPet().getSpiritShotsPerHit() > item.getCount())
								{
									activeChar.sendPacket(SystemMessageId.NOT_ENOUGH_SPIRITSHOTS_FOR_PET);
									return;
								}
							}
							
							// start the auto soulshot use
							activeChar.addAutoSoulShot(itemId);
							activeChar.sendPacket(new ExAutoSoulShot(itemId, type));
							activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.USE_OF_S1_WILL_BE_AUTO).addItemName(itemId));
							activeChar.rechargeShots(true, true);
							activeChar.getPet().rechargeShots(true, true);
						}
						else
							activeChar.sendPacket(SystemMessageId.NO_SERVITOR_CANNOT_AUTOMATE_USE);
					}
					else
					{
						// Cannot activate bss automation during Olympiad.
						if (itemId >= 3947 && itemId <= 3952 && activeChar.isInOlympiadMode())
						{
							activeChar.sendPacket(SystemMessageId.THIS_ITEM_IS_NOT_AVAILABLE_FOR_THE_OLYMPIAD_EVENT);
							return;
						}
						
						// Activate the visual effect
						activeChar.addAutoSoulShot(itemId);
						activeChar.sendPacket(new ExAutoSoulShot(itemId, type));
						
						// start the auto soulshot use
						if (activeChar.getActiveWeaponItem() != activeChar.getFistsWeaponItem() && item.getItem().getCrystalType() == activeChar.getActiveWeaponItem().getCrystalType())
							activeChar.rechargeShots(true, true);
						else
						{
							if ((itemId >= 2509 && itemId <= 2514) || (itemId >= 3947 && itemId <= 3952) || itemId == 5790)
								activeChar.sendPacket(SystemMessageId.SPIRITSHOTS_GRADE_MISMATCH);
							else
								activeChar.sendPacket(SystemMessageId.SOULSHOTS_GRADE_MISMATCH);
						}
						
						// In both cases (match/mismatch), that message is displayed.
						activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.USE_OF_S1_WILL_BE_AUTO).addItemName(itemId));
					}
				}
			}
			else if (type == 0)
			{
				// cancel the auto soulshot use
				activeChar.removeAutoSoulShot(itemId);
				activeChar.sendPacket(new ExAutoSoulShot(itemId, type));
				activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.AUTO_USE_OF_S1_CANCELLED).addItemName(itemId));
			}
		}
	}

	@Override
	public List<String> getCommands()
	{
		return List.of("RequestAutoShot:");
	}
	
	public static final AutoSSBypass getInstance()
	{
		return InstanceHolder.INSTANCE;
	}
	
	private static final class InstanceHolder
	{
		private static final AutoSSBypass INSTANCE = new AutoSSBypass();
	}
}