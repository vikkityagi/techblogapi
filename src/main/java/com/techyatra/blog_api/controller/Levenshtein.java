package com.techyatra.blog_api.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/words")
public class Levenshtein {

    private final List<String> dictionary = Arrays.asList(
            "angular", "anger", "anchor", "angry", "animal", "application");

    @GetMapping("/suggest")
    public String suggestWord(@RequestParam String input) {
        return findBestMatch(input);
    }

    private String findBestMatch(String input) {
        String suggestion = null;
        int minDistance = Integer.MAX_VALUE;

        for (String word : dictionary) {
            int distance = getLevenshteinDistance(input.toLowerCase(), word.toLowerCase());
            if (distance < minDistance) {
                minDistance = distance;
                suggestion = word;
            }
        }

        return suggestion;
    }

    private int getLevenshteinDistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];

        for (int i = 0; i <= a.length(); i++) {
            for (int j = 0; j <= b.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = Math.min(
                            Math.min(
                                    dp[i - 1][j] + 1,
                                    dp[i][j - 1] + 1),
                            dp[i - 1][j - 1] + (a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1));
                }
            }
        }

        return dp[a.length()][b.length()];
    }

}
