package com.ayman.tennis.service;

import com.ayman.tennis.Player;
import com.ayman.tennis.PlayerList;
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
}
