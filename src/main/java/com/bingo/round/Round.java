package com.bingo.round;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "rounds")
public class Round {
    @Id
    private String id;
    private List<Integer> drawnNumbers = new ArrayList<>();
    private List<BingoCard> cards = new ArrayList<>();
    private Set<String> players = new HashSet<>();
    private boolean finished = false;
    private String winnerId;
}
