import com.iflytek.voicecloud.itm.common.DataSourceContextHolder;
import com.iflytek.voicecloud.itm.dao.analysis.OverAllDao;
import com.iflytek.voicecloud.itm.dao.config.TagDao;
import com.iflytek.voicecloud.itm.entity.analysis.OverAllData;
import com.iflytek.voicecloud.itm.entity.config.Tag;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 测试数据源
 */

@RunWith(SpringJUnit4ClassRunner.class)
// 使用Springtest框架
@ContextConfiguration("classpath:spring-mybatis.xml")
public class DataSourceSwichTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceSwichTest.class);

    /*@Autowired
    // 注入
    private TagDao tagDao;

    @Autowired
    private OverAllDao overAllDailyDao;*/

    @Test
    public void findByItmConfig() {

        /*DataSourceContextHolder.setDbType("itmConfig");
        LOGGER.info("切换数据源至 itm-config");

        Tag tag = tagDao.getTagById(24);
        System.out.println(tag);*/
    }

    @Test
    public void swichAnalysis() {

        /*DataSourceContextHolder.setDbType("itmAnalysis");

        OverAllData overAllDaily = overAllDailyDao.getDailyByid(526);
        System.out.println(overAllDaily);*/

    }

}
