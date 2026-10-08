import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

/** Pares literais definidos antes da implementação; IDs repetidos usam um único dado. */
final class FixedCases {
    private FixedCases() {}
    private record Case(String ids, int number, String roman) {}
    private static final List<Case> CASES = List.of(
            new Case("CT-EQ-01 / CT-LIM-07", 5, "V"),
            new Case("CT-EQ-02", 50, "L"),
            new Case("CT-EQ-03", 500, "D"),
            new Case("CT-EQ-04 / CT-LIM-24", 1000, "M"),
            new Case("CT-EQ-05", 1666, "MDCLXVI"),
            new Case("CT-EQ-06", 86, "LXXXVI"),
            new Case("CT-EQ-07", 2008, "MMVIII"),
            new Case("CT-EQ-08 / CT-LIM-25", 1001, "MI"),
            new Case("CT-EQ-09", 49, "XLIX"),
            new Case("CT-EQ-10", 944, "CMXLIV"),
            new Case("CT-EQ-11", 1990, "MCMXC"),
            new Case("CT-EQ-12", 3888, "MMMDCCCLXXXVIII"),
            new Case("CT-LIM-01", 1, "I"),
            new Case("CT-LIM-02", 2, "II"),
            new Case("CT-LIM-03", 3998, "MMMCMXCVIII"),
            new Case("CT-LIM-04", 3999, "MMMCMXCIX"),
            new Case("CT-LIM-05", 3, "III"),
            new Case("CT-LIM-06", 4, "IV"),
            new Case("CT-LIM-08", 8, "VIII"),
            new Case("CT-LIM-09", 9, "IX"),
            new Case("CT-LIM-10", 10, "X"),
            new Case("CT-LIM-11", 39, "XXXIX"),
            new Case("CT-LIM-12", 40, "XL"),
            new Case("CT-LIM-13", 41, "XLI"),
            new Case("CT-LIM-14", 89, "LXXXIX"),
            new Case("CT-LIM-15", 90, "XC"),
            new Case("CT-LIM-16", 91, "XCI"),
            new Case("CT-LIM-17", 399, "CCCXCIX"),
            new Case("CT-LIM-18", 400, "CD"),
            new Case("CT-LIM-19", 401, "CDI"),
            new Case("CT-LIM-20", 899, "DCCCXCIX"),
            new Case("CT-LIM-21", 900, "CM"),
            new Case("CT-LIM-22", 901, "CMI"),
            new Case("CT-LIM-23", 999, "CMXCIX")
    );

    static Stream<Arguments> arguments() {
        return CASES.stream().map(c -> Arguments.of(c.ids(), c.number(), c.roman()));
    }

    static List<Integer> boundaries() {
        return List.of(1, 2, 3998, 3999, 3, 4, 5, 8, 9, 10, 39, 40, 41,
                89, 90, 91, 399, 400, 401, 899, 900, 901, 999, 1000, 1001);
    }
}
