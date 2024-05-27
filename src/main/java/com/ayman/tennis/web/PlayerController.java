package com.ayman.tennis.web;

import com.ayman.tennis.Player;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Tag(name = "Tennis Players API")
@RestController
@RequestMapping("/players")
public class PlayerController {

    @Operation(summary = "Finds Players", description = "Find players")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Players list",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Player.class)))})
    })
    @GetMapping
    public List<Player> list(){
        return Collections.emptyList();
    }

    @Operation(summary = "Finds a player", description = "Finds a player")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Player.class)))})
    })
    @GetMapping("{lastName}")
    public Player getByLastName(@PathVariable("lastName") String lastName){
        return null;
    }

    @Operation(summary = "Creates a player", description = "Creates a player")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created player",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Player.class)))})
    })
    @PostMapping
    public Player createPlayer(@RequestBody Player player){
        return player;
    }

    @Operation(summary = "Updates a player", description = "Updates a player")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated player",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Player.class)))})
    })
    @PutMapping
    public Player updatePlayer(@RequestBody Player player){
        return player;
    }

    @Operation(summary = "Deletes a player", description = "Deletes a player")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Player has been deleted")
    })
    @DeleteMapping("{lastName}")
    public void deletePlayerByLastName(@PathParam("lastName") String lastName){
    }

}
