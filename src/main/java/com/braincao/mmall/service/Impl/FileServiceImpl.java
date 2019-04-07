package com.braincao.mmall.service.Impl;

import com.braincao.mmall.service.IFileService;
import com.braincao.mmall.util.FTPUtil;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 用于文件上传的service业务，也可以理解为文件上传的工具
 */
@Service("iFileService")
public class FileServiceImpl implements IFileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    //用户A B都上传abc.jpg，不发生覆盖，因此需要宇宙无敌的UUID来命名fileUploadName
    public String upload(MultipartFile file, String path){
        String fileName = file.getOriginalFilename();
        //获取扩展名 abc.jpg-->jpg
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);
        String fileUploadName = UUID.randomUUID().toString() + "." + fileExtensionName;
        logger.info("开始上传文件,上传文件的文件名:{}, 新文件名:{}, 上传的路径:{}",
                fileName, fileUploadName, path);

        File fileDir = new File(path);
        if(!fileDir.exists()){
            //创建文件夹时先设置可写权限
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }

        //spring mvc最终上传的文件路径
        File targetFile = new File(path,fileUploadName);
        try {
            file.transferTo(targetFile);
            //spring mvc 上传文件成功

            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            //将targetFile上传到我们ftp服务器上

            //文件上传到ftp之后，删除upload中的该文件(即删除tomcat中的文件)
            targetFile.delete();
        } catch (IOException e) {
            logger.error("上传文件异常", e);
            return null;
        }

        return targetFile.getName();
    }

}
