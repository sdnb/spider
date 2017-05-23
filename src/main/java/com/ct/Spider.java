package com.ct;

import com.util.HttpClientUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chentao on 2017/5/23 0023.
 */
public class Spider implements Runnable{

    private SpiderConfig spiderConfig;

    public Spider(SpiderConfig spiderConfig) {
        this.spiderConfig = spiderConfig;
    }


    /**
     * 拉取网页
     * @param offset
     * @return
     */
    public String fetchPage(int offset) {
        String url = spiderConfig.getUrl();
        HttpClientUtil.doGet(url, buildParams(offset));
        return "";
    }


    public Map<String, String> buildParams(int offset) {
        Map<String, String> params = new HashMap<>();
        params.put("sortBy", "Relevance");
        params.put("ft", "china");
        params.put("rpp", "20");
        params.put("offset", String.valueOf(offset));
        params.put("pos", String.valueOf(2));
        return params;
    }


    /**
     * 对页面进行处理
     * @param page
     * @param filter
     * @return
     */
    public Map<String, String> getNameAndViewUrl(String page, Filter filter){
        return null;
    }


    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String str = "Hello,World! in Java.";
        Pattern pattern = Pattern.compile("W(or)(ld!)");
        Matcher matcher = pattern.matcher(str);
        while(matcher.find()){
            System.out.println("Group 0:"+matcher.group(0));//得到第0组——整个匹配
            System.out.println("Group 1:"+matcher.group(1));//得到第一组匹配——与(or)匹配的
            System.out.println("Group 2:"+matcher.group(2));//得到第二组匹配——与(ld!)匹配的，组也就是子表达式
            System.out.println("Start 0:"+matcher.start(0)+" End 0:"+matcher.end(0));//总匹配的索引
            System.out.println("Start 1:"+matcher.start(1)+" End 1:"+matcher.end(1));//第一组匹配的索引
            System.out.println("Start 2:"+matcher.start(2)+" End 2:"+matcher.end(2));//第二组匹配的索引
            System.out.println(str.substring(matcher.start(0),matcher.end(1)));//从总匹配开始索引到第1组匹配的结束索引之间子串——Wor
        }
    }

    @Override
    public void run() {

    }
}
