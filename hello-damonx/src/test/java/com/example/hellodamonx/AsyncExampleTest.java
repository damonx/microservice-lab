package com.example.hellodamonx;

import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static org.assertj.core.api.Assertions.assertThat;

public class AsyncExampleTest {

    @Test
    void testAsyncProcessing() {
        try (final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            final List<String> requests = List.of("A", "B", "C");

            final List<CompletableFuture<String>> futures = requests.stream()
                .map(req -> CompletableFuture.supplyAsync(() -> callApi(req), executor)
                    .exceptionally(ex -> "Error for " + req + ": " + ex.getMessage()))
                .toList();

            final CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
            allFutures.join();  // Wait for all to complete

            final List<String> results = futures.stream()
                .map(CompletableFuture::join)
                .toList();

            assertThat(results).containsExactly("Result-A", "Result-B", "Result-C");
        }
    }

    // Helper method to simulate callApi with shorter delay for testing
    private static String callApi(String req) {
        try {
            final Duration delay = Duration.ofMillis(10);
            Thread.sleep(delay.toMillis());  // Shorter delay for unit test
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Interrupted-" + req;
        }
        return "Result-" + req;
    }
}
