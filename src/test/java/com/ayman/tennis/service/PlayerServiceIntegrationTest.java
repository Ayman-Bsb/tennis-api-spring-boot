package com.ayman.tennis.service;

import com.ayman.tennis.Player;
import com.ayman.tennis.PlayerToSave;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // The application context, and thus the database state, will be reset between each method execution
public class PlayerServiceIntegrationTest {

    @Autowired
    private PlayerService playerService;

    @Test
    public void shouldCreatePlayer() {
        // Given
        PlayerToSave playerToSave = new PlayerToSave(
                "John",
                "DoeTest",
                LocalDate.of(2000, Month.JANUARY, 1),
                10000
        );

        // When
        playerService.create(playerToSave);
        Player createdPlayer = playerService.getByLastName("doeTest");

        // Then
        Assertions.assertThat(createdPlayer.firstName()).isEqualTo("John");
        Assertions.assertThat(createdPlayer.lastName()).isEqualTo("DoeTest");
        Assertions.assertThat(createdPlayer.birthDate()).isEqualTo(LocalDate.of(2000, Month.JANUARY, 1));
        Assertions.assertThat(createdPlayer.rank().points()).isEqualTo(10000);
        Assertions.assertThat(createdPlayer.rank().position()).isEqualTo(1);
    }

    @Test
    public void shouldFailToCreatePlayer_WhenPlayerAlreadyExists() {
        // Given
        PlayerToSave playerToSave = new PlayerToSave(
                "John",
                "NadalTest",
                LocalDate.of(2000, Month.JANUARY, 1),
                10000
        );

        // When / Then
        Exception exception = assertThrows(PlayerAlreadyExistsException.class, ()-> playerService.create(playerToSave));
        Assertions.assertThat(exception.getMessage()).isEqualTo("Player with the last name NadalTest already exists.");
    }

    @Test
    public void shouldUpdatePlayer() {
        // Given
        PlayerToSave playerToSave = new PlayerToSave(
                "Rafael",
                "NadalTest",
                LocalDate.of(1986, Month.JUNE, 3),
                1000
        );

        // When
        playerService.update(playerToSave);
        Player updatedPlayer = playerService.getByLastName("NadalTest");

        // Then
        Assertions.assertThat(updatedPlayer.rank().position()).isEqualTo(3);
    }

    @Test
    public void shouldFailToUpdatePlayer_WhenPlayerDoesNotExist() {
        // Given
        PlayerToSave playerToSave = new PlayerToSave(
                "Rafael",
                "DoeTest",
                LocalDate.of(1986, Month.JUNE, 3),
                1000
        );

        // When / Then
        Exception exception = assertThrows(PlayerNotFoundException.class, ()-> playerService.update(playerToSave));
        Assertions.assertThat(exception.getMessage()).isEqualTo("Player with the last name DoeTest could not be found.");
    }

    @Test
    public void shouldDeletePlayer(){
        // Given
        String playerToDelete = "DjokovicTest";

        // When
        playerService.delete(playerToDelete);
        List<Player> allPlayers = playerService.getAllPlayers();

        // Then

        // Assertions.assertThat(allPlayers)
        //         .extracting("lastName")
        //         .containsExactly("NadalTest", "FedererTest");

        Assertions.assertThat(allPlayers)
                .extracting("lastName", "rank.position")
                .containsExactly(Tuple.tuple("NadalTest", 1), Tuple.tuple("FedererTest", 2));
    }

    @Test
    public void shouldFailToDeletePlayer_WhenPlayerDoesNotExist() {
        // Given
        String playerToDelete = "DoeTest";

        // When / Then
        Exception exception = assertThrows(PlayerNotFoundException.class, ()-> playerService.delete(playerToDelete));
        Assertions.assertThat(exception.getMessage()).isEqualTo("Player with the last name DoeTest could not be found.");
    }


    }
