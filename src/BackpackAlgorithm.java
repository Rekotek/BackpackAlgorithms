public abstract class BackpackAlgorithm implements Algorithm {
    final protected int[] weights;
    final protected int[] prices;
    final protected int count;

    public BackpackAlgorithm(int[] weights, int[] prices) {
        if (weights.length != prices.length) {
            throw new IllegalArgumentException(String.format("Lengths of Weights (%s) and Prices (%s) are not much", weights.length, prices.length));
        }
        this.weights = weights;
        this.prices = prices;
        count = weights.length;
    }

}
