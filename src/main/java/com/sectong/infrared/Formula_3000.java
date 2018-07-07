package com.sectong.infrared;

/**
 * Created by xueyong on 16/9/9.
 * mobileeasy.
 */
public class Formula_3000 implements Formula {

    @Override
    public int formula(FormulaParam formulaParam) {
        return formulaParam.getOnoff() * 1500 +
                formulaParam.getMode() * 300 +
                formulaParam.getTemp() * 20 +
                formulaParam.getWind() * 5 +
                formulaParam.getWinddir() + 1;

    }
}
