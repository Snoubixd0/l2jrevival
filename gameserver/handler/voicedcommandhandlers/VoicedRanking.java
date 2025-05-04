package com.l2jrevival.gameserver.handler.voicedcommandhandlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import com.l2jrevival.Config;
import com.l2jrevival.L2DatabaseFactory;
import com.l2jrevival.events.pvpevent.PvPEvent;
import com.l2jrevival.gameserver.data.sql.ClanTable;
import com.l2jrevival.gameserver.handler.IVoicedCommandHandler;
import com.l2jrevival.gameserver.model.actor.instance.Player;
import com.l2jrevival.gameserver.model.item.kind.Weapon;
import com.l2jrevival.gameserver.data.ItemTable;
import com.l2jrevival.gameserver.model.pledge.Clan;
import com.l2jrevival.gameserver.network.serverpackets.NpcHtmlMessage;

public class VoicedRanking implements IVoicedCommandHandler {
	private static final String[] VOICED_COMMANDS = new String[] { "pvpevent","pvpEvent","pvp", "enchant", "pks", "clan", "ranking", "5x5", "9x9" };
	
	@Override
	public boolean useVoicedCommand(String command, Player activeChar, String target) {
		if (command.equals("ranking")) {
			showRankingHtml(activeChar);
		}
		if ((command.equals("pvpEvent") || command.equals("pvpevent")) && Config.PVP_EVENT_ENABLED){
			PvPEvent.getTopHtml(activeChar); 
		}
		else if (command.equals("pvp")) {
			NpcHtmlMessage htm = new NpcHtmlMessage(5);
			StringBuilder tb = new StringBuilder();
			tb.append("<html>");
			tb.append("<body>");
			tb.append("<center>");
			tb.append("<img src=\"l2ui_ch3.herotower_deco\" width=256 height=32><br>");
			tb.append("<table border=\"1\" width=\"300\">");
			tb.append("<tr>");
			tb.append("<td><center>Rank</center></td>");
			tb.append("<td><center>Character</center></td>");
			tb.append("<td><center>Pvp's</center></td>");
			tb.append("<td><center>Status</center></td>");
			tb.append("</tr>");
			try (Connection con = L2DatabaseFactory.getInstance().getConnection()) {
				PreparedStatement statement = con.prepareStatement("SELECT char_name,pvpkills,online FROM characters WHERE pvpkills>0 AND accesslevel=0 order by pvpkills desc limit 15");
				ResultSet result = statement.executeQuery();
				int pos = 0;
				while (result.next()) {
					String status, pvps = result.getString("pvpkills");
					String name = result.getString("char_name");
					if (name.equals("WWWWWWWWWWWWWWWW") || name.equals("WWWWWWWWWWWWWWW") || name.equals("WWWWWWWWWWWWWW") || name.equals("WWWWWWWWWWWWW") || name.equals("WWWWWWWWWWWW") || name.equals("WWWWWWWWWWW") || name.equals("WWWWWWWWWW") || name.equals("WWWWWWWWW") || name.equals("WWWWWWWW") || name.equals("WWWWWWW") || name.equals("WWWWWW")) {
						name = name.substring(0, 3) + "..";
					} else if (name.length() > 14) {
						name = name.substring(0, 14) + "..";
					} 
					pos++;
					String statu = result.getString("online");
					if (statu.equals("1")) {
						status = "<font color=00FF00>Online</font>";
					} else {
						status = "<font color=FF0000>Offline</font>";
					} 
					tb.append("<tr>");
					tb.append("<td><center><font color =\"AAAAAA\">" + pos + "</font></center></td>");
					tb.append("<td><center><font color=00FFFF>" + name + "</font></center></td>");
					tb.append("<td><center>" + pvps + "</center></td>");
					tb.append("<td><center>" + status + "</center></td>");
					tb.append("</tr>");
				} 
				statement.close();
				result.close();
			} catch (Exception exception) {}
			
			tb.append("</table>");
			tb.append("<br>");
			tb.append("<br>");
			tb.append("<a action=\"bypass voiced_ranking\">Back to Rankings</a>");
			tb.append("</center>");
			tb.append("</body>");
			tb.append("</html>");
			htm.setHtml(tb.toString());
			activeChar.sendPacket(htm);
			
		} else if (command.equals("enchant")) {
			NpcHtmlMessage htm = new NpcHtmlMessage(5);
			StringBuilder tb = new StringBuilder();
			tb.append("<html>");
			tb.append("<body>");
			tb.append("<center>");
			tb.append("<img src=\"revival.revivallogo\" width=270 height=65><br>");
			tb.append("<img src=\"L2UI.SquareGray\" width=300 height=1><br>");
			tb.append("<td><center><font color=ec0505>Top 10 Enchants</font></center></td>");
			tb.append("<img src=\"L2UI.SquareGray\" width=300 height=1><br>");
			tb.append("<table border=\"1\" width=\"300\">");
			tb.append("<tr>");
			tb.append("<td><center>Rank</center></td>");
			tb.append("<td><center>Name</center></td>");
			tb.append("<td><center>Weapon</center></td>");
			tb.append("<td><center>Enchant</center></td>");
			tb.append("</tr>");
			try (Connection con = L2DatabaseFactory.getInstance().getConnection()) {
				PreparedStatement stm = con.prepareStatement("SELECT C.char_name, I.enchant_level,I.item_id\r\n"
		                + "FROM characters C\r\n"
		                + "INNER JOIN items I\r\n"
		                + "ON C.obj_Id = I.owner_id\r\n"
		                + "WHERE C.accesslevel = 0\r\n"
		                + "AND I.item_id IN (9600,9601,9602,9603,9604,9605,9606,9607,9608,9609,9610,9611,9612,9613,9614,9615,9616,9617,9618,9619,9620,9621,9622,9623,9624,9625,9626,9627,9628,9629,9630,9631,9632,9633,9634,9635,9636,9637,9638,9639,9640,9641,9641,9642,9643,9644,9645,9646,9647,9648,9649,9650,9651,9652,6581,6582,6583,6584,6585,6586,6587,6588,6589,6590,6591,6592,6593,6594,6595,6596,6597,6598,6599,6600,6601,6602,6603,6604,6605,6606,6607,6608,6609,6610,13033,13034,13035,13036,13037,13038,13039)\r\n"
		                + "ORDER BY I.enchant_level DESC LIMIT 10");
		            ResultSet rset = stm.executeQuery();
		            int pos = 0;
		            while (rset.next())
		            {
		            	String name = rset.getString("char_name");
		                int enchantLevel = rset.getInt("enchant_level");
		                int itemId = rset.getInt("item_id");
		                String weaponName = "";
		                for (Map.Entry<Integer, Weapon> entry : ItemTable._weapons.entrySet()) {
		                    if((itemId== entry.getKey())) {
		                        weaponName = entry.getValue().getName();
		                        pos++;
		                    }
		                }
		                tb.append("<tr>");
		                tb.append("<td><center><font color=009900>" + pos + "</font></center></td>");
						tb.append("<td><center><font color=ffff00>" + name + "</font></center></td>");
						tb.append("<td><center><font color=ff9900>" + weaponName + "</font></center></td>");
						tb.append("<td><center><font color=ff66ff>" + enchantLevel + "</font></center></td>");
						tb.append("</tr>");
		            } 
		            rset.close();
		            stm.close();
			} catch (Exception exception) {}
			tb.append("</table>");
			tb.append("<br>");
			tb.append("<br>");
			tb.append("<a action=\"bypass voiced_ranking\">Back to Rankings</a>");
			tb.append("</center>");
			tb.append("</body>");
			tb.append("</html>");
			htm.setHtml(tb.toString());
			activeChar.sendPacket(htm);
			
		} else if (command.equals("pks")) {
			NpcHtmlMessage htm = new NpcHtmlMessage(5);
			StringBuilder tb = new StringBuilder();
			tb.append("<html>");
			tb.append("<body>");
			tb.append("<center>");
			tb.append("<img src=\"l2ui_ch3.herotower_deco\" width=256 height=32><br>");
			tb.append("<table border=\"1\" width=\"300\">");
			tb.append("<tr>");
			tb.append("<td><center>Rank</center></td>");
			tb.append("<td><center>Character</center></td>");
			tb.append("<td><center>Pk's</center></td>");
			tb.append("<td><center>Status</center></td>");
			tb.append("</tr>");
			try (Connection con = L2DatabaseFactory.getInstance().getConnection()) {
				PreparedStatement statement = con.prepareStatement("SELECT char_name,pkkills,online FROM characters WHERE pvpkills>0 AND accesslevel=0 order by pkkills desc limit 15");
				ResultSet result = statement.executeQuery();
				int pos = 0;
				while (result.next()) {
					String status, pks = result.getString("pkkills");
					String name = result.getString("char_name");
					if (name.equals("WWWWWWWWWWWWWWWW") || name.equals("WWWWWWWWWWWWWWW") || name.equals("WWWWWWWWWWWWWW") || name.equals("WWWWWWWWWWWWW") || name.equals("WWWWWWWWWWWW") || name.equals("WWWWWWWWWWW") || name.equals("WWWWWWWWWW") || name.equals("WWWWWWWWW") || name.equals("WWWWWWWW") || name.equals("WWWWWWW") || name.equals("WWWWWW")) {
						name = name.substring(0, 3) + "..";
					} else if (name.length() > 14) {
						name = name.substring(0, 14) + "..";
					} 
					pos++;
					String statu = result.getString("online");
					if (statu.equals("1")) {
						status = "<font color=00FF00>Online</font>";
					} else {
						status = "<font color=FF0000>Offline</font>";
					} 
					tb.append("<tr>");
					tb.append("<td><center><font color =\"AAAAAA\">" + pos + "</font></center></td>");
					tb.append("<td><center><font color=00FFFF>" + name + "</font></center></td>");
					tb.append("<td><center>" + pks + "</center></td>");
					tb.append("<td><center>" + status + "</center></td>");
					tb.append("</tr>");
				} 
				statement.close();
				result.close();
			} catch (Exception exception) {}
			tb.append("</table>");
			tb.append("<br>");
			tb.append("<br>");
			tb.append("<a action=\"bypass voiced_ranking\">Back to Rankings</a>");
			tb.append("</center>");
			tb.append("</body>");
			tb.append("</html>");
			htm.setHtml(tb.toString());
			activeChar.sendPacket(htm);
		} else if (command.equals("clan")) {
			NpcHtmlMessage htm = new NpcHtmlMessage(5);
			StringBuilder tb = new StringBuilder();
			tb.append("<html>");
			tb.append("<body>");
			tb.append("<center>");
			tb.append("<img src=\"l2ui_ch3.herotower_deco\" width=256 height=32><br>");
			tb.append("<table border=\"1\" width=\"300\">");
			tb.append("<tr>");
			tb.append("<td><center>Rank</center></td>");
			tb.append("<td><center>Clan</center></td>");
			tb.append("<td><center>Leader</center></td>");
			tb.append("<td><center>Kill Boss</center></td>");
			tb.append("</tr>");
			try (Connection con = L2DatabaseFactory.getInstance().getConnection()) {
				PreparedStatement statement = con.prepareStatement("SELECT clan_id,clan_name,ally_name,boss_win FROM clan_data WHERE boss_win>0 order by boss_win desc limit 15");
				ResultSet result = statement.executeQuery();
				int pos = 0;
				String leader_name = "N/A";
				while (result.next()) {
					int clan_id = result.getInt("clan_id");
					String clan_name = result.getString("clan_name");
					if (clan_name.equals("WWWWWWWWWWWWWWWW") || clan_name.equals("WWWWWWWWWWWWWWW") || clan_name.equals("WWWWWWWWWWWWWW") || clan_name.equals("WWWWWWWWWWWWW") || clan_name.equals("WWWWWWWWWWWW") || clan_name.equals("WWWWWWWWWWW") || clan_name.equals("WWWWWWWWWW") || clan_name.equals("WWWWWWWWW") || clan_name.equals("WWWWWWWW") || clan_name.equals("WWWWWWW") || clan_name.equals("WWWWWW")) {
						clan_name = clan_name.substring(0, 3) + "..";
					} else if (clan_name.length() > 14) {
						clan_name = clan_name.substring(0, 14) + "..";
					} 
					String ally_name = result.getString("ally_name");
					if (ally_name == null)
						ally_name = "N/A"; 
					if (ally_name.equals("WWWWWWWWWWWWWWWW") || ally_name.equals("WWWWWWWWWWWWWWW") || ally_name.equals("WWWWWWWWWWWWWW") || ally_name.equals("WWWWWWWWWWWWW") || ally_name.equals("WWWWWWWWWWWW") || ally_name.equals("WWWWWWWWWWW") || ally_name.equals("WWWWWWWWWW") || ally_name.equals("WWWWWWWWW") || ally_name.equals("WWWWWWWW") || ally_name.equals("WWWWWWW") || ally_name.equals("WWWWWW")) {
						ally_name = ally_name.substring(0, 3) + "..";
					} else if (ally_name.length() > 6) {
						ally_name = ally_name.substring(0, 6) + "..";
					} 
					Clan owner = ClanTable.getInstance().getClan(clan_id);
					if (owner != null)
						leader_name = owner.getLeaderName(); 
					if (leader_name.equals("WWWWWWWWWWWWWWWW") || leader_name.equals("WWWWWWWWWWWWWWW") || leader_name.equals("WWWWWWWWWWWWWW") || leader_name.equals("WWWWWWWWWWWWW") || leader_name.equals("WWWWWWWWWWWW") || leader_name.equals("WWWWWWWWWWW") || leader_name.equals("WWWWWWWWWW") || leader_name.equals("WWWWWWWWW") || leader_name.equals("WWWWWWWW") || leader_name.equals("WWWWWWW") || leader_name.equals("WWWWWW")) {
						leader_name = leader_name.substring(0, 3) + "..";
					} else if (leader_name.length() > 10) {
						leader_name = leader_name.substring(0, 10) + "..";
					} 
					String win = result.getString("boss_win");
					pos++;
					tb.append("<tr><td><center>" + pos + "</center></td><td><center>" + clan_name + "</center></td><td><center>" + leader_name + "</center></td><td><center>" + win + "</center></td></tr>");
				} 
				statement.close();
				result.close();
			} catch (Exception exception) {}
			tb.append("</table>");
			tb.append("<br>");
			tb.append("<br>");
			tb.append("<a action=\"bypass voiced_ranking\">Back to Rankings</a>");
			tb.append("</center>");
			tb.append("</body>");
			tb.append("</html>");
			htm.setHtml(tb.toString());
			activeChar.sendPacket(htm);
		} else if (command.equals("5x5")) {
			NpcHtmlMessage htm = new NpcHtmlMessage(5);
			StringBuilder tb = new StringBuilder();
			tb.append("<html>");
			tb.append("<body>");
			tb.append("<center>");
			tb.append("<img src=\"l2ui_ch3.herotower_deco\" width=256 height=32><br>");
			tb.append("<table border=\"1\" width=\"300\">");
			tb.append("<tr>");
			tb.append("<td><center>Rank</center></td>");
			tb.append("<td><center>Clan</center></td>");
			tb.append("<td><center>Leader</center></td>");
			tb.append("<td><center>5x5 Win</center></td>");
			tb.append("</tr>");
			try (Connection con = L2DatabaseFactory.getInstance().getConnection()) {
				PreparedStatement statement = con.prepareStatement("SELECT clan_id,clan_name,ally_name,5x5_win FROM clan_data WHERE 5x5_win>0 order by 5x5_win desc limit 15");
				ResultSet result = statement.executeQuery();
				int pos = 0;
				String leader_name = "N/A";
				while (result.next()) {
					int clan_id = result.getInt("clan_id");
					String clan_name = result.getString("clan_name");
					if (clan_name.equals("WWWWWWWWWWWWWWWW") || clan_name.equals("WWWWWWWWWWWWWWW") || clan_name.equals("WWWWWWWWWWWWWW") || clan_name.equals("WWWWWWWWWWWWW") || clan_name.equals("WWWWWWWWWWWW") || clan_name.equals("WWWWWWWWWWW") || clan_name.equals("WWWWWWWWWW") || clan_name.equals("WWWWWWWWW") || clan_name.equals("WWWWWWWW") || clan_name.equals("WWWWWWW") || clan_name.equals("WWWWWW")) {
						clan_name = clan_name.substring(0, 3) + "..";
					} else if (clan_name.length() > 14) {
						clan_name = clan_name.substring(0, 14) + "..";
					} 
					String ally_name = result.getString("ally_name");
					if (ally_name == null)
						ally_name = "N/A"; 
					if (ally_name.equals("WWWWWWWWWWWWWWWW") || ally_name.equals("WWWWWWWWWWWWWWW") || ally_name.equals("WWWWWWWWWWWWWW") || ally_name.equals("WWWWWWWWWWWWW") || ally_name.equals("WWWWWWWWWWWW") || ally_name.equals("WWWWWWWWWWW") || ally_name.equals("WWWWWWWWWW") || ally_name.equals("WWWWWWWWW") || ally_name.equals("WWWWWWWW") || ally_name.equals("WWWWWWW") || ally_name.equals("WWWWWW")) {
						ally_name = ally_name.substring(0, 3) + "..";
					} else if (ally_name.length() > 6) {
						ally_name = ally_name.substring(0, 6) + "..";
					} 
					Clan owner = ClanTable.getInstance().getClan(clan_id);
					if (owner != null)
						leader_name = owner.getLeaderName(); 
					if (leader_name.equals("WWWWWWWWWWWWWWWW") || leader_name.equals("WWWWWWWWWWWWWWW") || leader_name.equals("WWWWWWWWWWWWWW") || leader_name.equals("WWWWWWWWWWWWW") || leader_name.equals("WWWWWWWWWWWW") || leader_name.equals("WWWWWWWWWWW") || leader_name.equals("WWWWWWWWWW") || leader_name.equals("WWWWWWWWW") || leader_name.equals("WWWWWWWW") || leader_name.equals("WWWWWWW") || leader_name.equals("WWWWWW")) {
						leader_name = leader_name.substring(0, 3) + "..";
					} else if (leader_name.length() > 10) {
						leader_name = leader_name.substring(0, 10) + "..";
					} 
					String win = result.getString("5x5_win");
					pos++;
					tb.append("<tr><td><center>" + pos + "</center></td><td><center>" + clan_name + "</center></td><td><center>" + leader_name + "</center></td><td><center>" + win + "</center></td></tr>");
				} 
				statement.close();
				result.close();
			} catch (Exception exception) {}
			tb.append("</table>");
			tb.append("<br>");
			tb.append("<br>");
			tb.append("<a action=\"bypass voiced_ranking\">Back to Rankings</a>");
			tb.append("</center>");
			tb.append("</body>");
			tb.append("</html>");
			htm.setHtml(tb.toString());
			activeChar.sendPacket(htm);
		} else if (command.equals("9x9")) {
			NpcHtmlMessage htm = new NpcHtmlMessage(5);
			StringBuilder tb = new StringBuilder();
			tb.append("<html>");
			tb.append("<body>");
			tb.append("<center>");
			tb.append("<img src=\"l2ui_ch3.herotower_deco\" width=256 height=32><br>");
			tb.append("<table border=\"1\" width=\"300\">");
			tb.append("<tr>");
			tb.append("<td><center>Rank</center></td>");
			tb.append("<td><center>Clan</center></td>");
			tb.append("<td><center>Leader</center></td>");
			tb.append("<td><center>9x9 Win</center></td>");
			tb.append("</tr>");
			try (Connection con = L2DatabaseFactory.getInstance().getConnection()) {
				PreparedStatement statement = con.prepareStatement("SELECT clan_id,clan_name,ally_name,9x9_win FROM clan_data WHERE 9x9_win>0 order by 9x9_win desc limit 15");
				ResultSet result = statement.executeQuery();
				int pos = 0;
				String leader_name = "N/A";
				while (result.next()) {
					int clan_id = result.getInt("clan_id");
					String clan_name = result.getString("clan_name");
					if (clan_name.equals("WWWWWWWWWWWWWWWW") || clan_name.equals("WWWWWWWWWWWWWWW") || clan_name.equals("WWWWWWWWWWWWWW") || clan_name.equals("WWWWWWWWWWWWW") || clan_name.equals("WWWWWWWWWWWW") || clan_name.equals("WWWWWWWWWWW") || clan_name.equals("WWWWWWWWWW") || clan_name.equals("WWWWWWWWW") || clan_name.equals("WWWWWWWW") || clan_name.equals("WWWWWWW") || clan_name.equals("WWWWWW")) {
						clan_name = clan_name.substring(0, 3) + "..";
					} else if (clan_name.length() > 14) {
						clan_name = clan_name.substring(0, 14) + "..";
					} 
					String ally_name = result.getString("ally_name");
					if (ally_name == null)
						ally_name = "N/A"; 
					if (ally_name.equals("WWWWWWWWWWWWWWWW") || ally_name.equals("WWWWWWWWWWWWWWW") || ally_name.equals("WWWWWWWWWWWWWW") || ally_name.equals("WWWWWWWWWWWWW") || ally_name.equals("WWWWWWWWWWWW") || ally_name.equals("WWWWWWWWWWW") || ally_name.equals("WWWWWWWWWW") || ally_name.equals("WWWWWWWWW") || ally_name.equals("WWWWWWWW") || ally_name.equals("WWWWWWW") || ally_name.equals("WWWWWW")) {
						ally_name = ally_name.substring(0, 3) + "..";
					} else if (ally_name.length() > 6) {
						ally_name = ally_name.substring(0, 6) + "..";
					} 
					Clan owner = ClanTable.getInstance().getClan(clan_id);
					if (owner != null)
						leader_name = owner.getLeaderName(); 
					if (leader_name.equals("WWWWWWWWWWWWWWWW") || leader_name.equals("WWWWWWWWWWWWWWW") || leader_name.equals("WWWWWWWWWWWWWW") || leader_name.equals("WWWWWWWWWWWWW") || leader_name.equals("WWWWWWWWWWWW") || leader_name.equals("WWWWWWWWWWW") || leader_name.equals("WWWWWWWWWW") || leader_name.equals("WWWWWWWWW") || leader_name.equals("WWWWWWWW") || leader_name.equals("WWWWWWW") || leader_name.equals("WWWWWW")) {
						leader_name = leader_name.substring(0, 3) + "..";
					} else if (leader_name.length() > 10) {
						leader_name = leader_name.substring(0, 10) + "..";
					} 
					String win = result.getString("9x9_win");
					pos++;
					tb.append("<tr><td><center>" + pos + "</center></td><td><center>" + clan_name + "</center></td><td><center>" + leader_name + "</center></td><td><center>" + win + "</center></td></tr>");
				} 
				statement.close();
				result.close();
			} catch (Exception exception) {}
			tb.append("</table>");
			tb.append("<br>");
			tb.append("<br>");
			tb.append("<a action=\"bypass voiced_ranking\">Back to Rankings</a>");
			tb.append("</center>");
			tb.append("</body>");
			tb.append("</html>");
			htm.setHtml(tb.toString());
			activeChar.sendPacket(htm);
		}
		return true;
	}
	
	private static void showRankingHtml(Player activeChar) {
		NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile("data/html/mods/menu/Ranking.htm");
		activeChar.sendPacket(html);
	}
	
	@Override
	public String[] getVoicedCommandList() {
		return VOICED_COMMANDS;
	}
}
