package com.ayman.tennis.service;

import com.ayman.tennis.Player;
import com.ayman.tennis.PlayerList;
import com.ayman.tennis.PlayerToRegister;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerService {

    public List<Player> getAllPlayers(){
        return PlayerList.All.stream()
                .sorted(Comparator.comparing(player -> player.rank().position()))
                .collect(Collectors.toList());
    }

    public Player getByLastName(String lastName){
        return PlayerList.All.stream()
                .filter(player -> player.lastName().equals(lastName))
                .findFirst()
                .orElseThrow(() -> new PlayerNotFoundException(lastName));
    }

    public Player create(PlayerToRegister playerToRegister){
        RankingCalculator rankingCalculator = new RankingCalculator(PlayerList.All, playerToRegister);
        List<Player> players = rankingCalculator.getNewPlayersRanking();

        return players.stream()
                .filter(player -> player.lastName().equals(playerToRegister.lastName()))
                .findFirst().get();
    }
}
