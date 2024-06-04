package com.ayman.tennis.service;

import com.ayman.tennis.Player;
import com.ayman.tennis.PlayerList;
import com.ayman.tennis.PlayerToSave;
import com.ayman.tennis.Rank;
import com.ayman.tennis.data.PlayerEntity;
import com.ayman.tennis.data.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    public List<Player> getAllPlayers(){
        return playerRepository.findAll().stream()
                // Transform the PlayerEntity to Player, as this is what is expected at the business layer
                .map(player -> new Player(
                        player.getFirstName(),
                        player.getLastName(),
                        player.getBirthDate(),
                        new Rank(player.getPosition(), player.getPoints())
                ))
                .sorted(Comparator.comparing(player -> player.rank().position()))
                .collect(Collectors.toList());
    }

    public Player getByLastName(String lastName){
        Optional<PlayerEntity> optionalPlayerEntity = playerRepository.findOneByLastNameIgnoreCase(lastName);

        if(optionalPlayerEntity.isEmpty()){
            throw new PlayerNotFoundException(lastName);
        }

        PlayerEntity playerEntity = optionalPlayerEntity.get();

        return new Player(
                playerEntity.getFirstName(),
                playerEntity.getLastName(),
                playerEntity.getBirthDate(),
                new Rank(playerEntity.getPosition(), playerEntity.getPoints())
        );
    }

    public Player create(PlayerToSave playerToSave){
        return getPlayerNewRanking(PlayerList.All,playerToSave);
    }

    public Player update(PlayerToSave playerToSave) {
        getByLastName(playerToSave.lastName());

        List<Player> playersWithoutPlayerToUpdate = PlayerList.All.stream()
                .filter(player -> !player.lastName().equals(playerToSave.lastName()))
                .toList();

        return getPlayerNewRanking(playersWithoutPlayerToUpdate, playerToSave);
    }

    public void delete(String lastName) {
        getByLastName(lastName);

        PlayerList.All = PlayerList.All.stream()
                .filter(player -> !player.lastName().equals(lastName))
                .toList();

        RankingCalculator rankingCalculator = new RankingCalculator(PlayerList.All);
        rankingCalculator.getNewPlayersRanking();
    }

    private Player getPlayerNewRanking(List<Player> existingPlayers, PlayerToSave playerToSave) {
        RankingCalculator rankingCalculator = new RankingCalculator(existingPlayers, playerToSave);
        List<Player> players = rankingCalculator.getNewPlayersRanking();

        return players.stream()
                .filter(player -> player.lastName().equals(playerToSave.lastName()))
                .findFirst().get();
    }

}
