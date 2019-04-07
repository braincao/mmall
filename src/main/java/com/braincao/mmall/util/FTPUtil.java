package com.braincao.mmall.util;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * 用于上传文件到ftp服务器的工具
 */
public class FTPUtil {

    private static final Logger logger = LoggerFactory.getLogger(FTPUtil.class);

    private static String ftpIp = PropertiesUtil.getProperty("ftp.server.ip");
    private static int ftpPort = 21;
    private static String ftpUser = PropertiesUtil.getProperty("ftp.user");
    private static String ftpPass = PropertiesUtil.getProperty("ftp.pass");

    private FTPClient ftpClient;

    public static boolean uploadFile(List<File> fileList) throws IOException {
        FTPUtil ftpUtil = new FTPUtil();
        logger.info("开始连接ftp服务器..");

        boolean result = ftpUtil.uploadFile("img", fileList);

        logger.info("文件上传ftp服务器结束，上传结果:{}", result);
        return result;
    }

    private boolean uploadFile(String remotePath, List<File> fileList) throws IOException {
        boolean uploadResult = true;
        FileInputStream fis = null;
        //连接ftp服务器
        if(connectServer(ftpIp, ftpPort, ftpUser, ftpPass)){
            try {
                //在ftp服务器上切换的remotePath目录下并设置相关参数
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();//设置被动模式

                //核心 向ftp服务器上传文件
                for(File fileItem: fileList){
                    fis = new FileInputStream(fileItem);
                    ftpClient.storeFile(fileItem.getName(),fis);
                }
            } catch (IOException e) {
                logger.error("上传文件异常",e);
                uploadResult = false;
            }finally {
                fis.close();
                ftpClient.disconnect();
            }
        }

        return uploadResult;
    }

    private boolean connectServer(String ip, int port, String user, String pass){
        boolean isSuccess = true;
        ftpClient = new FTPClient();

        try {
            ftpClient.connect(ip, port);
            isSuccess = ftpClient.login(user,pass);
        } catch (IOException e) {
            logger.error("连接ftp服务器异常", e);
        }

        return isSuccess;
    }
}
