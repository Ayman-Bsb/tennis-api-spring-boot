package com.ayman.tennis.service;

import com.ayman.tennis.Player;
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

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

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
        Optional<PlayerEntity> optionalPlayerEntity = playerRepository.findOneByLastNameIgnoreCase(playerToSave.lastName());

        if(optionalPlayerEntity.isPresent()){
            throw new PlayerAlreadyExistsException(playerToSave.lastName());
        }

        PlayerEntity playerToRegister = new PlayerEntity(
                playerToSave.lastName(),
                playerToSave.firstName(),
                playerToSave.birthDate(),
                999999999,
                playerToSave.points()
        );

        playerRepository.save(playerToRegister);

        List<PlayerEntity> newRanking = new RankingCalculator(playerRepository.findAll()).getNewPlayersRanking();

        playerRepository.saveAll(newRanking);

        return getByLastName(playerToSave.lastName());
    }

    public Player update(PlayerToSave playerToSave) {
        Optional<PlayerEntity> playerToUpdate = playerRepository.findOneByLastNameIgnoreCase(playerToSave.lastName());

        if(playerToUpdate.isEmpty()){
            throw new PlayerNotFoundException(playerToSave.lastName());
        }

        playerToUpdate.get().setFirstName(playerToSave.firstName());
        playerToUpdate.get().setBirthDate(playerToSave.birthDate());

        if(playerToUpdate.get().getPoints() != playerToSave.points()){
            playerToUpdate.get().setPoints(playerToSave.points());
            playerRepository.save(playerToUpdate.get());

            List<PlayerEntity> newRanking = new RankingCalculator(playerRepository.findAll()).getNewPlayersRanking();
            playerRepository.saveAll(newRanking);
        }
        else {
            playerRepository.save(playerToUpdate.get());
        }

        return getByLastName(playerToSave.lastName());
    }

    public void delete(String lastName) {
        Optional<PlayerEntity> playerToDelete = playerRepository.findOneByLastNameIgnoreCase(lastName);

        if(playerToDelete.isEmpty()){
            throw new PlayerNotFoundException(lastName);
        }

        playerRepository.delete(playerToDelete.get());

        List<PlayerEntity> newRanking = new RankingCalculator(playerRepository.findAll()).getNewPlayersRanking();
        playerRepository.saveAll(newRanking);
    }

}
