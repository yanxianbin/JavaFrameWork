package com.utils;

import com.idgenerator.IdGeneratorService;
import com.idgenerator.NumberGeneratorService;

import java.util.List;

/**
 * Id和业务编码生成工具类
 */
public class IdGeneratorClient {

    private static IdGeneratorService idGeneratorService;

    private static NumberGeneratorService numberGeneratorService;

    /**
     * 获取ID
     * @return
     */
    public static Long getId(){
        return getIdGeneratorService().nextId();
    }

    /**
     * 获取业务编码
     * @param numberCode
     * @return
     */
    public static String getNumber(String numberCode){
       return getNumberGenService().nextNumber(numberCode);
    }

    /**
     * 获取业务编码集合，指定获取多少个
     * @param numberCode
     * @param size
     * @return
     */
    public static List<String> getNumberList(String numberCode,int size){
        return getNumberGenService().nextNumbers(numberCode,size);
    }

    private static IdGeneratorService getIdGeneratorService(){
        if(idGeneratorService==null){
            idGeneratorService=SpringUtils.getBean(IdGeneratorService.class);
        }
        return idGeneratorService;
    }

    private static NumberGeneratorService getNumberGenService(){
        if(numberGeneratorService==null){
            numberGeneratorService=SpringUtils.getBean(NumberGeneratorService.class);
        }
        return numberGeneratorService;
    }
}
