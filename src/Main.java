import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.joining;

public class Main {
    public static void main(String[] args) {
        int[] weights = {6, 80, 4, 7, 9, 2, 1};
        int[] prices = {35, 100, 5, 30, 20, 70, 40};
        int maxWeight = 80;

        var dynamicAlgorithm = new DynamicAlgorithm(weights, prices);
        var dynamicList = dynamicAlgorithm.calculate(maxWeight);
        printOut("Dynamic", dynamicList, weights, prices);
        printOut("Dynamic for 13", dynamicAlgorithm.calculate(13), weights, prices);
        printOut("Dynamic for 11", dynamicAlgorithm.calculate(11), weights, prices);


        var bruteAlgorithm = new BruteForceAlgorithm(weights, prices);
        List<Integer> indexList = bruteAlgorithm.calculate(maxWeight);
        printOut("Bruteforce", indexList, weights, prices);
        printOut("Bruteforce for 11", bruteAlgorithm.calculate(11), weights, prices);

        List<Integer> greedyList = calculateGreedy(weights, prices, maxWeight);
        printOut("Greedy", greedyList, weights, prices);

        int[] otherWeights = {10, 11, 34, 5, 4};
        int[] otherPrice = {20, 11, 8, 15, 1};
        maxWeight = 21;
        List<Integer> greedy2List = calculateGreedy(otherWeights, otherPrice, maxWeight);
        printOut("Greedy2", greedy2List, otherWeights, otherPrice);

        var dynamicAlgorithm2 = new DynamicAlgorithm(otherWeights, otherPrice);
        var dynamicList2 = dynamicAlgorithm2.calculate(maxWeight);
        printOut("Dynamic2", dynamicList2, otherWeights, otherPrice);
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

        Collections.sort(result);
        return result;
    }

    private static void printOut(String title, List<Integer> indexList, int[] weights, int[] prices) {
        System.out.printf(title + " result = {%s}\n", indexList.stream().map(String::valueOf).collect(joining(", ")));

        int overallWeight = indexList.stream().map(i -> weights[i]).reduce(0, Integer::sum);
        int overallPrice = indexList.stream().map(i -> prices[i]).reduce(0, Integer::sum);
        System.out.printf("\tPacked weight: %d\n", overallWeight);
        System.out.printf("\tPacked price: %d\n", overallPrice);
    }
}