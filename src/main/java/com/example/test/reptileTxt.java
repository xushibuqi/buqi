package com.example.test;

import com.example.DemoApplication;
import org.jsoup.Jsoup;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class reptileTxt {


    @Autowired
    ThreadPoolTaskExecutor socketThreadPool;


    public static void splitMethod(int i, reptileTxt reptile) {
        switch (i) {
            case 1:
                reptile.crawlAndSaveTextByPT();
                break;
            case 2:
                reptile.crawlAndSaveText();
                break;
            case 3:

                break;
            default:
                break;
        }


    }

    public TreeMap<Integer, String> getCatalogueByPT() {
        TreeMap<Integer, String> catalogueMap = new TreeMap<>();
        try {

            // 目录
            String url = "https://www.piaotian.com/html/11/11570/index.html";
            Document doc = Jsoup.connect(url).get();
            Elements brTags = doc.select("li");
            int i = 0;
            for (Element brTag : brTags) {
                if (brTag.parent() != null && brTag.parent().tagName().equals("ul")) {
                    Elements aElement = brTag.select("a");
                    String href = aElement.attr("href");
                    if (!StringUtil.isBlank(href)) {
                        i++;
                        catalogueMap.put(i, href);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return catalogueMap;

    }


    public void crawlAndSaveTextByPT() {
        int maxCount = 1130;
        final String baseUrl = "https://www.piaotian.com/html/11/11570/";
        CountDownLatch cd = new CountDownLatch(maxCount);
        TreeMap<Integer, String> map = new TreeMap();
        TreeMap<Integer, String> catalogueMap = getCatalogueByPT();
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i < maxCount; i++) {
            final int pageNumber = i;

            socketThreadPool.execute(() -> {
                String s=null;
                boolean success = false;
                StringBuilder textContent = new StringBuilder();
                while (!success) {
                    try {
                        String middle = "";//中间值 去重
                        Document doc = Jsoup.connect(baseUrl + catalogueMap.get(pageNumber)).get();

                        // 标题
                        Element h1 = doc.select("H1").first();
                        String title = h1.text();

                        Elements brTags = doc.select("br");

                        textContent.append(title);
                        textContent.append("\n");
                        for (Element brTag : brTags) {
                            if (brTag.parent().tagName().equals("body")) {
                                textContent.append(brTag.nextSibling().toString());
                                textContent.append("\n");
                            }
                        }
                        // 去除多余的换行符
                        s= textContent.toString().replaceAll("<br>", "").replaceAll("&nbsp;&nbsp;&nbsp;&nbsp;", "").replaceAll("\n{2,}", "\n");
                        success = true; // 成功获取内容
                    } catch (IOException e) {
                        list.add(pageNumber);
                        System.out.println("第" + pageNumber + "页提取失败: " + e.getMessage());
                    }
                }
                if (success) {
                    cd.countDown();
                    map.put(pageNumber, s);
                    System.out.println("第" + pageNumber + "页提取完毕");
                }

            });
        }

        try {
            cd.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //    continuText(baseUrl, list, map);
        String osName = System.getProperty("os.name").toLowerCase();
        String fileUrl;
        if (osName.contains("win")) fileUrl = "D://诡秘地海.txt";
        else fileUrl = "/usr";
        try (FileWriter writer = new FileWriter(fileUrl)) {
            for (String value : map.values()) {
                writer.write(value);
            }
            System.out.println("文本提取完成并保存至文件。");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void crawlAndSaveText() {
        int maxCount = 44;
        final String baseUrl = "https://louzhu.info/p/146554/";
        CountDownLatch cd = new CountDownLatch(maxCount);
        TreeMap<Integer, String> map = new TreeMap();
        AtomicReference<String> title = new AtomicReference<>("default");
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= maxCount; i++) {
            final int pageNumber = i;
            socketThreadPool.execute(() -> {
                try {
                    String middle = "";//中间值 去重
                    Document doc = Jsoup.connect(baseUrl + pageNumber + ".html").get();
                    title.set(doc.title());
                    StringBuilder textContent = new StringBuilder();
                    Elements brTags = doc.select("br");
                    for (Element brTag : brTags) {
                        if ("content mt_10".equals(brTag.parent().attr("class")) || brTag.parent().tagName().equals("p")) {
                            Node previousNode = brTag.previousSibling();
                            if (previousNode instanceof TextNode) {
                                // 判断前一个节点是否为文本节点
                                String text = ((TextNode) previousNode).text().trim();
                                if (!text.equals(middle)) {
                                    textContent.append(text);
                                    textContent.append("\n");
                                }
                            }
                        }
                        if ("content mt_10".equals(brTag.parent().attr("class")) || brTag.parent().tagName().equals("p")) {
                            middle = brTag.nextSibling().toString().replace("<br>", "").trim();
                            textContent.append(middle);
                            textContent.append("\n");
                        }
                    }
                    // 去除多余的换行符
                    String s = textContent.toString().replaceAll("\n{2,}", "\n");
                    map.put(pageNumber, s);
                    System.out.println("第" + pageNumber + "页提取完毕");
                } catch (IOException e) {
                    list.add(pageNumber);
                    System.out.println("第" + pageNumber + "页提取失败: " + e.getMessage());
                } finally {
                    cd.countDown();
                }
            });
        }

        try {
            cd.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //    continuText(baseUrl, list, map);
        String osName = System.getProperty("os.name").toLowerCase();
        String fileUrl;
        if (osName.contains("win")) fileUrl = "D://" + title + ".txt";
        else fileUrl = "/usr";
        try (FileWriter writer = new FileWriter(fileUrl)) {
            for (String value : map.values()) {
                writer.write(value);
            }
            System.out.println("文本提取完成并保存至文件。");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void continuText(String baseUrl, List<Integer> list, TreeMap<Integer, String> map) {
        for (Integer integer : list) {
            Document doc = null;
            try {
                doc = Jsoup.connect(baseUrl + integer + ".html").get();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            StringBuilder textContent = new StringBuilder();
            Elements brTags = doc.select("br");

            for (Element brTag : brTags) {
                textContent.append(brTag.nextSibling().toString().trim());
                textContent.append("\n");
            }
            System.out.println("第" + integer + "页重新提取完毕");
            map.put(integer, textContent.toString());
        }


    }


    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(DemoApplication.class); // 假设ApplicationConfig是你的Spring配置类
        reptileTxt reptile = context.getBean(reptileTxt.class);
//   2  天涯 1  飘天
        splitMethod(1, reptile);

    }
}
