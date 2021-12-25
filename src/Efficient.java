import java.awt.*;
import java.io.*;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

public class Efficient {
    private static TreeSet<Point> points;
    public static void main(String[] args) {
            try {
                String filename = args[0];
                StringGenerator stringGenerator = new StringGenerator(filename);
                PenaltyData penaltyData = new PenaltyData();
                String str1 = stringGenerator.getStr1();
                String str2 = stringGenerator.getStr2();
                points = new TreeSet<>(new PointComparator());
                alignDC(str1, 0, str1.length(), str2, 0, str2.length(), penaltyData);
                String[] alignedStrDC = findAlignDC(str1, str2);
                File file = new File("outpute.txt");
                BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
                bw.write(alignedStrDC[0].substring(0, 50));
                bw.append(" ");
                bw.append(alignedStrDC[0].substring(alignedStrDC[0].length()  - 50)+"\n");
                bw.append(alignedStrDC[1].substring(0,50));
                bw.append(" ");
                bw.append(alignedStrDC[1].substring(alignedStrDC[1].length() -50)+"\n");
                bw.append(String.valueOf(findPenaltyDC(str1, str2, penaltyData)));
                bw.close();
            } catch (FileNotFoundException e) {
                System.out.println("File not found: " + e.getMessage());
            } catch (BadDataException e) {
                System.out.println("Bad Data: " + e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }

    }

    private static int[][] align(String str1, int first1, int last1,
                                 String str2, int first2, int last2, PenaltyData penaltyData) throws BadDataException{
        int[][] dp = new int[last1 - first1 + 1][last2 - first2 + 1];
        for(int i = 0; i <= last1 - first1; i++) {
            dp[i][0] = i * penaltyData.getGapPenalty();
        }
        for(int j = 0; j <= last2 - first2; j++) {
            dp[0][j] = j * penaltyData.getGapPenalty();
        }
        for(int j = 1; j <= last2 - first2; j++) {
            for(int i = 1; i <= last1 - first1; i++) {
                dp[i][j] = Math.min(dp[i - 1][j - 1] + penaltyData.getMismatchPenalty(str1.charAt(i + first1 - 1),
                        str2.charAt(j + first2 - 1)), Math.min(dp[i][j - 1] + penaltyData.getGapPenalty(),
                        dp[i - 1][j] + penaltyData.getGapPenalty()));
            }
        }
        return dp;
    }


    private static int[] alignSpaceEfficient(String str1, int first1, int last1,
                                             String str2, int first2, int last2, PenaltyData penaltyData) throws BadDataException {
        int[][] dp = new int[last1 - first1 + 1][2];
        for(int i = 0; i <= last1 - first1; i++) {
            dp[i][0] = i * penaltyData.getGapPenalty();
        }
        for(int j = 1; j <= last2 - first2; j++) {
            dp[0][1] = j * penaltyData.getGapPenalty();
            for(int i = 1; i <= last1 - first1; i++) {
                dp[i][1] = Math.min(dp[i - 1][0] + penaltyData.getMismatchPenalty(str1.charAt(i + first1 - 1),
                        str2.charAt(j + first2 - 1)), Math.min(dp[i][0] + penaltyData.getGapPenalty(),
                        dp[i - 1][1] + penaltyData.getGapPenalty()));
            }
            for(int i = 0; i <= last1 - first1; i++) {
                dp[i][0] = dp[i][1];
            }
        }
        int[] last = new int[last1 - first1 + 1];
        for(int i = 0; i < last.length; i++) {
            last[i] = dp[i][1];
        }
        return last;
    }

    private static int[] alignSpaceEfficientReverse(String str1, int first1, int last1,
                                             String str2, int first2, int last2, PenaltyData penaltyData) throws BadDataException {
        int[][] dp = new int[last1 - first1 + 1][2];
        for(int i = 0; i <= last1 - first1; i++) {
            dp[i][0] = i * penaltyData.getGapPenalty();
        }
        for(int j = 1; j <= last2 - first2; j++) {
            dp[0][1] = j * penaltyData.getGapPenalty();
            for(int i = 1; i <= last1 - first1; i++) {
                dp[i][1] = Math.min(dp[i - 1][0] + penaltyData.getMismatchPenalty(str1.charAt(last1 - i),
                        str2.charAt(last2 - j)), Math.min(dp[i][0] + penaltyData.getGapPenalty(),
                        dp[i - 1][1] + penaltyData.getGapPenalty()));
            }
            for(int i = 0; i <= last1 - first1; i++) {
                dp[i][0] = dp[i][1];
            }
        }
        int[] last = new int[last1 - first1 + 1];
        for(int i = 0; i < last.length; i++) {
            last[i] = dp[i][1];
        }
        return last;
    }

    private static void alignDC(String str1, int first1, int last1,
                                String str2, int first2, int last2, PenaltyData penaltyData)
            throws BadDataException {
        if(last1 - first1 <= 2 || last2 - first2 <= 2) {
            int[][] dp = align(str1, first1, last1, str2, first2, last2, penaltyData);
            findAlignE(dp, str1, last1, str2, last2, penaltyData);
            return;
        }
        int[] f = alignSpaceEfficient(str1, first1, last1, str2, first2, (first2 + last2) / 2 + 1, penaltyData);
        int[] g = alignSpaceEfficientReverse(str1, first1, last1, str2, (first2 + last2) / 2+1, last2, penaltyData);
        int q = 0;
        int min = f[0] + g[f.length - 1];
        for(int i = 0; i < f.length; i++) {
            if(f[i] + g[f.length - i - 1] < min) {
                min = f[i] + g[f.length - i - 1];
                q = i;
            }
        }
        q = q + first1;
        points.add(new Point(q, (first2 + last2) / 2 + 1));
        alignDC(str1, first1, q, str2, first2, (first2 + last2) / 2 + 1, penaltyData);
        alignDC(str1, q, last1, str2, (first2 + last2) / 2 + 1, last2, penaltyData);
    }

    private static void findAlignE(int[][] dp, String str1, int last1,
                                   String str2, int last2, PenaltyData penaltyData)
            throws BadDataException {
        int i = dp.length - 1;
        int j = dp[0].length - 1;
        points.add(new Point(last1, last2));
        while(i > 0 && j > 0) {
            if(dp[i][j] == dp[i - 1][j - 1] + penaltyData.getMismatchPenalty(str1.charAt(last1 - 1),
                    str2.charAt(last2 - 1))) {
                points.add(new Point(last1 - 1, last2 - 1));
                i--; last1--;
                j--; last2--;
            }
            else if(dp[i][j] == dp[i][j - 1] + penaltyData.getGapPenalty()) {
                points.add(new Point(last1, last2 - 1));
                j--; last2--;
            }
            else if(dp[i][j] == dp[i - 1][j] + penaltyData.getGapPenalty()) {
                points.add(new Point(last1 - 1, last2));
                i--; last1--;
            }
        }
        if(i == 0) {
            while(j > 0) {
                points.add(new Point(last1, last2 - 1));
                j--; last2--;
            }
        }
        if(j == 0) {
            while(i > 0) {
                points.add(new Point(last1 - 1, last2));
                i--; last1--;
            }
        }
    }

    private static String[] findAlignDC(String str1, String str2) {
        StringBuilder str1ABuilder = new StringBuilder();
        StringBuilder str2ABuilder = new StringBuilder();
        Iterator<Point> iter = points.iterator();
        if(!iter.hasNext()){
            return new String[]{str1ABuilder.toString(),str2ABuilder.toString()};
        }
        Point prev = iter.next();
        while(iter.hasNext()) {
            Point curr = iter.next();
            if ((int) (curr.getX() - prev.getX()) == 1 && (int) (curr.getY() - prev.getY()) == 1) {
                    str1ABuilder.append(str1.charAt((int) prev.getX()));
                    str2ABuilder.append(str2.charAt((int) prev.getY()));
            }
            if ((int) (curr.getX() - prev.getX()) == 0 && (int) (curr.getY() - prev.getY()) == 1) {
                str1ABuilder.append('_');
                str2ABuilder.append(str2.charAt((int) prev.getY()));
            }
            if ((int) (curr.getX() - prev.getX()) == 1 && (int) (curr.getY() - prev.getY()) == 0) {
                str1ABuilder.append(str1.charAt((int) prev.getX()));
                str2ABuilder.append('_');
            }
            prev = curr;
        }
        return new String[]{str1ABuilder.toString(),str2ABuilder.toString()};
    }

    private static int findPenaltyDC(String str1, String str2, PenaltyData penaltyData) throws BadDataException {
        int penalty = 0;
        Iterator<Point> iter = points.iterator();
        if(!iter.hasNext()){
            return penalty;
        }
        Point prev = iter.next();
        while (iter.hasNext()) {
            Point curr = iter.next();
            if ((int) (curr.getX() - prev.getX()) == 1 && (int) (curr.getY() - prev.getY()) == 1) {
                    penalty += penaltyData.getMismatchPenalty(str1.charAt((int) prev.getX()), str2.charAt((int) prev.getY()));
            }
            if ((int) (curr.getX() - prev.getX()) == 0 && (int) (curr.getY() - prev.getY()) == 1) {
                penalty += penaltyData.getGapPenalty();
            }
            if ((int) (curr.getX() - prev.getX()) == 1 && (int) (curr.getY() - prev.getY()) == 0) {
                penalty += penaltyData.getGapPenalty();
            }
            prev = curr;
        }
        return penalty;
    }

    static class PointComparator implements Comparator<Point>{
        public int compare(Point a, Point b){
            if(a.getX() == b.getX()){
                return (int) (a.getY() - b.getY());
            }
            return (int) (a.getX() - b.getX());
        }
    }

}
