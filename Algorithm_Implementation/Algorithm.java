import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.math.BigInteger;

public class Algorithm {

    private static BigInteger decodeValue(String value, int base) {
        return new BigInteger(value, base);
    }

    private static BigInteger findC(BigInteger[] x, BigInteger[] y, int k) {

        BigInteger c = BigInteger.ZERO;

        for (int i = 0; i < k; i++) {

            BigInteger numerator = BigInteger.ONE;
            BigInteger denominator = BigInteger.ONE;

            for (int j = 0; j < k; j++) {
                if (i != j) {
                    numerator = numerator.multiply(x[j].negate());
                    denominator = denominator.multiply(x[i].subtract(x[j]));
                }
            }

            BigInteger term = y[i].multiply(numerator).divide(denominator);
            c = c.add(term);
        }

        return c;
    }

    public static void main(String[] args) throws Exception {

        String content = new String(
                Files.readAllBytes(Paths.get("input.json"))
        );

        int n = Integer.parseInt(
                content.split("\"n\"\\s*:\\s*")[1].split(",")[0].trim()
        );
        int k = Integer.parseInt(
                content.split("\"k\"\\s*:\\s*")[1].split("\\}")[0].trim()
        );

        if (n == 10 && k < 7) {
            throw new IllegalArgumentException(
                "For n = 10, threshold k must be at least 7"
            );
        }

        BigInteger[] x = new BigInteger[k];
        BigInteger[] y = new BigInteger[k];
        int index = 0;

        String[] blocks = content.split("\\},");

        for (String block : blocks) {
            if (block.contains("\"base\"") && index < k) {

                String keyPart = block.split(":")[0].replaceAll("[^0-9]", "");
                if (keyPart.isEmpty()) continue;

                int xi = Integer.parseInt(keyPart);

                int base = Integer.parseInt(
                        block.split("\"base\"\\s*:\\s*\"")[1].split("\"")[0]
                );

                String value = block.split("\"value\"\\s*:\\s*\"")[1].split("\"")[0];

                x[index] = BigInteger.valueOf(xi);
                y[index] = decodeValue(value, base);
                index++;
            }
        }

        BigInteger c = findC(x, y, k);

        System.out.println("Constant term c = " + c);
    }

}
