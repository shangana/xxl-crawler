package com.xuxueli.crawler.util;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * jsoup tool
 *
 * @author xuxueli 2015-05-14 22:44:43
 */
public class JsoupUtil {
    private static Logger logger = LoggerFactory.getLogger(JsoupUtil.class);

    /**
     * 加载解析页面
     *
     * @param url		：加载URL
     * @param paramMap	：请求参数
     * @param cookieMap	：请求cookie
     * @param ifPost	：是否使用post请求
     *
     * @return
     */
    public static Document load(String url, Map<String, String> paramMap, Map<String, String> cookieMap, boolean ifPost) {
        if (!UrlUtil.isUrl(url)) {
            return null;
        }
        try {
            // 请求设置
            Connection conn = Jsoup.connect(url);
            conn.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36");
            if (paramMap != null && !paramMap.isEmpty()) {
                conn.data(paramMap);
            }
            if (cookieMap != null && !cookieMap.isEmpty()) {
                conn.cookies(cookieMap);
            }
            conn.timeout(5000);

            // 发出请求
            Document html = null;
            if (ifPost) {
                html = conn.post();
            } else {
                html = conn.get();
            }
            return html;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取页面上所有超链接地址 （<a>标签的href值）
     *
     * @param html  页面文档
     * @return
     */
    public static Set<String> findLinks(Document html) {

        if (html == null) {
            return null;
        }

        // element
        /**
         *
         * Elements resultSelect = html.select(tagName);	// 选择器方式
         * Element resultId = html.getElementById(tagName);	// 元素ID方式
         * Elements resultClass = html.getElementsByClass(tagName);	// ClassName方式
         * Elements resultTag = html.getElementsByTag(tagName);	// html标签方式 "body"
         *
         */
        Elements hrefElements = html.select("a[href]");

        // 抽取数据
        Set<String> links = new HashSet<String>();
        if (hrefElements!=null && hrefElements.size() > 0) {
            for (Element item : hrefElements) {
                String href = item.attr("abs:href");    // href、abs:href
                if (UrlUtil.isUrl(href)) {
                    links.add(href);
                }
            }
        }
        return links;
    }

    /**
     * 获取页面上所有超链接地址
     *
     * @param link  待爬行的页面链接
     * @return
     */
    public static Set<String> findLinks(String link) {
        Set<String> links = findLinks(load(link, null, null, false));
        return links;
    }

}
