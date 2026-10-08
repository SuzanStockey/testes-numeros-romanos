import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RomanExamplesTest {
    @ParameterizedTest(name = "{0}-E: {1} -> {2}")
    @MethodSource("FixedCases#arguments")
    void encodesKnownPairs(String ids, int number, String roman) {
        assertEquals(roman, RomanNumerals.toRoman(number), ids + "-E");
    }

    @ParameterizedTest(name = "{0}-D: {2} -> {1}")
    @MethodSource("FixedCases#arguments")
    void decodesKnownPairs(String ids, int number, String roman) {
        assertEquals(number, RomanNumerals.fromRoman(roman), ids + "-D");
    }

    @Test
    @DisplayName("CT-SEQ-01: chamadas intercaladas independentes")
    void interleavedCallsDoNotChangeResults() {
        assertEquals("IV", RomanNumerals.toRoman(4));
        assertEquals(3999, RomanNumerals.fromRoman("MMMCMXCIX"));
        assertEquals("MMVIII", RomanNumerals.toRoman(2008));
        assertEquals(4, RomanNumerals.fromRoman("IV"));
        assertEquals("IV", RomanNumerals.toRoman(4));
        assertEquals(3999, RomanNumerals.fromRoman("MMMCMXCIX"));
    }
}
