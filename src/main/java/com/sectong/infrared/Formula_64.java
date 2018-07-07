package com.sectong.infrared;

/**
 * Created by xueyong on 16/10/20.
 * mobileeasy.
 */
public class Formula_64 implements Formula {
    @Override
    public int formula(FormulaParam formulaParam) {
        return Integer.valueOf(formulaParam.getStbCode());
    }

}
