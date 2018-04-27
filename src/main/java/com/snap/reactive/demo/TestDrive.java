package com.snap.reactive.demo;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class TestDrive {

    private static String reverse0(String word) {
        return new StringBuilder(word).reverse().toString();
    }

    private static void reverse1(String word, Consumer<String> ret) {
        ret.accept(new StringBuilder(word).reverse().toString());
    }

    private static CompletableFuture<String> reverse2(String word) {
        return CompletableFuture.completedFuture(new StringBuilder(word).reverse().toString());
    }

    private static CompletableFuture<Boolean> isPalindrome(String word) {
        return reverse2(word).thenApply(reversed -> reversed.equals(word));
    }

    private static CompletableFuture<Integer> add(int x, int y) {
        return CompletableFuture.completedFuture(x + y);
    }

    private static CompletableFuture<Integer> multiply(int x, int y) {

        return CompletableFuture.completedFuture(x * y);
    }

    private static CompletableFuture<Integer> square(int x) {
        return multiply(x, x);
    }

    private static CompletableFuture<Integer> pythagoras(int x, int y) {
        return square(x).thenCompose(squareOfx -> square(y).thenCompose(squareOfy -> add(squareOfx, squareOfy)));
    }

    private static CompletableFuture<String> getDayOfWeek1(int day) {
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday",
                "Friday", "Saturday", "Sunday"};

        CompletableFuture<String> promise = new CompletableFuture<>();
        if (day >= 1 && day <= 7) {
            promise.complete(days[day - 1]);
        } else {
            promise.completeExceptionally(new IllegalArgumentException("Invalid day of the week: " + day));
        }

        return promise;
    }


    public static void main(String[] args) throws Exception {
        System.out.println(reverse0("hello"));
        reverse1("hello", System.out::println);
        reverse2("hello").thenAccept(System.out::println);
        pythagoras(3, 4).thenAccept(System.out::println);
        getDayOfWeek1(0).handle((day, error) -> {
            if(error != null) {
                return "The day is unknown";
            }
            return day;
        }).thenAccept(System.out::println);

        System.out.println("End of Program");
    }
}
