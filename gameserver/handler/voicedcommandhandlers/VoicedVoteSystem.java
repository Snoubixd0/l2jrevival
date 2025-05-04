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
package com.l2jrevival.gameserver.handler.voicedcommandhandlers;

import com.l2jrevival.gameserver.handler.IVoicedCommandHandler;
import com.l2jrevival.gameserver.model.actor.instance.Player;
import com.l2jrevival.gameserver.network.serverpackets.NpcHtmlMessage;

import com.l2jrevival.gameserver.VoteSystem.HopzoneVoteReward;
import com.l2jrevival.gameserver.VoteSystem.NetworkVoteReward;
import com.l2jrevival.gameserver.VoteSystem.TopzoneVoteReward;
import com.l2jrevival.gameserver.VoteSystem.VoteSite;

/**
 * @author User
 *
 */
public class VoicedVoteSystem implements IVoicedCommandHandler
{
	private static final String[] VOICED_COMMANDS = 
	{
		"votehop","votenet","votetop","votesystem",
	};
	@Override
	public boolean useVoicedCommand(String command, Player activeChar, String target)
	{
		if (command.startsWith("votehop"))
		{
			if(activeChar.isEligibleToVote(VoteSite.HOPZONE))
			{
				HopzoneVoteReward rewardSite = new HopzoneVoteReward();
				rewardSite.checkVoteReward(activeChar);
				
			}
			activeChar.sendMessage("Available in " + activeChar.getVoteCountdown(VoteSite.HOPZONE));
		}
		
		else if (command.startsWith("votenet"))
		{
			if(activeChar.isEligibleToVote(VoteSite.NETWORK)){
				NetworkVoteReward rewardSite = new NetworkVoteReward();
				rewardSite.checkVoteReward(activeChar);
			}
			activeChar.sendMessage("Available in " + activeChar.getVoteCountdown(VoteSite.NETWORK));
		}
		
		else if (command.startsWith("votetop"))
		{
			if(activeChar.isEligibleToVote(VoteSite.TOPZONE)){
				TopzoneVoteReward rewardSite = new TopzoneVoteReward();
				rewardSite.checkVoteReward(activeChar);
			}
			activeChar.sendMessage("Available in " + activeChar.getVoteCountdown(VoteSite.TOPZONE));
		}
		
		else if (command.startsWith("votesystem"))
		{
			VoteSystemHtml(activeChar);
		}
		
		return true;
	}
	
	private static void VoteSystemHtml(Player activeChar)
	{
		String htmFile = "data/html/mods/menu/VoteSystem.htm";

		NpcHtmlMessage msg = new NpcHtmlMessage(5);
		msg.setFile(htmFile);
		activeChar.sendPacket(msg);
	}
	
	
	@Override
	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}
}
