package com.example.hellodamonx;

import java.util.*;
import java.util.concurrent.*;

public class AsyncExample {
    public static void main(String[] args) {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            final List<String> requests = List.of("A", "B", "C", "D", "E");
            final List<CompletableFuture<String>> futures = requests.stream()
                .map(req -> CompletableFuture.supplyAsync(() -> callApi(req), executor)
                    .exceptionally(ex -> "Error for " + req + ": " + ex.getMessage()))
                .toList();

            final CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
            allFutures.join();  // Wait for all to complete

            final List<String> results = futures.stream()
                .map(CompletableFuture::join)
                .toList();

            System.out.println(results);
            executor.shutdown();
        }
    }

    private static String callApi(String req) {
        try {
            Thread.sleep(1000); // 模拟调用
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Interrupted-" + req;
        }
        return "Result-" + req;
    }
}
