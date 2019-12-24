package idgenerator;

import annotation.NumbGenConfigAnnotation;
import idgenerator.mode.NumberGeneratorMode;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 生成业务编码通用类
 */
@Component
public class NumberGeneratorService {

    /**
     * 注册业务编码
     * @param configMap
     */
    public void registerNumberConfig(Map<String,Object> configMap){
      if(configMap!=null && !configMap.isEmpty()){
          for(Map.Entry<String,Object> entry:configMap.entrySet()){
              NumberGeneratorMode configValue= parseAnnotation(entry.getValue());
          }
      }
    }

    /**
     * 解析注解上的值
     * @param obj
     * @return
     */
    private NumberGeneratorMode parseAnnotation(Object obj){
        NumbGenConfigAnnotation configAnnotation= obj.getClass().getAnnotation(NumbGenConfigAnnotation.class);
        NumberGeneratorMode mode=new NumberGeneratorMode();
        mode.setCodeFormat(configAnnotation.codeFormat());
        mode.setLeftPadChar(configAnnotation.leftPadChar());
        mode.setNumberCode(configAnnotation.numberCode());
        mode.setNumberDesc(configAnnotation.numberDesc());
        mode.setStartSequence(configAnnotation.startSequence());
        mode.setSequenceLength(configAnnotation.sequenceLength());
        mode.setNumberFormat(configAnnotation.numberFormat());
        mode.setStep(configAnnotation.step());

        return mode;
    }
}
