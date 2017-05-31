import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 测试FTP上传文件功能
 */
public class FTPTest {

    @Test
    public void testFTPClient() throws IOException {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect("36.7.172.10", 21);
            ftpClient.login("dmp", "b1fsjtx");
            String reply = ftpClient.getReplyString();
            System.out.println(reply);
            File srcFile = new File("C:/Users/jdshao/Desktop/ITM/bihe_test.txt");
            FileInputStream fis = new FileInputStream(srcFile);
            // 设置上传目录
            ftpClient.changeWorkingDirectory("/data/acc_upload");
            // 设置为被动模式
            ftpClient.enterLocalPassiveMode();
            /*ftpClient.setBufferSize(1024);
            ftpClient.setControlEncoding("utf-8");
            // 设置文件类型（二进制）
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);*/
            Boolean result = ftpClient.storeFile("jdshaotest.txt", fis);
            System.out.println(result);
            System.out.println(ftpClient.getReplyString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean upload(String ftpUrl,String userName,int port, String password,String directory,String srcFileName,String destName) throws IOException {

        FTPClient ftpClient = new FTPClient();
        FileInputStream fis = null;
        boolean result = false;
        try {
            ftpClient.connect(ftpUrl,port);
            ftpClient.login(userName, password);
            ftpClient.enterLocalPassiveMode();
            File srcFile = new File(srcFileName);
            fis = new FileInputStream(srcFile);
            // 设置上传目录
            ftpClient.changeWorkingDirectory(directory);
            ftpClient.setBufferSize(1024);
            ftpClient.setControlEncoding("utf-8");
            // 设置文件类型（二进制）
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            result = ftpClient.storeFile(destName, fis);
            return result;
        } catch(NumberFormatException e){
            System.out.println("FTP端口配置错误:不是数字:" );
            throw e;
        } catch(FileNotFoundException e){
            throw new FileNotFoundException();
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        } finally {
            IOUtils.closeQuietly(fis);
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                throw new RuntimeException("关闭FTP连接发生异常！", e);
            }
        }
    }
}
