package com.l2jrevival.gameserver.communitybbs.Manager;

import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

import com.l2jrevival.Config;
import com.l2jrevival.gameserver.data.cache.HtmCache;
import com.l2jrevival.gameserver.communitybbs.BaseBBSManager;
import com.l2jrevival.gameserver.data.SkillTable;
import com.l2jrevival.gameserver.data.xml.TeleportLocationData;
import com.l2jrevival.gameserver.instancemanager.CastleManager;
import com.l2jrevival.gameserver.model.L2Skill;
import com.l2jrevival.gameserver.model.actor.instance.Player;
import com.l2jrevival.gameserver.model.entity.Siege;
import com.l2jrevival.gameserver.model.location.TeleportLocation;
import com.l2jrevival.gameserver.model.zone.ZoneId;
import com.l2jrevival.gameserver.network.SystemMessageId;
import com.l2jrevival.gameserver.network.serverpackets.ActionFailed;
import com.l2jrevival.gameserver.network.serverpackets.MagicSkillUse;
import com.l2jrevival.gameserver.network.serverpackets.PlaySound;

public class TeleportBBSManager extends BaseBBSManager
{	
	@Override
	public void parseCmd(String command, Player activeChar)
	{
		if (command.startsWith("_bbstele"))
		{
			if (!checkAllowed(activeChar))
			{
				return;
			}
			
			StringTokenizer st = new StringTokenizer(command, ";");
			st.nextToken();
									
			TeleportLocation list = TeleportLocationData.getInstance().getTeleportLocation(Integer.parseInt(st.nextToken()));
			
			if (list == null)
		          return;
						
			Siege siegeOnTeleportLocation = CastleManager.getInstance().getSiege(list.getX(), list.getY(), list.getZ());
	        if (siegeOnTeleportLocation != null && siegeOnTeleportLocation.isInProgress()) {
	        	activeChar.sendPacket(SystemMessageId.NO_PORT_THAT_IS_IN_SIGE);
	          return;
	        } 
	        if (!Config.PLAYER_FLAG_CAN_USE_GK && !activeChar.isGM() && activeChar.getPvpFlag() > 0 && !activeChar.isInsideZone(ZoneId.CUSTOM)) {
	        	activeChar.sendMessage("Don't run from PvP! You will be able to use the teleporter only after your flag is gone.");
	          return;
	        } 
	        if (!Config.KARMA_PLAYER_CAN_USE_GK && activeChar.getKarma() > 0) {
	        	activeChar.sendMessage("Go away, you're not welcome here.");
	          return;
	        } 
			        
	        if (list.isNoble() && !activeChar.isNoble()) {
	        	String content = HtmCache.getInstance().getHtm(CB_PATH + getFolder() + "nobleteleporter-no.htm");
	        	
	    		separateAndSend(content, activeChar);
	    		return;
	        } 
	        if (activeChar.isOnline()) {
	          L2Skill skill = SkillTable.FrequentSkill.ESCAPE_TELEPORT.getSkill();
	          if (skill != null) {
	            MagicSkillUse MSU = new MagicSkillUse(activeChar, activeChar, 9000, 1, 1, 0);
	            activeChar.sendPacket(new PlaySound("SkillSound.teleport_out"));
	            activeChar.sendPacket(MSU);
	            activeChar.broadcastPacket(MSU);
	            activeChar.useMagic(skill, false, false);
	          } 
	        } 
	        Calendar cal = Calendar.getInstance();
	        int price = list.getPrice();
	        if (!list.isNoble())
	          if (cal.get(11) >= 20 && cal.get(11) <= 23 && (cal.get(7) == 1 || cal.get(7) == 7))
	            price /= 2;  
	        if (Config.EFFECT_TELEPORT) {
	          L2Skill skill = SkillTable.FrequentSkill.ESCAPE_TELEPORT.getSkill();
	          if (skill != null) {
	            MagicSkillUse MSU = new MagicSkillUse(activeChar, activeChar, 8000, 1, 1, 0);
	            activeChar.sendPacket(MSU);
	            activeChar.broadcastPacket(MSU);
	            activeChar.useMagic(skill, false, false);
	          } 
	        } 
	        if (activeChar.destroyItemByItemId("Teleport ", list.isNoble() ? 6651 : 57, price, activeChar, true))
	        	activeChar.teleToLocation(list, 100); 
	        activeChar.sendPacket(ActionFailed.STATIC_PACKET);
	      } 
		
		if (command.startsWith("_bbspartygo"))
		{
			final StringTokenizer st = new StringTokenizer(command, " ");
			st.nextToken();
			
			if (st.countTokens() <= 0)
				return;
						
			if (!Config.KARMA_PLAYER_CAN_USE_GK && activeChar.getKarma() > 0)
			{
				activeChar.sendMessage("Go away, you're not welcome here.");
				return;
			}
			if (!activeChar.isInParty())
			{
				activeChar.sendMessage("This zone is only for parties.");
				return;
			}
			if (activeChar.getParty().getLeader() != activeChar)
			{
				activeChar.sendMessage("Only the party leader can request a teleport to the party zone.");
				return;
			}				
			for (Player p : activeChar.getParty().getPartyMembers())
			{
				if (!p.isInsideRadius(activeChar, 9000, false, false))
				{
					activeChar.sendMessage("One or more of your party members is not near. The teleport was canceled.");
					return;
				}
			}
						
			final TeleportLocation list = TeleportLocationData.getInstance().getTeleportLocation(Integer.parseInt(st.nextToken()));
			if (list == null)
				return;
			
			List<Player> party = activeChar.getParty().getPartyMembers();
			for (Player member : party) {		        
		        member.teleToLocation(list, 20);
		      } 
			
			activeChar.sendPacket(ActionFailed.STATIC_PACKET);
		}
		
		else
			_log.warning("Community Bord teleport destination ");
				
		String content = HtmCache.getInstance().getHtm(CB_PATH + getFolder() + "teleport.htm");
		separateAndSend(content, activeChar);
	}
	
	
	public boolean checkAllowed(Player activeChar)
	{
		String msg = null;
		
		if (activeChar.isSitting())
		{
			msg = "You can't use Community Teleport when you sit!";
		}
		else if (activeChar.isCastingNow())
		{
			msg = "You can't use Community Teleport when you cast!";
		}
		else if (activeChar.isDead())
		{
			msg = "You can't use Community Teleport when you dead!";
		}
		else if (activeChar.isInCombat())
		{
			msg = "You can't use Community Teleport when you in combat!";
		}
		
		if (msg != null)
		{
			activeChar.sendMessage(msg);
		}
		
		return msg == null;
	}
	
	@Override
	protected String getFolder()
	{
		return "top/";
	}
	
	public static TeleportBBSManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final TeleportBBSManager INSTANCE = new TeleportBBSManager();
	}
}