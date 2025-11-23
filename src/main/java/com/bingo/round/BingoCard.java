package com.bingo.round;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BingoCard {
    private String playerId;
    private Set<Integer> numbers;
}
