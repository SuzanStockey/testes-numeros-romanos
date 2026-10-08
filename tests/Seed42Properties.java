import net.jqwik.api.*;

class Seed42Properties {
    @Provide
    Arbitrary<Integer> numbers() { return RomanPropertyChecks.numbers(); }

    @Provide
    Arbitrary<RomanReference.Pair> pairs() { return RomanPropertyChecks.pairs(); }

    @Property(tries = 1000, seed = "42", generation = GenerationMode.RANDOMIZED,
            edgeCases = EdgeCasesMode.NONE, afterFailure = AfterFailureMode.RANDOM_SEED)
    @Label("CT-PBT-01 seed=42")
    void p01(@ForAll("numbers") int n) {
        RomanPropertyChecks.p01(n);
    }

    @Property(tries = 1000, seed = "42", generation = GenerationMode.RANDOMIZED,
            edgeCases = EdgeCasesMode.NONE, afterFailure = AfterFailureMode.RANDOM_SEED)
    @Label("CT-PBT-02 seed=42")
    void p02(@ForAll("pairs") RomanReference.Pair pair) {
        RomanPropertyChecks.p02(pair);
    }

    @Property(tries = 1000, seed = "42", generation = GenerationMode.RANDOMIZED,
            edgeCases = EdgeCasesMode.NONE, afterFailure = AfterFailureMode.RANDOM_SEED)
    @Label("CT-PBT-03 seed=42")
    void p03(@ForAll("numbers") int n) {
        RomanPropertyChecks.p03(n);
    }

    @Property(tries = 1000, seed = "42", generation = GenerationMode.RANDOMIZED,
            edgeCases = EdgeCasesMode.NONE, afterFailure = AfterFailureMode.RANDOM_SEED)
    @Label("CT-PBT-04 seed=42")
    void p04(@ForAll("pairs") RomanReference.Pair pair) {
        RomanPropertyChecks.p04(pair);
    }

    @Property(tries = 1000, seed = "42", generation = GenerationMode.RANDOMIZED,
            edgeCases = EdgeCasesMode.NONE, afterFailure = AfterFailureMode.RANDOM_SEED)
    @Label("CT-PBT-05 seed=42")
    void p05(@ForAll("numbers") int n) {
        RomanPropertyChecks.p05(n);
    }

    @Property(tries = 1000, seed = "42", generation = GenerationMode.RANDOMIZED,
            edgeCases = EdgeCasesMode.NONE, afterFailure = AfterFailureMode.RANDOM_SEED)
    @Label("CT-PBT-06 seed=42")
    void p06(@ForAll("pairs") RomanReference.Pair pair) {
        RomanPropertyChecks.p06(pair);
    }
}
