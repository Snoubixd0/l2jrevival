package com.l2jrevival.gameserver.model.actor.instance;

import java.util.StringTokenizer;

import com.l2jrevival.Config;
import com.l2jrevival.gameserver.model.actor.instance.Player;
import com.l2jrevival.gameserver.model.actor.template.NpcTemplate;
import com.l2jrevival.gameserver.network.serverpackets.NpcHtmlMessage;

import com.l2jrevival.Dungeon.DungeonManager;


/**
 * @author Anarchy
 */
public class DungeonManagerNpc extends Folk
{
	public DungeonManagerNpc(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (command.startsWith("dungeon"))
		{
			if (DungeonManager.getInstance().isInDungeon(player) || player.isInOlympiadMode())
			{
				player.sendMessage("You are currently unable to enter a Dungeon. Please try again later.");
				return;
			}

			int dungeonId = Integer.parseInt(command.substring(8)); 

			String ip = player.getIP(); // HWID
			if (DungeonManager.getInstance().getPlayerData().containsKey(ip) &&
				DungeonManager.getInstance().getPlayerData().get(ip)[dungeonId] > 0)
			{
				long total = (DungeonManager.getInstance().getPlayerData().get(ip)[dungeonId] + (1000 * 60 * 60 * 12)) - System.currentTimeMillis();
				if (total > 0)
				{
					player.sendMessage("You must wait before re-entering this Dungeon.");
					return;
				}
			}

			if (player.getInventory().getItemByItemId(Config.DUNGEON_COIN_ID) == null)
			{
				DungeonManagerNpc.mainHtml(player, 0);
				return;
			}

			if (dungeonId == 2)
			{
				if (player.getParty() == null || player.getParty().getMemberCount() < 4)
				{
					player.sendMessage("You need a party of 4 to enter this Dungeon.");
					return;
				}
			}

			player.destroyItemByItemId("Quest", Config.DUNGEON_COIN_ID, Config.CONT_DUNGEON_ITEM, player, true);

			if (dungeonId == 1 || dungeonId == 2)
			{
				DungeonManager.getInstance().enterDungeon(dungeonId, player);
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
	public static void mainHtml(Player activeChar, int time)
	{		
	if (activeChar.isOnline())
    {
        NpcHtmlMessage nhm = new NpcHtmlMessage(5);
        StringBuilder html1 = new StringBuilder("");
        html1.append("<html><head><title>Dungeon</title></head><body><center>");
        html1.append("<br>");
        html1.append("Your character Cont Item.");
        html1.append("</center>");
        html1.append("</body></html>");
        nhm.setHtml(html1.toString());
        activeChar.sendPacket(nhm);
    }
			
	}
	
	public static String getPlayerStatus(Player player, int dungeonId)
	{
		String s = "You can enter";
		
		String ip = player.getIP(); // Is ip or hwid?
		if (DungeonManager.getInstance().getPlayerData().containsKey(ip) && DungeonManager.getInstance().getPlayerData().get(ip)[dungeonId] > 0)
		{
			long total = (DungeonManager.getInstance().getPlayerData().get(ip)[dungeonId] + (1000*60*60*12)) - System.currentTimeMillis();
			
			if (total > 0)
			{
				int hours = (int) (total/1000/60/60);
				int minutes = (int) ((total/1000/60) - hours*60);
				int seconds = (int) ((total/1000) - (hours*60*60 + minutes*60));
				
				s = String.format("%02d:%02d:%02d", hours, minutes, seconds);
			}
		}
		
		return s;
	}
	
	@Override
	public void showChatWindow(Player player, int val)
	{
		NpcHtmlMessage htm = new NpcHtmlMessage(getObjectId());
		htm.setFile("data/html/mods/dungeon/"+getNpcId()+(val == 0 ? "" : "-"+val)+".htm");
		
		String[] s = htm.getHtml().split("%");
		for (int i = 0; i < s.length; i++)
		{
			if (i % 2 > 0 && s[i].contains("dung "))
			{
				StringTokenizer st = new StringTokenizer(s[i]);
				st.nextToken();
				htm.replace("%"+s[i]+"%", getPlayerStatus(player, Integer.parseInt(st.nextToken())));
			}
		}
		
		htm.replace("%objectId%", getObjectId()+"");
		
		player.sendPacket(htm);
	}
	
	@Override
	public String getHtmlPath(int npcId, int val)
	{
		String filename = "";
		if (val == 0)
			filename = "" + npcId;
		else
			filename = npcId + "-" + val;
		
		return "data/html/mods/dungeon/" + filename + ".htm";
	}
}
