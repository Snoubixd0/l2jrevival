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
package com.l2jrevival.gameserver.communitybbs.BB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;

import com.l2jrevival.Config;
import com.l2jrevival.L2DatabaseFactory;

/**
 * @author Juvenil Walker
 *
 */
public class GrandBossList
{
	protected static final Logger LOGGER = Logger.getLogger(GrandBossList.class.getName());
	
	private static final String SELECT_BOSS = "SELECT boss_id, status FROM grandboss_data";
	private static final String SELECT_NAME = "SELECT name FROM npc WHERE id=";
	private final StringBuilder _GrandBossList = new StringBuilder();
	
	public GrandBossList()
	{
		loadFromDB();
	}
	
	private void loadFromDB()
	{
		int pos = 0;
		
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement statement = con.prepareStatement(SELECT_BOSS);
			ResultSet result = statement.executeQuery();
			
			nextnpc: while (result.next())
			{
				int npcid = result.getInt("boss_id");
				int status = result.getInt("status");
				if ((npcid == 29028) || (npcid == 29019) || (npcid == 29022) || (npcid == 29001) || (npcid == 29020))
				{
					continue nextnpc;
				}
				
				PreparedStatement statement2 = con.prepareStatement(SELECT_NAME + npcid);
				ResultSet result2 = statement2.executeQuery();
				
				while (result2.next())
				{
					pos++;
					boolean rstatus = false;
					if (status == 0)
					{
						rstatus = true;
					}
					String npcname = result2.getString("name");
					addGrandBossToList(pos, npcname, rstatus);
				}
				result2.close();
				statement2.close();
			}
			
			result.close();
			statement.close();
		}
		catch (Exception e)
		{
			LOGGER.warning(GrandBossList.class.getName() + ": Error Loading DB ");
			if (Config.DEBUG)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void addGrandBossToList(int pos, String npcname, boolean rstatus)
	{
		_GrandBossList.append("<table width=630 border=0 cellspacing=0 cellpadding=2>");
		_GrandBossList.append("<tr>");
		_GrandBossList.append("<td FIXWIDTH=5></td>");
		_GrandBossList.append("<td FIXWIDTH=25>" + pos + "</td>");
		_GrandBossList.append("<td FIXWIDTH=270>" + npcname + "</td>");
		_GrandBossList.append("<td FIXWIDTH=50 align=center></td>");
		_GrandBossList.append("<td FIXWIDTH=120 align=center></td>");
		_GrandBossList.append("<td FIXWIDTH=50 align=center>" + ((rstatus) ? "<font color=99FF00>Alive</font>" : "<font color=CC0000>Dead</font>") + "</td>");
		_GrandBossList.append("<td FIXWIDTH=5></td>");
		_GrandBossList.append("</tr>");
		_GrandBossList.append("</table>");
		_GrandBossList.append("<img src=\"L2UI.Squaregray\" width=\"630\" height=\"1\">");
	}
	
	public String loadGrandBossList()
	{
		return _GrandBossList.toString();
	}
}
