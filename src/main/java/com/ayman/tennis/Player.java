package com.ayman.tennis;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record Player(
        @NotBlank(message = "First name is mandatory")
        String firstName,
        @NotBlank(message = "Last name is mandatory")
        String lastName,
        @NotNull(message = "Birth date is mandatory")
        @PastOrPresent
        LocalDate birthDate,
        @Valid
        Rank rank
) {
}
