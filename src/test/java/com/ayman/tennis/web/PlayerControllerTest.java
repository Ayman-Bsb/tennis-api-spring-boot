package com.ayman.tennis.web;

import com.ayman.tennis.data.PlayerList;
import com.ayman.tennis.service.PlayerNotFoundException;
import com.ayman.tennis.service.PlayerService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PlayerController.class) // To instruct Spring to load a minimal application context sufficient for testing the web layer, without loading the application server, and limiting it to PlayerController.
public class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc; // To simulate HTTP requests

    @MockBean // Adds mock to Spring context
    private PlayerService playerService;

    @Test
    public void shouldListAllPlayers() throws Exception {
        // Given
        Mockito.when(playerService.getAllPlayers()).thenReturn(PlayerList.All);

        // When / Then
        mockMvc.perform(get("/players"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4))) // $ symbol is a reference to the root element of the JSON response
                .andExpect(jsonPath("$[0].lastName", CoreMatchers.is("Nadal")))
                .andExpect(jsonPath("$[1].lastName", CoreMatchers.is("Djokovic")))
                .andExpect(jsonPath("$[2].lastName", CoreMatchers.is("Federer")))
                .andExpect(jsonPath("$[3].lastName", CoreMatchers.is("Murray")));
    }

    @Test
    public void shouldRetrievePlayer() throws Exception {
        // Given
        String playerToRetrieve = "nadal";
        Mockito.when(playerService.getByLastName(playerToRetrieve)).thenReturn(PlayerList.RAFAEL_NADAL);

        // When / Then
        mockMvc.perform(get("/players/"+playerToRetrieve))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName", CoreMatchers.is("Nadal")))
                .andExpect(jsonPath("$.rank.position", CoreMatchers.is(1)));
    }

    @Test
    public void shouldReturn404NotFound_WhenPlayerDoesNotExist() throws Exception {
        // Given
        String playerToRetrieve = "doe";
        Mockito.when(playerService.getByLastName(playerToRetrieve)).thenThrow(new PlayerNotFoundException(playerToRetrieve));

        // When / Then
        mockMvc.perform(get("/players/"+playerToRetrieve))
                .andExpect(status().isNotFound());
    }

}
