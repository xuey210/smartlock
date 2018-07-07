package com.sectong.domain.mongomodle;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by xueyong on 16/7/11.
 * demo.
 */
public class VersionFactory {

    static final Logger LOGGER = LoggerFactory.getLogger(VersionFactory.class);

    static final String basePath = "/alidata/server/version/";
//    static final String basePath = "/Users/xueyong/Desktop/version/";

    public static UpdateModle versionV1_0() {
        UpdateModle updateModle = new UpdateModle("", "", "", "V1.0", 11, 522 * 11, "");
        return updateModle;
    }

    public static UpdateModle getV() {
        return versionV1_0_1();
    }

    public static UpdateModle versionV1_0_1() {
        UpdateModle updateModle = new UpdateModle("", "", "", "V1.0.1", 59, 0x758C, "");
        return updateModle;
    }

    private static UpdateModle versionV1_0_2() {
        UpdateModle updateModle = new UpdateModle("", "", "", "V1.0.2", 53, 0x69E0, "");
        return updateModle;
    }

    public static byte[] versionSegment(String vPath, Integer num) throws IOException {
        byte[] binCode = null;
        File file = new File(basePath + vPath + "/" + num + ".bin");
        try {
            binCode = IOUtils.toByteArray(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("file not found: {}", new Object[]{file == null ? "" : file.getPath()});
        }
        return binCode;
    }
}
