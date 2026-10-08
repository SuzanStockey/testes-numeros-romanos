import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

/** Valida os oráculos, separadamente da avaliação funcional do SUT. */
class RomanInfrastructureTest {
    @ParameterizedTest(name = "modelo: {0}: {1} = {2}")
    @MethodSource("FixedCases#arguments")
    void referenceAgreesWithPredefinedLiterals(String ids, int number, String roman) {
        assertEquals(roman, RomanReference.encode(number), ids);
        assertTrue(RomanReference.isCanonical(roman), ids);
    }

    @Test
    void formatOracleRejectsNonCanonicalForms() {
        for (String invalid : new String[]{"", "IIII", "IC", "VX", "MMMM", "iv", " IV", "IV\n"}) {
            assertFalse(RomanReference.isCanonical(invalid), "oráculo aceitou: " + invalid);
        }
        assertFalse(RomanReference.isCanonical(null));
    }
}
