package com.ep.joy.bmob.utils;


import com.ep.joy.bmob.bean.ImageInfo;
import com.jiongbull.jlog.JLog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

/**
 * Created by Joy on 2015/8/27.
 */
public class JsoupTool {
    private static JsoupTool instance = null;

    private JsoupTool() {
        trustEveryone();
    }

    public static JsoupTool getInstance() {
        if (instance == null) {
            synchronized (JsoupTool.class) {
                instance = new JsoupTool();
            }
        }

        return instance;
    }

    public static void trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<ImageInfo> getContent(String url) {

        try {
            List<ImageInfo> imgList = new ArrayList<ImageInfo>();
            Document document = Jsoup.connect(url).timeout(10000).get();
            Elements ListDiv = document.getElementsByAttributeValue("class", "picture");
            JLog.e(ListDiv.html());
            for (Element e : ListDiv) {
                Elements links = e.getElementsByTag("a");
                for (Element el : links) {
                    String href = el.attr("abs:href");
                    String title = el.attr("alt");
                    ImageInfo img = new ImageInfo();
                    img.setImgurl(href);
                    img.setContent(title);
                    imgList.add(img);
                }
            }
            imgList.remove(0);
            return imgList;


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }




    public List<ImageInfo> getAllImages(String pageUrl) {
        try {
            Document doc = Jsoup.connect(pageUrl)
                    .timeout(10000)
                    .post();

            String title = doc.title();
            System.out.println(title);
            Elements urls = doc.select("img[src$=.jpg]");
            List<ImageInfo> imgList = new ArrayList<ImageInfo>();
            ImageInfo imageInfo;
            for (Element url : urls) {
                imageInfo = new ImageInfo();
                imageInfo.setContent(url.attr("title"));
                imageInfo.setImgurl(url.attr("src"));
                imgList.add(imageInfo);
            }
            return imgList;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
