package main;

import com.client.ServiceSend;
import com.parse.IPConfigMap;

public class ProductTestMain {

    public static void main(String[] args) {
        ServiceSend sc = new ServiceSend();
        //获取应用IP
        String esbapp1 = IPConfigMap.getInstance().getIPValue("esbapp1");
        String esbapp2 = IPConfigMap.getInstance().getIPValue("esbapp2");
        String esbapp3 = IPConfigMap.getInstance().getIPValue("esbapp3");
        String esbapp4 = IPConfigMap.getInstance().getIPValue("esbapp4");
        String esbF5 = IPConfigMap.getInstance().getIPValue("f5");
        if (esbapp1 != null && !"".equals(esbapp1.trim()))
            sc.initService(esbapp1);
        if (esbapp2 != null && !"".equals(esbapp2.trim()))
            sc.initService(esbapp2);
        if (esbapp3 != null && !"".equals(esbapp3.trim()))
            sc.initService(esbapp3);
        if (esbapp4 != null && !"".equals(esbapp4.trim()))
            sc.initService(esbapp4);
        if (esbF5 != null && !"".equals(esbF5.trim()))
            sc.initService(esbF5);
    }

}
