package com.l2jrevival.gameserver.VoteSystem;


import com.l2jrevival.Config;
import com.l2jrevival.commons.concurrent.ThreadPool;
import com.l2jrevival.gameserver.data.ItemTable;
import com.l2jrevival.gameserver.model.World;
import com.l2jrevival.gameserver.model.actor.instance.Player;
import com.l2jrevival.gameserver.network.serverpackets.ActionFailed;
import com.l2jrevival.gameserver.network.serverpackets.PlaySound;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class VoteRewardSite {
	
	private static final Logger LOGGER = Logger.getLogger(VoteRewardSite.class.getName());
	
	protected abstract String getEndpoint(Player player);
	
	public void checkVoteReward(Player player){
		try
		{
			if(player.isVoting())
			{
				player.sendMessage("You are already voting.");
				player.sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}
			
			if(World.getInstance().getPlayers1().stream().filter(Player::isVoting).count() >= 5){
				player.sendMessage("Tente novamente em alguns segundos.");
				player.sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}
			
			player.setIsVoting(true);
			
			if(!player.isEligibleToVote(getVoteSiteInfo().voteSite()))
			{
				player.setIsVoting(false);
				player.sendMessage("Voce nao pode votar.");
				player.sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}
			
			ThreadPool.execute(() -> {
				
				try{
					
					
					long dateTimevoted = System.currentTimeMillis();
					
					VotedRecord votedRecord = new VotedRecord(player.getAccountName(), player.getIpAddress(), dateTimevoted, getVoteSiteInfo().voteSite().getName());
					
					VoteDao.addVotedRecord(votedRecord);
					player.setLastVotedTimestamp(getVoteSiteInfo().voteSite(), dateTimevoted);
					reward(player);
					player.setIsVoting(false);
				}catch (Exception e){
					handleExceptionForVoteAttempt(player, e);
				}
				
			});
			
		}catch (Exception e){
			handleExceptionForVoteAttempt(player, e);
		}
	}
	
	 protected abstract boolean hasVoted(Player player);
	
	protected void reward(Player player){
		player.sendMessage("Thanks for voting! Here's your reward.");
		
		if (player.isOnline())
		{
			for (int[] reward : Config.REWARD_VOTESYSTEM)
			{
				if (ItemTable.getInstance().createDummyItem(reward[0]).isStackable())
				{
					
					player.addItem("VoteReward", reward[0], reward[1], null, true);
				}
				else
				{
					for (int i = 0; i < reward[1]; ++i)
					{
						player.addItem("VoteReward", reward[0], 1, null, true);
					}
				}
			}
			player.broadcastPacket(new PlaySound("ItemSound.quest_finish"));
		}
	}
	
	private VoteSiteInfo getVoteSiteInfo() {
		return getClass().getAnnotation(VoteSiteInfo.class);
	}
	
	String getApiKey(){
		return getVoteSiteInfo().apiKey();
	}
	
	private static void handleExceptionForVoteAttempt(Player player, Exception e) {
		player.setIsVoting(false);
		player.sendPacket(ActionFailed.STATIC_PACKET);
		LOGGER.log(Level.WARNING, "There was an error during a vote attempt", e);
	}
	
	@Override
	public String toString() {
		return getClass().getAnnotation(VoteSiteInfo.class).voteSite().getName();
	}
}