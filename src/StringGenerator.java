import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 */
public class StringGenerator
{
    private String str1;
    private String str2;

    public StringGenerator(String filename) throws IOException {
        File inFile = new File(filename);
        try(Scanner in = new Scanner(inFile)) {
            if(!in.hasNext()) {
                throw new BadDataException("No string");
            }
            str1 = in.next();
            while(in.hasNextInt()) {
                int loc = in.nextInt();
                if (loc < -1 || loc > str1.length() - 1) {
                    throw new BadDataException("Location exceeds string1 bound");
                }
                StringBuilder stringBuilder = new StringBuilder(str1);
                stringBuilder.insert(loc + 1, str1);
                str1 = stringBuilder.toString();
            }
            str2 = in.next();
            while(in.hasNextInt()) {
                int loc = in.nextInt();
                if (loc < -1 || loc > str2.length() - 1) {
                    throw new BadDataException("Location exceeds string2 bound");
                }
                StringBuilder stringBuilder = new StringBuilder(str2);
                stringBuilder.insert(loc + 1, str2);
                str2 = stringBuilder.toString();
            }
            if(in.hasNext()) {
                throw new BadDataException("End of file expected");
            }
        }catch(FileNotFoundException e) {
            throw new FileNotFoundException(filename);
        }
    }

    public String getStr1() {
        return str1;
    }

    public String getStr2() {
        return str2;
    }
}


