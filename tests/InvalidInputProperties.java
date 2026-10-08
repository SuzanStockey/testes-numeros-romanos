import net.jqwik.api.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InvalidInputProperties {
    @Provide
    Arbitrary<Integer> outsideDomain() {
        return Arbitraries.oneOf(Arbitraries.integers().between(Integer.MIN_VALUE, 0),
                Arbitraries.integers().between(4000, Integer.MAX_VALUE));
    }

    @Property(tries = 1000, seed = "42", generation = GenerationMode.RANDOMIZED,
            edgeCases = EdgeCasesMode.NONE, afterFailure = AfterFailureMode.RANDOM_SEED)
    @Label("CT-INV-P01: inteiros fora do domínio")
    void rejectsAnyOutsideNumber(@ForAll("outsideDomain") int number) {
        assertThrows(IllegalArgumentException.class, () -> RomanNumerals.toRoman(number));
    }

    @Provide
    Arbitrary<String> romanWithUnknownSymbol() {
        return Arbitraries.integers().between(1, 3999).map(n -> RomanReference.encode(n) + "!");
    }

    @Property(tries = 1000, seed = "42", generation = GenerationMode.RANDOMIZED,
            edgeCases = EdgeCasesMode.NONE, afterFailure = AfterFailureMode.RANDOM_SEED)
    @Label("CT-INV-P02: símbolo desconhecido")
    void rejectsUnknownSymbol(@ForAll("romanWithUnknownSymbol") String roman) {
        assertThrows(IllegalArgumentException.class, () -> RomanNumerals.fromRoman(roman));
    }
}
