package com.l2jrevival.gameserver.VoteSystem;

import com.l2jrevival.Config;
import com.l2jrevival.gameserver.model.actor.instance.Player;

@VoteSiteInfo(voteSite = VoteSite.NETWORK, apiKey = "NetworkApiKey")
public class NetworkVoteReward extends VoteRewardSite {

    @Override
    protected String getEndpoint(Player player) {
    	return String.format(Config.VOTE_NETWORK, player.getIpAddress());
    }

    @Override
    protected boolean hasVoted(Player player) {
        String serverResponse = VoteApiService.getApiResponse(getEndpoint(player));

        if(serverResponse.length() == 0){
            player.sendMessage("Algo deu errado, Relate para o administrador.");
            return false;
        }

        try{
            return Integer.parseInt(serverResponse.trim()) == 1;
        }catch (Exception e){
        	player.sendMessage("Algo deu errado, Relate para o administrador.");
            return false;
        }
    }
}