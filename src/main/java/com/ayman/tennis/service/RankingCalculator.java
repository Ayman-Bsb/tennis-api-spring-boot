package com.ayman.tennis.service;

import com.ayman.tennis.Player;
import com.ayman.tennis.PlayerToRegister;
import com.ayman.tennis.Rank;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RankingCalculator {
    private final List<Player> currentPlayersRanking;
    private final PlayerToRegister playerToRegister;

    public RankingCalculator(List<Player> currentPlayersRanking, PlayerToRegister playerToRegister) {
        this.currentPlayersRanking = currentPlayersRanking;
        this.playerToRegister = playerToRegister;
    }

    public List<Player> getNewPlayersRanking() {
        List<Player> newRankingList = new ArrayList<>(currentPlayersRanking);
        newRankingList.add(new Player(
                playerToRegister.firstName(),
                playerToRegister.lastName(),
                playerToRegister.birthDate(),
                new Rank(999999999, playerToRegister.points())
        ));

        List<Player> sortedPlayers = newRankingList.stream()
                .sorted(Comparator.comparing((Player player) -> player.rank().points()).reversed())
                .toList();

        List<Player> updatedPlayers = new ArrayList<>();

        for (int i = 0; i < sortedPlayers.size(); i++){
            Player player = sortedPlayers.get(i);
            Player updatedPlayer = new Player(
                    player.firstName(),
                    player.lastName(),
                    player.birthDate(),
                    new Rank(i + 1, player.rank().points())
            );
            updatedPlayers.add(updatedPlayer);
        }

        return updatedPlayers;
    }
}
