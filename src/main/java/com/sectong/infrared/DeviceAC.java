package com.sectong.infrared;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xueyong on 16/9/9.
 * mobileeasy.
 */
public class DeviceAC extends DeviceManager {

    private static final Logger logger = LoggerFactory.getLogger(DeviceAC.class);

    @Override
    public int getIndex(ModelEntity modelEntity, FormulaParam formulaParam) {
        Formula formula = null;
        if (modelEntity.getKeySquency().equals("15000")) {
            formula = new Formula_15000();
        }
        if (modelEntity.getKeySquency().equals("3000")) {
            formula = new Formula_3000();
        }
        if (modelEntity.getKeySquency().equals("14")) {
            formula = new Formula_15();
        }
        int result = formula.formula(formulaParam);
        logger.info("index is :{}", new Object[]{result});
        return result;
    }

    @Override
    public String[] createTestCode(ModelEntity modelEntity, FormatEntity formatEntity) {
        String[] codes = new String[2];
        int index_on = getIndex(modelEntity, new FormulaParam(0));
        int index_off = getIndex(modelEntity, new FormulaParam(1));
        codes[0] = getFinalCode(index_on, modelEntity, formatEntity);
        codes[1] = getFinalCode(index_off, modelEntity, formatEntity);
        return codes;
    }

    @Override
    public List<DeviceFunction> allStatus(String model) {
        ModelEntity modelEntity = getKeyFileAndSquency(model);
        logger.info("modelEntity is :{},s :{}", new Object[]{modelEntity, model});
        if (modelEntity == null) return null;
        FormatEntity formatEntity = getFormat(modelEntity.getFormatId());
        if (formatEntity == null) return null;
        List<DeviceFunction> deviceFunctions = getStract();
        for (DeviceFunction deviceFunction : deviceFunctions) {
            for (FunctionStatus functionStatu : deviceFunction.getStatusMap()) {
                if (deviceFunction.getCode().equals("conoff")) {
                    int index = getIndex(modelEntity, FormulaParam.onoffInstance(Integer.valueOf(functionStatu.getStatus())));
                    functionStatu.setRcode(getFinalCode(index, modelEntity, formatEntity));
                }
                if (deviceFunction.getCode().equals("cmodel")) {
                    int index = getIndex(modelEntity, FormulaParam.modelInstance(Integer.valueOf(functionStatu.getStatus())));
                    functionStatu.setRcode(getFinalCode(index, modelEntity, formatEntity));
                }
                if (deviceFunction.getCode().equals("ctemp")) {
                    int index = getIndex(modelEntity, FormulaParam.tempInstance(Integer.valueOf(functionStatu.getStatus())));
                    functionStatu.setRcode(getFinalCode(index, modelEntity, formatEntity));
                }
                if (deviceFunction.getCode().equals("cwind")) {
                    int index = getIndex(modelEntity, FormulaParam.windInstance(Integer.valueOf(functionStatu.getStatus())));
                    functionStatu.setRcode(getFinalCode(index, modelEntity, formatEntity));
                }
                if (deviceFunction.getCode().equals("cwinddir")) {
                    int index = getIndex(modelEntity, FormulaParam.winddirInstance(Integer.valueOf(functionStatu.getStatus())));
                    functionStatu.setRcode(getFinalCode(index, modelEntity, formatEntity));
                }
                if (deviceFunction.getCode().equals("ckey")) {
                    int index = getIndex(modelEntity, FormulaParam.keyInstance(Integer.valueOf(functionStatu.getStatus())));
                    functionStatu.setRcode(getFinalCode(index, modelEntity, formatEntity));
                }
            }
        }
        return deviceFunctions;
    }

    @Override
    protected List<DeviceFunction> allStatus(String model, String path, int length) {
        return null;
    }

    protected List<DeviceFunction> getStract() {
        List<DeviceFunction> deviceFunctions = new ArrayList<>(6);
        //onoff
        List<FunctionStatus> onoffstatus = new ArrayList<>(2);
        onoffstatus.add(new FunctionStatus("0"));
        onoffstatus.add(new FunctionStatus("1"));
        DeviceFunction onoff = new DeviceFunction("conoff", "开关", onoffstatus);
        deviceFunctions.add(onoff);

        //model
        List<FunctionStatus> cmodel = new ArrayList<>(5);
        cmodel.add(new FunctionStatus("0"));
        cmodel.add(new FunctionStatus("1"));
        cmodel.add(new FunctionStatus("2"));
        cmodel.add(new FunctionStatus("3"));
        cmodel.add(new FunctionStatus("4"));
        DeviceFunction model = new DeviceFunction("cmodel", "运转模式", cmodel);
        deviceFunctions.add(model);

        //temp
        List<FunctionStatus> ctemp = new ArrayList<>(15);
        ctemp.add(new FunctionStatus("0"));
        ctemp.add(new FunctionStatus("1"));
        ctemp.add(new FunctionStatus("2"));
        ctemp.add(new FunctionStatus("3"));
        ctemp.add(new FunctionStatus("4"));
        ctemp.add(new FunctionStatus("5"));
        ctemp.add(new FunctionStatus("6"));
        ctemp.add(new FunctionStatus("7"));
        ctemp.add(new FunctionStatus("8"));
        ctemp.add(new FunctionStatus("9"));
        ctemp.add(new FunctionStatus("10"));
        ctemp.add(new FunctionStatus("11"));
        ctemp.add(new FunctionStatus("12"));
        ctemp.add(new FunctionStatus("13"));
        ctemp.add(new FunctionStatus("14"));
        DeviceFunction temp = new DeviceFunction("ctemp", "温度", ctemp);
        deviceFunctions.add(temp);

        List<FunctionStatus> cwind = new ArrayList<>(4);
        cwind.add(new FunctionStatus("0"));
        cwind.add(new FunctionStatus("1"));
        cwind.add(new FunctionStatus("2"));
        cwind.add(new FunctionStatus("3"));
        DeviceFunction wind = new DeviceFunction("cwind", "风向", cwind);
        deviceFunctions.add(wind);

        List<FunctionStatus> cwinddir = new ArrayList<>(5);
        cwinddir.add(new FunctionStatus("0"));
        cwinddir.add(new FunctionStatus("1"));
        cwinddir.add(new FunctionStatus("2"));
        cwinddir.add(new FunctionStatus("3"));
        cwinddir.add(new FunctionStatus("4"));
        DeviceFunction winddir = new DeviceFunction("cwinddir", "风速", cwinddir);
        deviceFunctions.add(winddir);

        List<FunctionStatus> ckey = new ArrayList<>(5);
        ckey.add(new FunctionStatus("0"));
        ckey.add(new FunctionStatus("1"));
        ckey.add(new FunctionStatus("2"));
        ckey.add(new FunctionStatus("3"));
        ckey.add(new FunctionStatus("4"));
        DeviceFunction key = new DeviceFunction("ckey", "键值", ckey);
        deviceFunctions.add(key);
        return deviceFunctions;
    }

}
