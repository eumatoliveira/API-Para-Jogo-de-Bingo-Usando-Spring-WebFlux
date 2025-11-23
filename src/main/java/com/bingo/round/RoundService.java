package com.bingo.round;

import com.bingo.player.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoundService {

    private final RoundRepository roundRepository;
    private final PlayerRepository playerRepository;
    private final JavaMailSender mailSender;

    public Mono<Round> createRound() {
        return roundRepository.save(new Round());
    }

    public Mono<BingoCard> generateCard(String roundId, String playerId) {
        return roundRepository.findById(roundId)
                .flatMap(round -> {
                    if (!round.getDrawnNumbers().isEmpty()) {
                        return Mono.error(new IllegalStateException("Cannot generate cards after game started"));
                    }
                    if (round.getPlayers().contains(playerId)) {
                        return Mono.error(new IllegalStateException("Player already has a card in this round"));
                    }

                    Set<Integer> numbers = generateRandomNumbers(round.getCards());
                    BingoCard card = new BingoCard(playerId, numbers);
                    
                    round.getCards().add(card);
                    round.getPlayers().add(playerId);
                    
                    return roundRepository.save(round).thenReturn(card);
                });
    }

    private Set<Integer> generateRandomNumbers(List<BingoCard> existingCards) {
        Random random = new Random();
        Set<Integer> numbers;
        int attempts = 0;
        do {
            numbers = new HashSet<>();
            while (numbers.size() < 20) {
                numbers.add(random.nextInt(100)); // 0-99
            }
            attempts++;
            if (attempts > 1000) {
                 throw new RuntimeException("Could not generate a valid card after 1000 attempts");
            }
        } while (!isValidCard(numbers, existingCards));
        return numbers;
    }

    private boolean isValidCard(Set<Integer> newNumbers, List<BingoCard> existingCards) {
        for (BingoCard card : existingCards) {
            Set<Integer> intersection = new HashSet<>(newNumbers);
            intersection.retainAll(card.getNumbers());
            // Max 1/4 overlap (20 * 1/4 = 5)
            if (intersection.size() > 5) {
                return false;
            }
        }
        return true;
    }

    public Mono<Integer> drawNumber(String roundId) {
        return roundRepository.findById(roundId)
                .flatMap(round -> {
                    if (round.isFinished()) {
                        return Mono.error(new IllegalStateException("Round is already finished"));
                    }

                    int number;
                    Random random = new Random();
                    do {
                        number = random.nextInt(100);
                    } while (round.getDrawnNumbers().contains(number));

                    round.getDrawnNumbers().add(number);
                    
                    // Check for winner
                    String winnerId = checkWinner(round);
                    if (winnerId != null) {
                        round.setFinished(true);
                        round.setWinnerId(winnerId);
                        notifyWinnerAndLosers(round, winnerId);
                    }

                    return roundRepository.save(round).thenReturn(number);
                });
    }

    private String checkWinner(Round round) {
        Set<Integer> drawn = new HashSet<>(round.getDrawnNumbers());
        for (BingoCard card : round.getCards()) {
            if (drawn.containsAll(card.getNumbers())) {
                return card.getPlayerId();
            }
        }
        return null;
    }

    private void notifyWinnerAndLosers(Round round, String winnerId) {
        // In a real reactive app, this should be non-blocking or offloaded.
        // For simplicity, we just fire and forget or block slightly (Mock mail sender is fast).
        
        playerRepository.findById(winnerId).subscribe(winner -> {
            sendEmail(winner.getEmail(), "You Won!", "Congratulations, you won the Bingo round!");
        });

        round.getPlayers().stream()
                .filter(pid -> !pid.equals(winnerId))
                .forEach(loserId -> {
                    playerRepository.findById(loserId).subscribe(loser -> {
                        sendEmail(loser.getEmail(), "Game Over", "Someone else won the Bingo round. Better luck next time!");
                    });
                });
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        try {
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send email to " + to + ": " + e.getMessage());
        }
    }

    public Mono<Integer> getLastNumber(String roundId) {
        return roundRepository.findById(roundId)
                .map(round -> {
                    List<Integer> drawn = round.getDrawnNumbers();
                    if (drawn.isEmpty()) return -1;
                    return drawn.get(drawn.size() - 1);
                });
    }

    public Flux<Round> findAll() {
        return roundRepository.findAll();
    }

    public Mono<Round> findById(String id) {
        return roundRepository.findById(id);
    }
}
