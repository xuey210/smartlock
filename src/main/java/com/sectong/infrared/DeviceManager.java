package com.sectong.infrared;

import com.sectong.utils.SerializationObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by xueyong on 16/9/8.
 * mobileeasy.
 */

public abstract class DeviceManager {

    private static final Logger logger = LoggerFactory.getLogger(DeviceManager.class);
    private static final String FILEPATH = "/alidata/server/infrared/server/";
    protected static final String STBPATH = "/alidata/server/infrared/db/STB.txt";
    protected static final String DVDPATH = "/alidata/server/infrared/db/DVD.txt";
    protected static final String FANPATH = "/alidata/server/infrared/db/FAN.txt";
    protected static final String ACLPATH = "/alidata/server/infrared/db/ACL.txt";
    protected static final String IPTVPATH = "/alidata/server/infrared/db/IPTV.txt";
//    private static final String FILEPATH = "/Users/xueyong/Downloads/qmys/server/";

    private String brands;
    private String device;
    private String models;

    private AccessConnection accessConnection;

    public DeviceManager() {

    }

    public abstract int getIndex(ModelEntity modelEntity, FormulaParam formulaParam);

    public abstract String[] createTestCode(ModelEntity modelEntity, FormatEntity formatEntity);

    public abstract List<DeviceFunction> allStatus(String model);

    public DeviceManager init(String brands, String device) {
        this.device = device;
        this.brands = brands;
        accessConnection = new AccessConnection();
        return this;
    }

    public String getModels() {
        return this.models;
    }

    public DeviceManager queryModels() {
        Assert.notNull(device);
        Assert.notNull(brands);
        logger.info("device:{},brands:{}", new Object[]{device, brands});
        int device_id = DeviceEnum.getIdByName(device);
        String models = "";
        String sql = "SELECT [model_list] FROM [brands] where brandname = '" + brands + "' and device_id = " + device_id;
        ResultSet rs = null;
        try {
            rs = accessConnection.getResult(sql);
            while (rs.next()) {
                models = rs.getString(1);
                this.models = models;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return this;
    }

    public ModelEntity getKeyFileAndSquency(String model) {
        Assert.notNull(model);
        logger.info("model:{} getting keyfile and squency", new Object[]{model});
        String sql = "select [m_keyfile],[m_key_squency],[m_format_id] from [model] where m_code = '" + model + "' and device_id =" + DeviceEnum.getIdByName(device);
        ResultSet rs = null;
        String keylife = "", keysquency = "", formatId = "";
        try {
            rs = accessConnection.getResult(sql);
            while (rs.next()) {
                keylife = rs.getString(1);
                keysquency = rs.getString(2);
                formatId = rs.getString(3);
                ModelEntity modelEntity = new ModelEntity(keylife, keysquency);
                modelEntity.setFormatId(formatId);
                return modelEntity;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    public FormatEntity getFormat(String fid) {
        Assert.notNull(fid);
        logger.info("fid:{} is getting format_string and c3rv", new Object[]{fid});
        String sql = "select [format_string],[c3rv] from [formats] where fid = '" + fid + "' and device_id =" + DeviceEnum.getIdByName(device);
        ResultSet rs = null;
        String formatString, c3rv;
        try {
            rs = accessConnection.getResult(sql);
            while (rs.next()) {
                formatString = rs.getString(1);
                c3rv = rs.getString(2);
                FormatEntity formatEntity = new FormatEntity();
                formatEntity.setC3rv(c3rv);
                formatEntity.setFormatString(formatString);
                logger.info("formatEntity:{}", new Object[]{SerializationObject.serialize(formatEntity)});
                return formatEntity;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    protected String readFile(String filename,int index) {
        String code = "";
        File file = new File(FILEPATH + device + "/codes/" + filename + ".txt");
        LineIterator it = null;
        try {
            it = FileUtils.lineIterator(file, "UTF-8");
            logger.info("readFile path :{}", new Object[]{file.getAbsolutePath()});
            int count = 1;
            while (it.hasNext()) {
                code = it.nextLine();
                if (count == index) {
                    break;
                }
                count++;
            }
            logger.info("readFile code : {}", new Object[]{code});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            it.close();
        }
        return code;
    }

    public String getFinalCode(int index, ModelEntity modelEntity, FormatEntity formatEntity) {
        String m = readFile(modelEntity.getKeyFile(), index);
        if (StringUtils.isBlank(m)) {
            return "";
        }
        String m2 = "";
        try {
            String formatString = formatEntity.getFormatString();
            Assert.notNull(formatEntity);
            String c3rv = formatEntity.getC3rv();
            logger.info("c3rv:{}", new Object[]{c3rv});
            logger.info("befor format string is :{}", new Object[]{formatString});

            String[] fary = formatString.split(",");
            if (fary.length < 10) {
                logger.info("formatstring length is less than 10 , so return m:{}", new Object[]{m});
                return m;
            }
            String[] c3rvary = c3rv.split("\\|");
            Queue<String> stringQueue = new ConcurrentLinkedQueue<>();
            for (String formats : m.split(",")) {
                stringQueue.offer(formats);
            }
            for (String s : c3rvary) {
                String[] _place = s.split("\\-");
                String _index = _place[0];
                String bit = _place[1];
                for (int i = 0, j = Integer.valueOf(_index); i < Integer.valueOf(bit); i++, j++) {
                    fary[j] = stringQueue.poll();
                }
            }
            m2 = StringUtils.join(fary, ",");
            logger.info("after format string is :{}", new Object[]{m2});
        } catch (Exception e) {
            e.printStackTrace();

        }
        return m2;
    }

    protected List<DeviceFunction> getStract(String filePath,int length) {
        List<DeviceFunction> deviceFunctions = new ArrayList<>(length);
        File file = new File(filePath);
        LineIterator it;
        try {
            it = FileUtils.lineIterator(file, "GB18030");
            int i = 1;
            while (it.hasNext()) {
                String code = it.nextLine();
                List<FunctionStatus> power = new ArrayList<>(1);
                power.add(new FunctionStatus(code));
                DeviceFunction _power = new DeviceFunction(String.valueOf(i), code, power);
                deviceFunctions.add(_power);
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return deviceFunctions;
    }

    protected abstract List<DeviceFunction> allStatus(String model, String path, int length);
}
