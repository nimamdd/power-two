import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TaTest {

    private ByteArrayOutputStream outStream;

    public String ExpectedOutput(List<Integer> inputList) {
        StringBuilder sb = new StringBuilder();
        int count = inputList.get(0);

        for (int i = 1; i <= count; i++) {
            int n = inputList.get(i);
            int result = (n > 0 && (n & (n - 1)) == 0) ? 1 : 0;
            sb.append(result);
            if (i < count) {
                sb.append(" ");
            }
        }

        return sb.toString().trim();
    }

    public List<Integer> generateRandomTestNumbers() {
        Random random = new Random();
        Set<Integer> result = new HashSet<>();

        int totalCount = random.nextInt(15) + 5;
        int countPowersOfTwo = 2 + random.nextInt(Math.max(1, totalCount / 2));

        result.add(0);
        result.add(1);

        while (result.size() < countPowersOfTwo + 2) {
            int exp = random.nextInt(12);
            result.add((int) Math.pow(2, exp));
        }

        while (result.size() < totalCount) {
            int n = random.nextInt(10000);
            if (n > 0 && (n & (n - 1)) != 0) {
                result.add(n);
            }
        }

        ArrayList<Integer> finalList = new ArrayList<>(result);
        Collections.shuffle(finalList);
        finalList.add(0, finalList.size());
        return finalList;
    }

    public void baseTest(List<Integer> inputList, String correctResult) {
        Process p;

        try {
            StringBuilder inputBuilder = new StringBuilder();
            for (int num : inputList) {
                inputBuilder.append(num).append("\\n");
            }

            if (inputBuilder.length() >= 2) {
                inputBuilder.setLength(inputBuilder.length() - 2);
            }

            String echoInput = "echo -e \"" + inputBuilder.toString() + "\"";
            String fullCmd = echoInput + " | java -jar lib/rars.jar nc src/main/java/solution.s";

            String[] cmd = {"/bin/bash", "-c", fullCmd};

            p = Runtime.getRuntime().exec(cmd);
            p.waitFor();

            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String result = br.readLine().trim();
            br.close();

            result = result.trim();
            assertEquals(correctResult, result);
            p.destroy();

        } catch (Exception e) {
            System.err.println("Execution error: " + e.getMessage());
            fail();
        }
    }

    @Before
    public void initStreams() {
        outStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outStream));
    }

    @Test
    public void test1() {
        List<Integer> input = generateRandomTestNumbers();
        String correctOutput = ExpectedOutput(input);
        baseTest(input, correctOutput);
    }
}