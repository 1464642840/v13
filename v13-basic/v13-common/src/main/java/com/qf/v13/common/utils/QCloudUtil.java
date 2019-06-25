package com.qf.v13.common.utils;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 * @author blxf
 * @Date ${Date}
 */
public class QCloudUtil {

    private final static Long appID = 1252683084L;
    private final static String secretId = "AKIDGchZGsaJ7eute8DCiyylOwHWWHn6tbR0";
    private final static String secretKey = "bokOp0vc6ScU4PTGpqhKgkW1GG2xiBNI";
    private final static String bucketName = "blxf-1252683084";
    private final static String region = "ap-shenzhen-fsi";

    public static String uploadFile(File file, String key) {
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        ClientConfig clientConfig = new ClientConfig(new Region(region));
        COSClient cosClient = new COSClient(cred, clientConfig);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);
        cosClient.putObject(putObjectRequest);
        cosClient.shutdown();
        Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
        URL url = cosClient.generatePresignedUrl(bucketName, key, expiration);
        return url.toString();
    }


}
