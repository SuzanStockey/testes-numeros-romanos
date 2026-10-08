import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/** Complemento exaustivo separado das execuções aleatórias. */
@Tag("exhaustive")
class RomanExhaustiveTest {
    @Test
    void encodesAll3999Values() {
        for (int n = 1; n <= 3999; n++) {
            assertEquals(RomanReference.encode(n), RomanNumerals.toRoman(n), "n=" + n);
        }
    }

    @Test
    void decodesAll3999CanonicalForms() {
        for (int n = 1; n <= 3999; n++) {
            String roman = RomanReference.encode(n);
            assertEquals(n, RomanNumerals.fromRoman(roman), "r=" + roman);
        }
    }
}
