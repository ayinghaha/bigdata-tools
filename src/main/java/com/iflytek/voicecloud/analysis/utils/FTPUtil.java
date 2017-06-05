package com.iflytek.voicecloud.analysis.utils;

import com.iflytek.voicecloud.itm.dto.Message;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * FTP 上传工具类
 */
public class FTPUtil {

    /**
     * FTP服务器地址
     */
    private static String FTPIP = "36.7.172.10";

    /**
     * 用户名
     */
    private static String userName = "dmp";

    /**
     * 密码
     */
    private static String password = "b1fsjtx";

    /**
     * FTP目标服务器上传路径
     */
    private static String targetPath = "/data/aac_upload";

    public static Message uploadFileToFTPServer(File sourceFile, String targetName) {

        FTPClient ftpClient = new FTPClient();

        try {
            ftpClient.connect(FTPIP, 21);
            ftpClient.login(userName, password);
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                ftpClient.disconnect();
                return new Message(-1, "登录失败，用户名或密码不正确");
            }

            FileInputStream fileInputStream = new FileInputStream(sourceFile);
            // 设置上传目录
            ftpClient.changeWorkingDirectory(targetPath);
            // 设置为被动模式
            ftpClient.enterLocalPassiveMode();
            // 设置编码
            ftpClient.setControlEncoding("UTF-8");
            Boolean result = ftpClient.storeFile(new String(targetName.getBytes("UTF-8"),"iso-8859-1"), fileInputStream);
            if (!result) {
                return new Message(-1, "上传失败");
            } else {
                return new Message(1, "上传成功");
            }
        } catch (IOException e) {
            return new Message(-1, "上传文件失败");
        }
    }


}
