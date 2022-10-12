import java.util.ArrayList;
import java.util.List;

public class BruteForceAlgorithm extends BackpackAlgorithm {
    final long MAX_VARIANTS;

    public BruteForceAlgorithm(int[] weights, int[] prices) {
        super(weights, prices);
        MAX_VARIANTS = 2L << count;
    }

    private record Pack(int weight, int price) { }

    @Override
    public List<Integer> calculate(int maxWeight) {
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

    private Pack calculatePackFor(long variant, int[] prices, int[] weights) {
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
