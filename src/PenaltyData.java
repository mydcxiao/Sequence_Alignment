import java.util.Arrays;
import java.util.HashMap;

public class PenaltyData {
    private int gapPenalty;
    private HashMap<String, Integer> mismatchPenalty;

    public PenaltyData()  {
        gapPenalty = 30;
        mismatchPenalty = new HashMap<>();
        mismatchPenalty.put("AA",0);
        mismatchPenalty.put("CC",0);
        mismatchPenalty.put("GG",0);
        mismatchPenalty.put("TT",0);
        mismatchPenalty.put("AC",110);
        mismatchPenalty.put("AG",48);
        mismatchPenalty.put("AT",94);
        mismatchPenalty.put("CG",118);
        mismatchPenalty.put("CT",48);
        mismatchPenalty.put("GT",110);

    }

    public int getMismatchPenalty(char char1, char char2) {
        char[] chars = new char[]{char1, char2};
        Arrays.sort(chars);
        String str = new String(chars);
        return mismatchPenalty.get(str);
    }

    public int getGapPenalty() {
        return gapPenalty;
    }

}
