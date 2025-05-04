/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jrevival.gameserver.model.actor.instance;

import java.util.StringTokenizer;

import com.l2jrevival.gameserver.data.SkillTable;
import com.l2jrevival.gameserver.instancemanager.CastleManager;
import com.l2jrevival.gameserver.instancemanager.ClanHallManager;
import com.l2jrevival.gameserver.model.actor.Npc;
import com.l2jrevival.gameserver.model.actor.ai.CtrlIntention;
import com.l2jrevival.gameserver.model.actor.template.NpcTemplate;
import com.l2jrevival.gameserver.model.entity.ClanHall;
import com.l2jrevival.gameserver.model.pledge.Clan;
import com.l2jrevival.gameserver.network.SystemMessageId;
import com.l2jrevival.gameserver.network.serverpackets.ActionFailed;
import com.l2jrevival.gameserver.network.serverpackets.ExShowScreenMessage;
import com.l2jrevival.gameserver.network.serverpackets.MagicSkillUse;
import com.l2jrevival.gameserver.network.serverpackets.MyTargetSelected;
import com.l2jrevival.gameserver.network.serverpackets.NpcHtmlMessage;
import com.l2jrevival.gameserver.network.serverpackets.SocialAction;
import com.l2jrevival.gameserver.network.serverpackets.SystemMessage;
import com.l2jrevival.gameserver.network.serverpackets.ValidateLocation;
import com.l2jrevival.util.Rnd;

public final class CustomBuffer
  extends Npc
{
  public CustomBuffer(int objectId, NpcTemplate template)
  {
    super(objectId, template);
  }
  
  @Override
public void onBypassFeedback(Player player, String command)
  {
    StringTokenizer st = new StringTokenizer(command, " ");
    String actualCommand = st.nextToken();
    
    int buffid = 0;
    int bufflevel = 1;
    String nextWindow = null;
    if (st.countTokens() == 3)
    {
      buffid = Integer.valueOf(st.nextToken()).intValue();
      bufflevel = Integer.valueOf(st.nextToken()).intValue();
      nextWindow = st.nextToken();
    }
    else if (st.countTokens() == 1)
    {
      buffid = Integer.valueOf(st.nextToken()).intValue();
    }
    if (actualCommand.equalsIgnoreCase("clanleader"))
    {
      Clan clan = player.getClan();
      if (clan != null)
      {
        ClanHall clanHall = ClanHallManager.getInstance().getClanHallByOwner(clan);
        if (clanHall != null)
        {
          if (buffid != 0) {}
          player.broadcastPacket(new MagicSkillUse(this, player, buffid, bufflevel, 5, 0));
          SkillTable.getInstance().getInfo(buffid, bufflevel).getEffects(this, player);
          player.sendPacket(new SystemMessage(SystemMessageId.YOU_FEEL_S1_EFFECT).addSkillName(buffid, bufflevel));
          
          showMessageWindow(player);
          showChatWindow(player, nextWindow);
        }
        else
        {
          player.sendMessage("Your Clan needs a clan hall to receive this buff.");
          player.sendPacket(new ExShowScreenMessage("Clan Hall member buff", 6000));
          showMessageWindow(player);
        }
      }
      else
      {
        player.sendMessage("You do not have a Clan with Clan hall.");
        player.sendPacket(new ExShowScreenMessage("Clan Hall member buff", 6000));
        showMessageWindow(player);
      }
    }
    if (actualCommand.equalsIgnoreCase("clanmember"))
    {
      Clan activeCharClan = player.getClan();
      if (activeCharClan != null)
      {
        if (buffid != 0) {}
        player.broadcastPacket(new MagicSkillUse(this, player, buffid, bufflevel, 5, 0));
        SkillTable.getInstance().getInfo(buffid, bufflevel).getEffects(this, player);
        player.sendPacket(new SystemMessage(SystemMessageId.YOU_FEEL_S1_EFFECT).addSkillName(buffid, bufflevel));
        
        showMessageWindow(player);
        showChatWindow(player, nextWindow);
      }
      else
      {
        player.sendMessage("You don't have a Clan.");
        showMessageWindow(player);
      }
    }
    if (actualCommand.equalsIgnoreCase("castlelord")) {
      if (player.getClan() != null)
      {
        if ((CastleManager.getInstance().getCastleByOwner(player.getClan()) != null) && (player.isClanLeader()))
        {
          if (buffid != 0) {}
          player.broadcastPacket(new MagicSkillUse(this, player, buffid, bufflevel, 5, 0));
          SkillTable.getInstance().getInfo(buffid, bufflevel).getEffects(this, player);
          player.sendPacket(new SystemMessage(SystemMessageId.YOU_FEEL_S1_EFFECT).addSkillName(buffid, bufflevel));
          
          showMessageWindow(player);
          showChatWindow(player, nextWindow);
        }
        else
        {
          player.sendMessage("Only Castle Leaders can receive this buff.");
          player.sendPacket(new ExShowScreenMessage("Castle Leader buff", 6000));
          showMessageWindow(player);
        }
      }
      else
      {
        player.sendMessage("You are not the Owner of a Clan with a Castle.");
        player.sendPacket(new ExShowScreenMessage("Castle Leader buff", 6000));
        showMessageWindow(player);
      }
    }
    if (actualCommand.equalsIgnoreCase("castlemember")) {
      if (player.getClan() != null)
      {
        if (CastleManager.getInstance().getCastleByOwner(player.getClan()) != null)
        {
          if (buffid != 0) {}
          player.broadcastPacket(new MagicSkillUse(this, player, buffid, bufflevel, 5, 0));
          SkillTable.getInstance().getInfo(buffid, bufflevel).getEffects(this, player);
          player.sendPacket(new SystemMessage(SystemMessageId.YOU_FEEL_S1_EFFECT).addSkillName(buffid, bufflevel));
          
          showMessageWindow(player);
          showChatWindow(player, nextWindow);
        }
        else
        {
          player.sendMessage("Only members of a Castle can receive this buff.");
          player.sendPacket(new ExShowScreenMessage("Castle Member buff", 6000));
          showMessageWindow(player);
        }
      }
      else
      {
        player.sendMessage("You are not part of a Clan with a Castle.");
        player.sendPacket(new ExShowScreenMessage("Castle Member buff", 6000));
        showMessageWindow(player);
      }
    }
    if (actualCommand.equalsIgnoreCase("hero"))
    {
      if (player.isHero())
      {
        if (buffid != 0) {}
        player.broadcastPacket(new MagicSkillUse(this, player, buffid, bufflevel, 5, 0));
        SkillTable.getInstance().getInfo(buffid, bufflevel).getEffects(this, player);
        player.sendPacket(new SystemMessage(SystemMessageId.YOU_FEEL_S1_EFFECT).addSkillName(buffid, bufflevel));
        
        showMessageWindow(player);
        showChatWindow(player, nextWindow);
      }
      else
      {
        player.sendMessage("Only Hero characters can receive this buff.");
        player.sendPacket(new ExShowScreenMessage("Hero buff", 6000));
        showMessageWindow(player);
      }
    }
    else {
      super.onBypassFeedback(player, command);
    }
  }
  
  @Override
public void onAction(Player player)
  {
    if (this != player.getTarget())
    {
      player.setTarget(this);
      player.sendPacket(new MyTargetSelected(getObjectId(), player.getLevel() - getLevel()));
      player.sendPacket(new ValidateLocation(this));
    }
    else if (isInsideRadius(player, 150, false, false))
    {
      SocialAction sa = new SocialAction(this, Rnd.get(8));
      broadcastPacket(sa);
      player.setCurrentFolkNPC(this);
      showMessageWindow(player);
      player.sendPacket(ActionFailed.STATIC_PACKET);
    }
    else
    {
      player.getAI().setIntention(CtrlIntention.INTERACT, this);
      player.sendPacket(ActionFailed.STATIC_PACKET);
    }
  }
  
  private void showMessageWindow(Player player)
  {
    String filename = "data/html/CustomBuffer/" + getNpcId() + ".htm";
    
    filename = getHtmlPath(getNpcId(), 0);
    NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
    html.setFile(filename);
    html.replace("%objectId%", String.valueOf(getObjectId()));
    html.replace("%npcname%", getName());
    player.sendPacket(html);
  }
  
  @Override
public String getHtmlPath(int npcId, int val)
  {
    String pom = "";
    if (val == 0) {
      pom = "" + npcId;
    } else {
      pom = npcId + "-" + val;
    }
    return "data/html/CustomBuffer/" + pom + ".htm";
  }
}

