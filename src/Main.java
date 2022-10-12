import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;

public class Main {
    private record Pack(int weight, int price) {
    }

    public static void main(String[] args) {
        int[] weights = {6, 80, 4, 7, 9, 2, 1};
        int[] prices = {1, 100, 5, 3, 2, 70, 40};
        int maxWeight = 80;

        List<Integer> indexList = calculateBruteForce(weights, prices, maxWeight);
        System.out.printf("Bruteforce result = {%s}\n", indexList.stream().map(String::valueOf).collect(joining(", ")));
        int overallWeight = indexList.stream().map(i -> weights[i]).reduce(0, Integer::sum);
        int overallPrice = indexList.stream().map(i -> prices[i]).reduce(0, Integer::sum);
        System.out.printf("\tPacked weight: %d\n", overallWeight);
        System.out.printf("\tPacked price: %d\n", overallPrice);
    }

    private static List<Integer> calculateBruteForce(int[] weights, int[] prices, int maxWeight) {
        int count = weights.length;
        final long MAX_VARIANTS = 2L << count;
        System.out.printf("Overall variants = %d\n", MAX_VARIANTS);

        var currentMaxPrice = 0;
        var currentBestVariant = 0L;

        for (var variant = 0L; variant < MAX_VARIANTS; variant++) {
            Pack pack = calculatePackFor(variant, prices, weights);
            if ((pack.weight <= maxWeight) && (pack.price > currentMaxPrice)) {
                currentBestVariant = variant;
                currentMaxPrice = pack.price;
            }
        }

        List<Integer> result = new ArrayList<>(count);
        long index = 1L;
        for (int i = 0; i < count; i++) {
            if ((currentBestVariant & index) == index) {
                result.add(i);
            }
            index <<= 1;
        }
        return result;
    }

    private static Pack calculatePackFor(long variant, int[] prices, int[] weights) {
        long index = 1L;
        int price = 0;
        int weight = 0;

        for (int i = 0; i < prices.length; i++) {
            if ((variant & index) == index) {
                weight += weights[i];
                price += prices[i];
            }
            index <<= 1;
        }
        return new Pack(weight, price);
    }
}