package com.l2jrevival.gameserver.instancemanager;

import com.l2jrevival.gameserver.handler.admincommandhandlers.AdminCustom;
import com.l2jrevival.gameserver.model.actor.instance.Player;


public class VIPFreeHTML implements Runnable {
  private Player _activeChar;
  
  public VIPFreeHTML(Player activeChar) {
    _activeChar = activeChar;
  }
  
  @Override
public void run() {
    if (_activeChar.isOnline())
      AdminCustom.showHtml(_activeChar); 
  }
}
