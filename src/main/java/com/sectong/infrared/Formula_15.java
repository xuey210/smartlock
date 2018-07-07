package com.sectong.infrared;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xueyong on 16/9/9.
 * mobileeasy.
 */
public class Formula_15 implements Formula {

    private static final Map<String, Integer> f15Map = new HashMap<>(14);
    static {
        f15Map.put("ON/OFF", 1);
        f15Map.put("MODE0", 2);
        f15Map.put("MODE1", 3);
        f15Map.put("MODE2", 4);
        f15Map.put("MODE3", 5);
        f15Map.put("MODE4", 6);
        f15Map.put("TEMP+", 7);
        f15Map.put("TEMP-", 8);
        f15Map.put("WIND0", 9);
        f15Map.put("WIND1", 10);
        f15Map.put("WIND2", 11);
        f15Map.put("WIND3", 12);
        f15Map.put("WINDDIR0", 13);
        f15Map.put("WINDDIR1", 14);
    }
    @Override
    public int formula(FormulaParam formulaParam) {
        return f15Map.get(formulaParam.get_15key());
    }
}
