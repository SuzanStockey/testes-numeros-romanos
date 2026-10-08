public class ExperimentProbe {
    public static void main(String[] args) {
        String id = args[0];
        int wrongEncoding = 0, wrongDecoding = 0;
        for (int n = 1; n <= 3999; n++) {
            String expected = RomanReference.encode(n);
            if (!expected.equals(RomanNumerals.toRoman(n))) wrongEncoding++;
            if (RomanNumerals.fromRoman(expected) != n) wrongDecoding++;
        }
        String witness = switch (id) {
            case "DF-01" -> RomanNumerals.toRoman(4);
            case "DF-02" -> Integer.toString(RomanNumerals.fromRoman("IV"));
            case "DF-03" -> RomanNumerals.toRoman(1001);
            case "DF-04" -> RomanNumerals.toRoman(3999);
            default -> "baseline";
        };
        System.out.printf("{\"encoding_differences\":%d,\"decoding_differences\":%d,\"witness_observed\":\"%s\"}%n",
            wrongEncoding, wrongDecoding, witness);
    }
}
