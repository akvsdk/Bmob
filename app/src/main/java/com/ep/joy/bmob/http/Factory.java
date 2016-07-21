package com.ep.joy.bmob.http;


import com.ep.joy.bmob.bean.TYService;
import com.ep.joy.bmob.utils.Constants;

import java.util.HashMap;
import java.util.Map;


/**
 * author miekoz on 2016/3/21.
 * email  meikoz@126.com
 */
public class Factory {

    public static final TYService provideImgService() {
        return provideService(TYService.class);
    };

    private static Map<Class, Object> m_service = new HashMap();

    public static <T> T provideService(Class cls) {
        Object serv = m_service.get(cls);
        if (serv == null) {
            synchronized (cls) {
                serv = m_service.get(cls);
                if (serv == null) {
                    serv = HttpClient.getIns(Constants.BASE_URL).createService(cls);
                    m_service.put(cls, serv);
                }
            }
        }
        return (T) serv;
    }
}
