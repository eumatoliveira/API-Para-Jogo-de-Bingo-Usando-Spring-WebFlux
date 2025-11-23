package com.bingo.player;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;

    public Mono<Player> save(Player player) {
        return playerRepository.save(player);
    }

    public Mono<Player> update(String id, Player player) {
        return playerRepository.findById(id)
                .flatMap(existingPlayer -> {
                    existingPlayer.setName(player.getName());
                    existingPlayer.setEmail(player.getEmail());
                    return playerRepository.save(existingPlayer);
                });
    }

    public Mono<Void> delete(String id) {
        return playerRepository.deleteById(id);
    }

    public Mono<Player> findById(String id) {
        return playerRepository.findById(id);
    }

    public Flux<Player> findAll() {
        return playerRepository.findAll();
    }
}
