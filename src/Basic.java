import java.io.*;

public class Basic {
    public static void main(String[] args) {
        try {
            String filename = args[0];
            StringGenerator stringGenerator = new StringGenerator(filename);
            PenaltyData penaltyData = new PenaltyData();
            String str1 = stringGenerator.getStr1();
            String str2 = stringGenerator.getStr2();
            int[][] dp = align(str1, str2, penaltyData);
            String[] alignedStr = findAlign(dp, str1, str2, penaltyData);
            File file = new File("output.txt");
            BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
            bw.write(alignedStr[0].substring(0, 50));
            bw.append(" ");
            bw.append(alignedStr[0].substring(alignedStr[0].length() - 50)+"\n");
            bw.append(alignedStr[1].substring(0, 50));
            bw.append(" ");
            bw.append(alignedStr[1].substring(alignedStr[1].length() - 50)+"\n");
            bw.append(String.valueOf(dp[str1.length()][str2.length()]));
            bw.close();


        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (BadDataException e) {
            System.out.println("Bad Data: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static int[][] align(String str1, String str2, PenaltyData penaltyData) throws BadDataException {
        int[][] dp = new int[str1.length() + 1][str2.length() + 1];
        //initialization
        for (int i = 0; i <= str1.length(); i++) {
            dp[i][0] = i * penaltyData.getGapPenalty();
        }
        for (int j = 0; j <= str2.length(); j++) {
            dp[0][j] = j * penaltyData.getGapPenalty();
        }
        for (int j = 1; j <= str2.length(); j++) {
            for (int i = 1; i <= str1.length(); i++) {
                dp[i][j] = Math.min(dp[i - 1][j - 1] + penaltyData.getMismatchPenalty(str1.charAt(i - 1),
                        str2.charAt(j - 1)), Math.min(dp[i][j - 1] + penaltyData.getGapPenalty(),
                        dp[i - 1][j] + penaltyData.getGapPenalty()));
            }
        }
        return dp;
    }

    private static String[] findAlign(int[][] dp, String str1, String str2, PenaltyData penaltyData)
            throws BadDataException {
        int i = dp.length - 1;
        int j = dp[0].length - 1;
        StringBuilder str1ABuilder = new StringBuilder();
        StringBuilder str2ABuilder = new StringBuilder();
        while (i > 0 && j > 0) {
            if (dp[i][j] == dp[i - 1][j - 1] + penaltyData.getMismatchPenalty(str1.charAt(i - 1),
                    str2.charAt(j - 1))) {
                str1ABuilder.append(str1.charAt(i - 1));
                str2ABuilder.append(str2.charAt(j - 1));
                i--;
                j--;
            } else if (dp[i][j] == dp[i][j - 1] + penaltyData.getGapPenalty()) {
                str1ABuilder.append('_');
                str2ABuilder.append(str2.charAt(j - 1));
                j--;
            } else if (dp[i][j] == dp[i - 1][j] + penaltyData.getGapPenalty()) {
                str1ABuilder.append(str1.charAt(i - 1));
                str2ABuilder.append('_');
                i--;
            }
        }
        if (i == 0) {
            while (j > 0) {
                str1ABuilder.append('_');
                str2ABuilder.append(str2.charAt(j - 1));
                j--;
            }
        }
        if (j == 0) {
            while (i > 0) {
                str1ABuilder.append(str1.charAt(i - 1));
                str2ABuilder.append('_');
                i--;
            }
        }
        str1ABuilder.reverse();
        str2ABuilder.reverse();
        return new String[]{str1ABuilder.toString(), str2ABuilder.toString()};
    }

}

