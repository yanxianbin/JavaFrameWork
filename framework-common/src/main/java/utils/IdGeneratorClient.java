package utils;

import idgenerator.IdGeneratorService;

/**
 * Id 工具类
 */
public class IdGeneratorClient {

    private static IdGeneratorService idGeneratorService;

    /**
     * 获取ID
     * @return
     */
    public static Long getId(){
        return getIdGeneratorService().nextId();
    }

    private static IdGeneratorService getIdGeneratorService(){
        if(idGeneratorService==null){
            idGeneratorService=SpringUtils.getBean(IdGeneratorService.class);
        }
        return idGeneratorService;
    }
}
