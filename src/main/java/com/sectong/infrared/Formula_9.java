package com.sectong.infrared;

/**
 * Created by xueyong on 16/9/9.
 * mobileeasy.
 */
public class Formula_9 implements Formula {
    @Override
    public int formula(FormulaParam formulaParam) {
        return Integer.valueOf(formulaParam.getFanCode());
    }
}
