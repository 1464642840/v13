package com.qf.v13centerweb.control;

import com.github.tobato.fastdfs.FdfsClientConfig;
import com.github.tobato.fastdfs.FdfsClientConstants;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.qf.v13.common.pojo.ResultBean;
import com.qf.v13.common.pojo.WangResultBean;
import com.qf.v13.common.utils.QCloudUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * @author blxf
 * @Date ${Date}
 */
@Controller
@RequestMapping("file")
public class CommonController {

    @Value("${img.server}")
    private String imageServer;
    @Autowired
    private FastFileStorageClient client;

    @RequestMapping("/")
    public String toIndex() {
        return "index";
    }

    @PostMapping("upload")
    @ResponseBody
    public ResultBean uploadFile(MultipartFile files) throws IOException {
      /*  String fileName =files.getOriginalFilename();
        InputStream inputStream  = files.getInputStream();
        String extertn =fileName.substring(fileName.lastIndexOf('.')+1);
        StorePath storePath = client.uploadFile(inputStream, files.getSize(), extertn, null);
        String fullPath = storePath.getFullPath();
        String path = new StringBuilder(imageServer).append(fullPath).toString();*/
        File localFile = null;
        localFile = File.createTempFile("temp", null);
        files.transferTo(localFile);
        String path = QCloudUtil.uploadFile(localFile, files.getOriginalFilename());
        localFile.delete();
        System.out.println(path);
        return new ResultBean("200", path);
    }

    @PostMapping("uploads")
    @ResponseBody
    public WangResultBean uploadFiles(MultipartFile[] files) throws IOException {
        String[] s = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            String fileName = files[i].getOriginalFilename();
            InputStream inputStream = files[i].getInputStream();
            String extertn = fileName.substring(fileName.lastIndexOf('.') + 1);
            StorePath storePath = client.uploadFile(inputStream, files[i].getSize(), extertn, null);
            String fullPath = storePath.getFullPath();
            String path = new StringBuilder(imageServer).append(fullPath).toString();
            s[i] = path;
            System.out.println(path);
        }
        return new WangResultBean("0", s);

    }
}
