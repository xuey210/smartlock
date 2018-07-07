package com.sectong.infrared;

/**
 * Created by xueyong on 16/9/9.
 * mobileeasy.
 */
public class Formula_15000 implements Formula {
    @Override
    public int formula(FormulaParam formulaParam) {
        return formulaParam.getOnoff() * 7500 +
                formulaParam.getMode() * 1500 +
                formulaParam.getTemp() * 100 +
                formulaParam.getWind() * 25 +
                formulaParam.getWinddir() * 5 +
                formulaParam.getKey() + 1;
    }
}
