import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class RomanInvalidInputTest {
    @ParameterizedTest(name = "CT-INV-N: rejeita {0}")
    @ValueSource(ints = {Integer.MIN_VALUE, -3999, -1, 0, 4000, 4001, Integer.MAX_VALUE})
    void rejectsNumbersOutsideDomain(int number) {
        assertThrows(IllegalArgumentException.class, () -> RomanNumerals.toRoman(number));
    }

    static Stream<String> invalidRomans() {
        return Stream.of(null, "", " ", "\t", "\n", "iv", "mm", "Iv", " IV", "IV ",
                "I V", "IV\n", "ABC", "123", "Ⅰ", "IIII", "VV", "XXXX", "LL", "CCCC",
                "DD", "MMMM", "IC", "IL", "ID", "IM", "VX", "LC", "DM", "IIV", "IXIX",
                "MCMC", "CMCM", "IVI", "VIV", "I".repeat(10000));
    }

    @ParameterizedTest(name = "CT-INV-R[{index}]: rejeita texto inválido")
    @MethodSource("invalidRomans")
    void rejectsInvalidRomans(String roman) {
        assertThrows(IllegalArgumentException.class, () -> RomanNumerals.fromRoman(roman));
    }

    @ParameterizedTest(name = "CT-BORDA: {0} é válido")
    @ValueSource(ints = {1, 3999})
    void acceptsValidDomainEndpoints(int number) {
        String expected = RomanReference.encode(number);
        assertEquals(expected, RomanNumerals.toRoman(number));
        assertEquals(number, RomanNumerals.fromRoman(expected));
    }
}
