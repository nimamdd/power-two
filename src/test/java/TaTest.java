
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TaTest {

    private ByteArrayOutputStream outStream;

    public void baseTest (int test_no) {
        String result = "", correctResult = "";
        Process p;
        
        try {
            File myObj = new File("src/main/java/out" + test_no);
            Scanner myReader = new Scanner(myObj);
            correctResult = myReader.nextLine().trim();
            myReader.close();
        } catch (FileNotFoundException e) {
            System.err.println("File Error");
            fail ();
        }
        
        try {
            String[] cmd = {
               "/bin/bash",
               "-c",
               "cat src/main/java/in"+test_no+" | java -jar src/main/java/rars.jar nc src/main/java/solution.s"
            };
            p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            result = br.readLine().trim();
            assertEquals (true, result.equals(correctResult));
            p.destroy();
        } catch (Exception e) {
            System.err.println("Output Parse Error");
            fail ();
        }
    }
    
    @Before
    public void initStreams() {
        outStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outStream));
    }

    @Test
    public void test1() {
        baseTest (1);
    }

    @Test
    public void test2() {
        baseTest (2);
    }

    @Test
    public void test3() {
        baseTest (3);
    }




}
