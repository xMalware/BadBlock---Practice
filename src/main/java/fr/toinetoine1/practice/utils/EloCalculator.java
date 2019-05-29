package fr.toinetoine1.practice.utils;

/*
    Created by Toinetoine1 on 15/05/2019
*/

public class EloCalculator {

    private static int K = 16;

    private static double calculateExpectedScore(int rating1, int rating2) {
        return 1 / (1.0 + Math.pow(10.0, (rating2 - rating1) / 4000.0));
    }

    public static int calculateRatingChange(int rating1, int rating2, double score) {
        return (int) Math.round(K * (score - calculateExpectedScore(rating1, rating2)));
    }

}
