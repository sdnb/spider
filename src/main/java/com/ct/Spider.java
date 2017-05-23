package com.ct;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.util.HttpClientUtil;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

/**
 * Created by chentao on 2017/5/23 0023.
 */
public class Spider implements Callable<Map<String, Set<String>>>{

    private BlockingQueue<Map<String, Set<String>>> queue = new ArrayBlockingQueue<Map<String, Set<String>>>(20);

    private int page;
    private static final String url = "http://www.metmuseum.org/api/search";

    public Spider(int page) {
        this.page = page;
    }


    /**
     * 抓取网页
     * @param
     * @return
     */
    public String renderByPage() {
        return HttpClientUtil.doGet(url, buildParams());
    }


    public Map<String, String> buildParams() {
        Map<String, String> params = new HashMap<>();
        params.put("perPage", "20");
        params.put("q", "china");
//        params.put("rpp", "20");//q=china&perPage=20
//        params.put("offset", String.valueOf(offset));
        params.put("page", String.valueOf(page));
        return params;
    }


    /**
     * 对页面进行处理
     * @param
     *  <a href="/art/collection/search/486633">
    <span ng-bind-html="search.stripBrTags(searchResult.title)" class="ng-binding">China</span>
    <span class="search-results__result--subtitle ng-binding" ng-bind-html="searchResult.subTitle">Andrew Spence (American, born 1947) </span>
    </a>
     * 调用api得到图片路径 http://www.metmuseum.org/api/Collection/additionalImages?crdId=486633
     * @param
     * @return
     */
    public Map<String, Integer> getViewUrlsFromPage(String ret){
        System.out.println(ret);
//        Pattern pattern = Pattern.compile("<a[\\s\\S]?href=\"/art/collection/search/(\\d*)\">[\\s\\S]+ng-bind-html=.+>(.*?)</span>");
//        Matcher matcher = pattern.matcher(page);
//        Map<String, Integer> res = new HashMap<>();
//        while (matcher.find()) {
//            Integer searchId = Integer.valueOf(matcher.group(1));
//            String title = matcher.group(2);
//            res.put(title, searchId);
//        }
        JSONObject jo = JSON.parseObject(ret);
        JSONArray ja = jo.getJSONArray("results");
        Map<String, Integer> result = new HashMap<>();
        Iterator iter = ja.iterator();
        while (iter.hasNext()) {
            JSONObject object  = (JSONObject)iter.next();
            boolean bestBet = (Boolean)object.get("bestBet");
            if (!bestBet) {
                String title = object.getString("title");
                Integer id =  object.getInteger("id");
                result.put(title, id);
            }
        }
        return result;
    }

    /**
     * <h1 class="collection-details__object-title">北魏太和十年 青銅鎏金彌勒佛像<br>Buddha Maitreya (Mile fo)</h1>
     * @param
     * @return
     */
//    public Map<String, Set<String>> getTitleAndImageUrls(Set<String> uris){
//        Pattern imgPatter = Pattern.compile("<img\\s+ng-src=\"(http://images.metmuseum.org/CRDImages/.*/web-(additional|large)/\\w+.jpg)\"\\s+");
//        //http://images.metmuseum.org/CRDImages/es/web-large/DP110780.jpg
//        Pattern titlepattern = Pattern.compile("<h1\\s+class=\"collection-details__object-title\">(.*)</h1>");
//        Map<String, Set<String>>  res = new HashMap<>();
//        String host = "http://www.metmuseum.org";
//        for (String uri : uris) {
//            String url = host + uri;
//            String result = HttpClientUtil.doGet(url, null);
//            System.out.println(result);
//            Matcher imgM = imgPatter.matcher(result);
//            Matcher tM = titlepattern.matcher(result);
//            String title = null;
//            while (tM.find()) {
//                title = tM.group(1);
//            }
//            if (title != null) {
//                Set<String> imgUrls = new HashSet<>();
//                while (imgM.find()) {
//                    System.out.println(imgM.group());
//                    imgUrls.add(imgM.group());
//                    res.put(title, imgUrls);
//                }
//            }
//        }
//        return res;
//    }


    public Map<String, Set<String>> getTitleAndImageUrls(Map<String, Integer> res){
        Set<Map.Entry<String, Integer>> entries = res.entrySet();
        Map<String, Set<String>> map = new HashMap<>();
        for (Map.Entry entry : entries) {
            String title = (String) entry.getKey();
            Integer id = (Integer)entry.getValue();
            String url = "http://www.metmuseum.org/api/Collection/additionalImages";
            Map<String, String> param = new HashMap<>();
            param.put("crdId", String.valueOf(id));
            String result = HttpClientUtil.doGet(url, param);
            JSONObject jo = JSON.parseObject(result);
            JSONArray ja = (JSONArray)jo.get("results");
            Set<String> imgUrls = new HashSet<>();
            for (Object o : ja.toArray()) {
                JSONObject jobj = (JSONObject)o;
                String imageUrl = (String) jobj.get("webImageUrl");
                imgUrls.add(imageUrl);
            }
            map.put(title, imgUrls);
        }
        return map;
    }


    public static void main(String[] args) {
        int page = 1;
        String url = "http://www.metmuseum.org/api/search";
        Spider s = new Spider(page);
    }

    @Override
    public Map<String, Set<String>> call() throws Exception {
        String ret = renderByPage();
        Map<String, Integer> viewUrls = getViewUrlsFromPage(ret);
        System.out.println(viewUrls);
        return getTitleAndImageUrls(viewUrls);
    }
}
