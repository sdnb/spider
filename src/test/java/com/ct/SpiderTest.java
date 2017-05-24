package com.ct;

import com.util.HttpClientUtil;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chentao on 2017/5/23 0023.
 */
public class SpiderTest {

    /**
     * 1 通过传入offset参数及persize，一次获取一页数据
     * 2 得到数据中的每个藏品的链接以及标题
     <a href="/art/collection/search/486633">
     <span ng-bind-html="search.stripBrTags(searchResult.title)" class="ng-binding">China</span>
     <span class="search-results__result--subtitle ng-binding" ng-bind-html="searchResult.subTitle">Andrew Spence (American, born 1947) </span>
     </a>
     4 根据藏品的链接，进入藏品的详细页面
     5 找到附加图片的节点，得到图片url链接
     <div class="additional-images">
     <div class="additional-images__image-wrapper">

     <div class="additional-images__image ng-scope" ng-class="{ 'is-active': addImg.isActive(img) }" ng-repeat="img in addImg.images track by $index">
     <img ng-src="http://images.metmuseum.org/CRDImages/as/web-additional/DT7032.jpg" alt="" ng-click="addImg.selectImage(img)" src="http://images.metmuseum.org/CRDImages/as/web-additional/DT7032.jpg">
     </div><div class="additional-images__image ng-scope" ng-class="{ 'is-active': addImg.isActive(img) }" ng-repeat="img in addImg.images track by $index">
     <img ng-src="http://images.metmuseum.org/CRDImages/as/web-additional/DT7033.jpg" alt="" ng-click="addImg.selectImage(img)" src="http://images.metmuseum.org/CRDImages/as/web-additional/DT7033.jpg">
     </div>

     </div>
     </div>
     6 对图片进行下载
     * @throws Exception
     */

    @Test
    public void testGetList() throws Exception {


        String url = "http://www.metmuseum.org/art/collection/search/203826";
        Map<String, String> params = new HashMap<>();
        params.put("sortBy", "Relevance");
        params.put("ft", "china");
        params.put("offset", "0");
        params.put("rpp", "20");
        params.put("pos", "2");
        Pattern pattern = Pattern.compile("<img\\s+src=['|\"]{1}(.*)['|\"]{1}\\s*/?>{1}");
//        Pattern pattern = Pattern.compile("<img\\s+src=['|\"]{1}(.*)['|\"]{1}\\s*/?>{1}");
        String ret = HttpClientUtil.doGet(url, params);
        Matcher matcher = pattern.matcher(ret);
        System.out.println(ret);
//        while (matcher.find()) {
////            String g = matcher.group();
//            String g1 = matcher.group(1);
////            System.out.println(g);
//            System.out.println(g1);
//        }
//        System.out.println(HttpClientUtil.doGet(url, params));

    }


    @Test
    public void testGetProductUrl(){
        File file = new File("e:\\spider\\data\\Bottle with Peony Scroll\\DP274691.jpg ");
    }

}