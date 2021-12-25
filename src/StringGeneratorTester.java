
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class StringGeneratorTester {
    public static void main(String[] args) {
        boolean done = false;
        while(!done) {
            Scanner in = new Scanner(System.in);
            System.out.println("Please enter filename: ");
            String filename = in.nextLine();
            try {
                StringGenerator stringGenerator = new StringGenerator(filename);
                System.out.println(stringGenerator.getStr1());
                System.out.println(stringGenerator.getStr2());
                done = true;
            } catch (FileNotFoundException e) {
                System.out.println("File not found: " + filename);
            } catch (BadDataException e) {
                System.out.println("Bad Data: " + e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
