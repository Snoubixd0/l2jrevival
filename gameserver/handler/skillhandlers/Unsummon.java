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
package com.l2jrevival.gameserver.handler.skillhandlers;

import com.l2jrevival.gameserver.handler.ISkillHandler;
import com.l2jrevival.gameserver.model.WorldObject;
import com.l2jrevival.gameserver.model.L2Skill;
import com.l2jrevival.gameserver.model.actor.Creature;
import com.l2jrevival.gameserver.model.actor.instance.Player;
import com.l2jrevival.gameserver.templates.skills.L2SkillType;

/**
 * @author Pawel
 */
public class Unsummon implements ISkillHandler
{
	private static final L2SkillType[] SKILL_IDS =
	{
		L2SkillType.UNSUMMON
	};
	
	@Override
	public void useSkill(Creature activeChar, L2Skill skill, WorldObject[] targets)
	{
		
		if (activeChar != null || (activeChar instanceof Player))
		{
			
			Player activePlayer = (Player) activeChar;
			if (activePlayer != null)
			{
				if (activePlayer.getAgathion() != null)
				{
					activePlayer.getAgathion().unSummon(activePlayer);
				}
				else
				{
					activePlayer.sendMessage("You have not any summoned agathion now.");
				}
			}
		}
		
	}
			
	@Override
	public L2SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}