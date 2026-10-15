import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Self-checks for the Chapter 2 readers on random input, with tiny buffer sizes too. */
public class Ch02Check {

    private static final Random RANDOM = new Random(22);

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    public static void main(String[] args) throws IOException {
        for (int trial = 0; trial < 3_000; trial++) {
            List<String> tokens = new ArrayList<>();
            StringBuilder text = new StringBuilder();
            int count = RANDOM.nextInt(12);
            for (int i = 0; i < count; i++) {
                String space = String.valueOf(" \n\t\r".charAt(RANDOM.nextInt(4)));
                text.append(space.repeat(RANDOM.nextInt(3)));
                String token = RANDOM.nextBoolean()
                        ? Long.toString(RANDOM.nextLong() >> RANDOM.nextInt(64))
                        : "w" + RANDOM.nextInt(1000);
                tokens.add(token);
                text.append(token).append(i + 1 < count ? " " : "");
            }
            text.append("\n".repeat(RANDOM.nextInt(3)));
            byte[] bytes = text.toString().getBytes(StandardCharsets.US_ASCII);
            int bufferSize = 1 + RANDOM.nextInt(8);    // tiny buffers split tokens apart
            FastReader reader =
                    new FastReader(new ByteArrayInputStream(bytes), bufferSize);
            OaTemplate.FastIn fastIn =
                    new OaTemplate.FastIn(new ByteArrayInputStream(bytes));
            for (String token : tokens) {
                require(reader.hasNext(), "hasNext before " + token);
                if (token.startsWith("w")) {
                    require(reader.next().equals(token), "next " + token);
                    require(fastIn.next().equals(token), "FastIn next " + token);
                } else {
                    long expected = Long.parseLong(token);
                    require(reader.nextLong() == expected, "nextLong " + token);
                    require(fastIn.nextLong() == expected, "FastIn nextLong " + token);
                }
            }
            require(!reader.hasNext(), "hasNext at end");
        }
        System.out.println("Ch02Check: all checks passed");
    }
}
