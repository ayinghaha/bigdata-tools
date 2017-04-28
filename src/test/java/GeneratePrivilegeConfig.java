import com.iflytek.voicecloud.itm.entity.Privilege;
import com.iflytek.voicecloud.itm.utils.JsonUtil;
import org.junit.Test;

import java.io.*;

/**
 * 生成当前页面权限配置
 */
public class GeneratePrivilegeConfig {


    /**
     * 生成权限树形结构
     */
    @Test
    public void generateTree() throws Exception {

        // 代码管理器
        Privilege codeManager = new Privilege("代码管理器", 2, -1);

        // 数据来源
        Privilege dataSource = new Privilege("数据来源", 0, -1);
        dataSource.setChildren(new Privilege[]{
            new Privilege("数据源接入", 4, -1),
            new Privilege("web数据", 8, -1)
        });

        // 受众管理
        Privilege audienceManager = new Privilege("受众管理", 0, -1);
        audienceManager.setChildren(new Privilege[]{
            new Privilege("id图谱", 16, -1),
            new Privilege("id打通率", 32, -1),
            new Privilege("标签统计", 64, -1),
            new Privilege("标签整合", 128, -1),
            new Privilege("人群包管理", 256, -1),
            new Privilege("人群洞察", 512, -1),
        });

        // 营销平台
        Privilege marketPlatform = new Privilege("营销平台", 1024, -1);

        // 营销分析
        Privilege audienceAnalysis = new Privilege("营销分析", 0, -1);
        audienceAnalysis.setChildren(new Privilege[]{
            new Privilege("目标管理", 2048, -1),
            new Privilege("广告效果分析", 4096, -1),
            new Privilege("事件分析", 8192, -1),
            new Privilege("页面分析", 16384, -1),
            new Privilege("访客深度", 32768, -1)
        });

        Privilege root = new Privilege("全部", 0, -1);
        root.setChildren(new Privilege[]{codeManager, dataSource, audienceManager, marketPlatform, audienceAnalysis});

        // 序列化对象，得到配置
        File directory = new File("");// 参数为空
        String courseFile = directory.getCanonicalPath() + "/src/main/java/com/iflytek/voicecloud/itm/config/privilege.obj";
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(courseFile));
        out.writeObject(root);    //写入对象
        out.close();
    }

    @Test
    public void testGetSerizeObject() throws Exception {
        File directory = new File("");// 参数为空
        String courseFile = directory.getCanonicalPath() + "/src/main/java/com/iflytek/voicecloud/itm/config/privilege.obj";
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(courseFile));
        Privilege root = (Privilege) in.readObject();
        System.out.println(JsonUtil.ObjectToJson(root));
    }


}
