//package com.reven.webservice;
//
//import java.io.IOException;
//import java.net.URL;
//
//import javax.xml.namespace.QName;
//import javax.xml.ws.Service;
//
//public class MobileClient {
//    public static void main(String[] args) throws IOException {
//        // 创建WSDL文件的URL
//        URL wsdlDocumentLocation = new URL("http://ws.webxml.com.cn/WebServices/MobileCodeWS.asmx?wsdl");
//        // 创建服务名称
//        // 1.namespaceURI - 命名空间地址
//        // 2.localPart - 服务视图名
//        QName serviceName = new QName("http://WebXml.com.cn/", "MobileCodeWS");
//        Service service = Service.create(wsdlDocumentLocation, serviceName);
//        // 获取服务实现类
//        MobileCodeWSSoap mobileCodeWSSoap = service.getPort(MobileCodeWSSoap.class);
//        // 调用方法
//        String message = mobileCodeWSSoap.getMobileCodeInfo("XXXXXXX", null);
//        System.out.println(message);
//    }
//}
