package com.sectong.infrared;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xueyong on 16/9/12.
 * mobileeasy.
 */
public class DeviceTV extends DeviceManager {

    private static final Logger logger = LoggerFactory.getLogger(DeviceTV.class);

    @Override
    public int getIndex(ModelEntity modelEntity, FormulaParam formulaParam) {
        Formula formula = null;
        if (modelEntity.getKeySquency().equals("38")) {
            formula = new Formula_38();
        }
        int result = formula.formula(formulaParam);
        logger.info("index is :{}", new Object[]{result});
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
        ModelEntity modelEntity = getKeyFileAndSquency(model);
        logger.info("modelEntity is :{},s :{}", new Object[]{modelEntity, model});
        if (modelEntity == null) return null;
        FormatEntity formatEntity = getFormat(modelEntity.getFormatId());
        if (formatEntity == null) return null;
        List<DeviceFunction> deviceFunctions = getStract();
        for (DeviceFunction deviceFunction : deviceFunctions) {
            for (FunctionStatus functionStatu : deviceFunction.getStatusMap()) {
                int index = getIndex(modelEntity, FormulaParam.tvCodeInstance(deviceFunction.getCode()));
                functionStatu.setRcode(getFinalCode(index, modelEntity, formatEntity));
            }
        }
        return deviceFunctions;
    }

    @Override
    protected List<DeviceFunction> allStatus(String model, String path, int length) {
        return null;
    }

    protected List<DeviceFunction> getStract() {
        List<DeviceFunction> deviceFunctions = new ArrayList<>(38);

        List<FunctionStatus> power = new ArrayList<>(1);
        power.add(new FunctionStatus("power"));
        DeviceFunction _power = new DeviceFunction("1", "power", power);
        deviceFunctions.add(_power);

        List<FunctionStatus> cmodel = new ArrayList<>(1);
        cmodel.add(new FunctionStatus("丽音"));
        DeviceFunction model = new DeviceFunction("2", "丽音", cmodel);
        deviceFunctions.add(model);

        List<FunctionStatus> ctemp = new ArrayList<>(1);
        ctemp.add(new FunctionStatus("伴音"));
        DeviceFunction temp = new DeviceFunction("3", "伴音", ctemp);
        deviceFunctions.add(temp);

        List<FunctionStatus> cwind = new ArrayList<>(1);
        cwind.add(new FunctionStatus("制式"));
        DeviceFunction wind = new DeviceFunction("4", "制式", cwind);
        deviceFunctions.add(wind);

        List<FunctionStatus> cwinddir = new ArrayList<>(1);
        cwinddir.add(new FunctionStatus("睡眠"));
        DeviceFunction winddir = new DeviceFunction("5", "睡眠", cwinddir);
        deviceFunctions.add(winddir);

        List<FunctionStatus> ckey = new ArrayList<>(1);
        ckey.add(new FunctionStatus("1"));
        DeviceFunction key = new DeviceFunction("6", "1", ckey);
        deviceFunctions.add(key);

        List<FunctionStatus> c2 = new ArrayList<>(1);
        c2.add(new FunctionStatus("2"));
        DeviceFunction _c2 = new DeviceFunction("7", "2", c2);
        deviceFunctions.add(_c2);

        List<FunctionStatus> c3 = new ArrayList<>(1);
        c3.add(new FunctionStatus("3"));
        DeviceFunction _c3 = new DeviceFunction("8", "3", c3);
        deviceFunctions.add(_c3);

        List<FunctionStatus> c4 = new ArrayList<>(1);
        c4.add(new FunctionStatus("4"));
        DeviceFunction _c4 = new DeviceFunction("9", "4", c4);
        deviceFunctions.add(_c4);

        List<FunctionStatus> c5 = new ArrayList<>(1);
        c5.add(new FunctionStatus("5"));
        DeviceFunction _c5 = new DeviceFunction("10", "5", c5);
        deviceFunctions.add(_c5);

        List<FunctionStatus> c6 = new ArrayList<>(1);
        c6.add(new FunctionStatus("6"));
        DeviceFunction _c6 = new DeviceFunction("11", "6", c6);
        deviceFunctions.add(_c6);

        List<FunctionStatus> c7 = new ArrayList<>(1);
        c7.add(new FunctionStatus("7"));
        DeviceFunction _c7 = new DeviceFunction("12", "7", c7);
        deviceFunctions.add(_c7);

        List<FunctionStatus> c8 = new ArrayList<>(1);
        c8.add(new FunctionStatus("8"));
        DeviceFunction _c8 = new DeviceFunction("13", "8", c8);
        deviceFunctions.add(_c8);

        List<FunctionStatus> c9 = new ArrayList<>(1);
        c9.add(new FunctionStatus("9"));
        DeviceFunction _c9 = new DeviceFunction("14", "9", c9);
        deviceFunctions.add(_c9);

        List<FunctionStatus> ch = new ArrayList<>(1);
        ch.add(new FunctionStatus("--/-"));
        DeviceFunction _ch = new DeviceFunction("15", "--/-", ch);
        deviceFunctions.add(_ch);

        List<FunctionStatus> c0 = new ArrayList<>(1);
        c0.add(new FunctionStatus("0"));
        DeviceFunction _c0 = new DeviceFunction("16", "0", c0);
        deviceFunctions.add(_c0);

        List<FunctionStatus> cjmjt = new ArrayList<>(1);
        cjmjt.add(new FunctionStatus("节目交替"));
        DeviceFunction _cjmjt = new DeviceFunction("17", "节目交替", cjmjt);
        deviceFunctions.add(_cjmjt);

        List<FunctionStatus> zjh = new ArrayList<>(1);
        zjh.add(new FunctionStatus("交换"));
        DeviceFunction _cjh = new DeviceFunction("18", "交换", zjh);
        deviceFunctions.add(_cjh);

        List<FunctionStatus> chzh = new ArrayList<>(1);
        chzh.add(new FunctionStatus("画中画"));
        DeviceFunction _chzh = new DeviceFunction("19", "画中画", chzh);
        deviceFunctions.add(_chzh);

        List<FunctionStatus> czc = new ArrayList<>(1);
        czc.add(new FunctionStatus("正常"));
        DeviceFunction _czc = new DeviceFunction("20", "正常", czc);
        deviceFunctions.add(_czc);

        List<FunctionStatus> cxt = new ArrayList<>(1);
        cxt.add(new FunctionStatus("选台"));
        DeviceFunction _cxt = new DeviceFunction("21", "选台", cxt);
        deviceFunctions.add(_cxt);

        List<FunctionStatus> ctx = new ArrayList<>(1);
        ctx.add(new FunctionStatus("图像"));
        DeviceFunction _ctx = new DeviceFunction("22", "图像", ctx);
        deviceFunctions.add(_ctx);

        List<FunctionStatus> cch_ = new ArrayList<>(1);
        cch_.add(new FunctionStatus("CH-"));
        DeviceFunction _cch_ = new DeviceFunction("23", "CH-", cch_);
        deviceFunctions.add(_cch_);

        List<FunctionStatus> cchj = new ArrayList<>(1);
        cchj.add(new FunctionStatus("CH+-"));
        DeviceFunction _cchj = new DeviceFunction("24", "CH+-", cchj);
        deviceFunctions.add(_cchj);

        List<FunctionStatus> csy = new ArrayList<>(1);
        csy.add(new FunctionStatus("声音"));
        DeviceFunction _cst = new DeviceFunction("25", "声音", csy);
        deviceFunctions.add(_cst);

        List<FunctionStatus> cs = new ArrayList<>(1);
        cs.add(new FunctionStatus("上"));
        DeviceFunction _cs = new DeviceFunction("26", "上", cs);
        deviceFunctions.add(_cs);

        List<FunctionStatus> cx = new ArrayList<>(1);
        cx.add(new FunctionStatus("下"));
        DeviceFunction _cx = new DeviceFunction("27", "下", cx);
        deviceFunctions.add(_cx);

        List<FunctionStatus> cz = new ArrayList<>(1);
        cz.add(new FunctionStatus("左"));
        DeviceFunction _cz = new DeviceFunction("28", "左", cz);
        deviceFunctions.add(_cz);

        List<FunctionStatus> cy = new ArrayList<>(1);
        cy.add(new FunctionStatus("右"));
        DeviceFunction _cy = new DeviceFunction("29", "右", cy);
        deviceFunctions.add(_cy);

        List<FunctionStatus> ccd = new ArrayList<>(1);
        ccd.add(new FunctionStatus("菜单"));
        DeviceFunction _ccd = new DeviceFunction("30", "菜单", ccd);
        deviceFunctions.add(_ccd);

        List<FunctionStatus> cpj = new ArrayList<>(1);
        cpj.add(new FunctionStatus("屏显"));
        DeviceFunction _cpj = new DeviceFunction("31", "屏显", cpj);
        deviceFunctions.add(_cpj);

        List<FunctionStatus> cdssp = new ArrayList<>(1);
        cdssp.add(new FunctionStatus("电视/视频"));
        DeviceFunction _cdssp = new DeviceFunction("32", "电视/视频", cdssp);
        deviceFunctions.add(_cdssp);

        List<FunctionStatus> cwc = new ArrayList<>(1);
        cwc.add(new FunctionStatus("完成"));
        DeviceFunction _cwc = new DeviceFunction("33", "完成", cwc);
        deviceFunctions.add(_cwc);

        List<FunctionStatus> cyld = new ArrayList<>(1);
        cyld.add(new FunctionStatus("音量+"));
        DeviceFunction _cyld = new DeviceFunction("34", "音量+", cyld);
        deviceFunctions.add(_cyld);

        List<FunctionStatus> cylx = new ArrayList<>(1);
        cylx.add(new FunctionStatus("音量-"));
        DeviceFunction _cylx = new DeviceFunction("35", "音量-", cylx);
        deviceFunctions.add(_cylx);

        List<FunctionStatus> cpdd = new ArrayList<>(1);
        cpdd.add(new FunctionStatus("频道+"));
        DeviceFunction _cpdd = new DeviceFunction("36", "频道+", cpdd);
        deviceFunctions.add(_cpdd);

        List<FunctionStatus> cpdx = new ArrayList<>(1);
        cpdx.add(new FunctionStatus("频道-"));
        DeviceFunction _cpdx = new DeviceFunction("37", "频道-", cpdx);
        deviceFunctions.add(_cpdx);

        List<FunctionStatus> cjy = new ArrayList<>(1);
        cjy.add(new FunctionStatus("静音"));
        DeviceFunction _cjy = new DeviceFunction("38", "静音", cjy);
        deviceFunctions.add(_cjy);

        return deviceFunctions;
    }

}