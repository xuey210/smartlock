package com.sectong.infrared;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by xueyong on 16/10/21.
 * mobileeasy.
 */
public class DeviceACL extends DeviceManager {

    private static final Logger logger = LoggerFactory.getLogger(DeviceACL.class);

    @Override
    public int getIndex(ModelEntity modelEntity, FormulaParam formulaParam) {
        Formula formula = null;
        if ("38".equals(modelEntity.getKeySquency())) {
            formula = new Formula_38();
        }
//        int result = formula.formula(formulaParam);
        int result = Integer.valueOf(formulaParam.getAclCode());
        logger.info(this.getClass().getName() + " index is: {}", new Object[]{result});
        return result;
    }

    @Override
    public String[] createTestCode(ModelEntity modelEntity, FormatEntity formatEntity) {
        String[] codes = new String[1];
        codes[0] = getFinalCode(1, modelEntity, formatEntity);
        return codes;
    }

    @Override
    public List<DeviceFunction> allStatus(String model) {
        return allStatus(model, ACLPATH, 12);
    }

    @Override
    protected List<DeviceFunction> allStatus(String model, String path, int length) {
        List<DeviceFunction> deviceFunctions = getStract(path, length);
        try {
            ModelEntity modelEntity = getKeyFileAndSquency(model);
            logger.info("modelEntity is :{},s :{}", new Object[]{modelEntity, model});
            if (modelEntity == null) return null;
            FormatEntity formatEntity = getFormat(modelEntity.getFormatId());
            if (formatEntity == null) return null;
            for (DeviceFunction deviceFunction : deviceFunctions) {
                for (FunctionStatus functionStatu : deviceFunction.getStatusMap()) {
                    int index = getIndex(modelEntity, FormulaParam.aclCodeInstance(deviceFunction.getCode()));
                    functionStatu.setRcode(getFinalCode(index, modelEntity, formatEntity));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deviceFunctions;
    }
}
