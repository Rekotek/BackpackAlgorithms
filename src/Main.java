import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.joining;

public class Main {
    private record Pack(int weight, int price) {
    }

    public static void main(String[] args) {
        int[] weights = {6, 80, 4, 7, 9, 2, 1};
        int[] prices = {35, 100, 5, 30, 20, 70, 40};
        int maxWeight = 80;

        List<Integer> dynamicList = calculateDynamic(weights, prices, maxWeight);
        printOut("Dynamic", dynamicList, weights, prices);

        List<Integer> indexList = calculateBruteForce(weights, prices, maxWeight);
        printOut("Bruteforce", indexList, weights, prices);
        List<Integer> greedyList = calculateGreedy(weights, prices, maxWeight);
        printOut("Greedy", greedyList, weights, prices);

        int[] otherWeights = {10, 11, 34, 5, 4};
        int[] otherPrice = {20, 11, 8, 15, 1};
        maxWeight = 21;
        List<Integer> greedy2List = calculateGreedy(otherWeights, otherPrice, maxWeight);
        printOut("Greedy2", greedy2List, otherWeights, otherPrice);

        dynamicList = calculateDynamic(otherWeights, otherPrice, maxWeight);
        printOut("Dynamic2", dynamicList, otherWeights, otherPrice);
    }

    private static List<Integer> calculateDynamic(int[] weights, int[] prices, int maxWeight) {
        int count = weights.length;
        int[][] values = new int[count + 1][];
        for (int i = 0; i <= count; i++) {
            values[i] = new int[maxWeight + 1];
        }
        for (int k = 0; k <= count; k++) {
            for (int w = 0; w <= maxWeight; w++) {
                if (k == 0 || w == 0) {
                    values[k][w] = 0;
                } else {
                    if (w < weights[k - 1]) {
                        values[k][w] = values[k - 1][w];
                    } else {
                        values[k][w] = Math.max(values[k - 1][w], values[k - 1][w - weights[k - 1]] + prices[k - 1]);
                    }
                }
            }
        }
        System.out.println();
        for (int i = 0; i < values.length; i++) {
            System.out.printf("%d: ", i);
            Arrays.stream(values[i]).forEach(n -> System.out.printf("%d ", n));
            System.out.println();
        }
        var resultList = new ArrayList<Integer>(count);
        traceResult(values, weights, count, maxWeight, resultList);
        return resultList;
    }

    private static void traceResult(final int[][] values, final int[] weights, int k, int w, ArrayList<Integer> resultList) {
        if (values[k][w] == 0) {
            return;
        }
        if (values[k][w] == values[k - 1][w]) {
            traceResult(values, weights, k - 1, w, resultList);
        } else {
            resultList.add(k - 1);
            int newWeightIndex = w - weights[k - 1];
            if (newWeightIndex == 0) {
                return;
            }
            traceResult(values, weights, k - 1, newWeightIndex, resultList);
        }
    }


    private static List<Integer> calculateGreedy(int[] weights, int[] prices, int maxWeight) {
        int count = weights.length;
        List<Integer> indexes = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            indexes.add(i);
        }
        List<Integer> result = new ArrayList<>(count);
        int resultWeight = 0;

        while (!indexes.isEmpty()) {
            int indexToDelete = 0;
            int currentMaxIndex = indexes.get(0);
            int currentMaxPrice = prices[currentMaxIndex];
            for (int i = 1; i < indexes.size(); i++) {
                var iterIndex = indexes.get(i);
                if (currentMaxPrice < prices[iterIndex]) {
                    currentMaxPrice = prices[iterIndex];
                    currentMaxIndex = iterIndex;
                    indexToDelete = i;
                }
            }

            resultWeight += weights[currentMaxIndex];
            if (resultWeight > maxWeight) {
                break;
            }

            result.add(currentMaxIndex);
            indexes.remove(indexToDelete);
        }

        return result.stream().sorted().toList();
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

    private static void printOut(String title, List<Integer> indexList, int[] weights, int[] prices) {
        System.out.printf(title + " result = {%s}\n", indexList.stream().map(String::valueOf).collect(joining(", ")));

        int overallWeight = indexList.stream().map(i -> weights[i]).reduce(0, Integer::sum);
        int overallPrice = indexList.stream().map(i -> prices[i]).reduce(0, Integer::sum);
        System.out.printf("\tPacked weight: %d\n", overallWeight);
        System.out.printf("\tPacked price: %d\n", overallPrice);
    }
}