import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DynamicAlgorithm extends BackpackAlgorithm {
    private final int[][] values = new int[count + 1][];

    public DynamicAlgorithm(int[] weights, int[] prices) {
        super(weights, prices);
    }

    @Override
    public List<Integer> calculate(int maxWeight) {
        prepareValueMatrix(maxWeight);

        for (int n = 0; n <= count; n++) {
            for (int w = 0; w <= maxWeight; w++) {
                if (n == 0 || w == 0) {
                    values[n][w] = 0;
                } else {
                    if (w < weights[n - 1]) {
                        values[n][w] = values[n - 1][w];
                    } else {
                        int possibleValue = values[n - 1][w - weights[n - 1]] + prices[n - 1];
                        values[n][w] = Math.max(values[n - 1][w], possibleValue);
                    }
                }
            }
        }
        var resultList = new ArrayList<Integer>(count);
        traceResult(count, maxWeight, resultList);
        Collections.reverse(resultList);
        return resultList;
    }

    private void prepareValueMatrix(int maxWeight) {
        for (int i = 0; i <= count; i++) {
            values[i] = new int[maxWeight + 1];
        }
    }

    private void traceResult(int n, int w, ArrayList<Integer> resultList) {
        if (values[n][w] == 0) {
            return;
        }
        if (values[n][w] == values[n - 1][w]) {
            traceResult(n - 1, w, resultList);
        } else {
            resultList.add(n - 1);
            int newWeightIndex = w - weights[n - 1];
            if (newWeightIndex == 0) {
                return;
            }
            traceResult(n - 1, newWeightIndex, resultList);
        }
    }
}
