package com.l2jrevival.gameserver.handler.itemhandlers.aio;

import com.l2jrevival.Config;
import com.l2jrevival.gameserver.handler.IItemHandler;
import com.l2jrevival.gameserver.handler.admincommandhandlers.AdminAiox;
import com.l2jrevival.gameserver.model.World;
import com.l2jrevival.gameserver.model.actor.Playable;
import com.l2jrevival.gameserver.model.actor.instance.Player;
import com.l2jrevival.gameserver.model.item.instance.ItemInstance;
import com.l2jrevival.gameserver.network.clientpackets.Say2;
import com.l2jrevival.gameserver.network.serverpackets.CreatureSay;

public class AioEterno implements IItemHandler {
  @Override
public void useItem(Playable playable, ItemInstance item, boolean forceUse) {
    if (!(playable instanceof Player))
      return; 
    if (!Config.ENABLE_AIO_SYSTEM)
      return; 
    Player activeChar = (Player)playable;
    if (activeChar.isInOlympiadMode()) {
      activeChar.sendMessage("SYS: You can not do that.");
      return;
    } 
    if (activeChar.isAioEterno()) {
      activeChar.sendMessage("SYS: You can not do that.");
      return;
    } 
	playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
	activeChar.setAio(true);
	activeChar.setNoble(true, true);
	AdminAiox.updateDatabase(activeChar, true);

	
    if (Config.CHANGE_AIO_NAME)
        AdminAiox.nameChanger("[AIO]", activeChar); 
    if (Config.ALLOW_AIO_NCOLOR)
        activeChar.getAppearance().setNameColor(Config.AIO_NCOLOR); 
      if (Config.ALLOW_AIO_TCOLOR)
        activeChar.getAppearance().setTitleColor(Config.AIO_TCOLOR); 
      activeChar.rewardAioSkills();
      if (Config.ALLOW_AIOX_SET_ITEM)
    	  AdminAiox.giveAioItems(activeChar); 
 
      activeChar.getStat().addExp(activeChar.getStat().getExpForLevel(81));
      
	activeChar.broadcastUserInfo();

	for (Player allgms : World.getAllGMs())
		allgms.sendPacket(new CreatureSay(0, Say2.SHOUT, "(Aio Manager)", activeChar.getName() + " ativou Aio Eternal."));
  }
}
