package com.bingo.round;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/rounds")
@RequiredArgsConstructor
public class RoundController {

    private final RoundService roundService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Round> createRound() {
        return roundService.createRound();
    }

    @PostMapping("/{id}/generate-number")
    public Mono<Integer> generateNumber(@PathVariable String id) {
        return roundService.drawNumber(id);
    }

    @GetMapping("/{id}/current-number")
    public Mono<Integer> getLastNumber(@PathVariable String id) {
        return roundService.getLastNumber(id);
    }

    @PostMapping("/{id}/bingo-card/{playerId}")
    public Mono<BingoCard> generateCard(@PathVariable String id, @PathVariable String playerId) {
        return roundService.generateCard(id, playerId);
    }

    @GetMapping
    public Flux<Round> findAll() {
        return roundService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Round> findById(@PathVariable String id) {
        return roundService.findById(id);
    }
}
