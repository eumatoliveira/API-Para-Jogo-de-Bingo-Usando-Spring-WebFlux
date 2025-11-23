package com.bingo.player;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Player> save(@RequestBody Player player) {
        return playerService.save(player);
    }

    @PutMapping("/{id}")
    public Mono<Player> update(@PathVariable String id, @RequestBody Player player) {
        return playerService.update(id, player);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable String id) {
        return playerService.delete(id);
    }

    @GetMapping("/{id}")
    public Mono<Player> findById(@PathVariable String id) {
        return playerService.findById(id);
    }

    @GetMapping
    public Flux<Player> findAll() {
        return playerService.findAll();
    }
}
