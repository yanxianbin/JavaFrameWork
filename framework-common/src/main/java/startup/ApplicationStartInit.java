package startup;

import annotation.NumbGenConfigAnnotation;
import com.fasterxml.jackson.databind.ObjectMapper;
import idgenerator.NumberGeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 应用启动后
 */
@Component
public class ApplicationStartInit implements ApplicationContextAware, ApplicationRunner {

    private static final Logger logger= LoggerFactory.getLogger(ApplicationStartInit.class);

    private final Map<String,Object> numberConfigMap=new ConcurrentHashMap<>();

    @Autowired
    private NumberGeneratorService numberGeneratorService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        initNumberGenConfig(applicationContext);
    }

    /**
     * 注册业务编码
     * @param context
     */
    private void initNumberGenConfig(ApplicationContext context){
        Map<String, Object> configs=context.getBeansWithAnnotation(NumbGenConfigAnnotation.class);
        for(Map.Entry<String,Object> entry:configs.entrySet()){
            numberConfigMap.put(entry.getKey(),entry.getValue());
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("ApplicationStartInit run registerNumberConfig numberGenMap:{}");
        numberGeneratorService.registerNumberConfig(numberConfigMap);
    }
}
