package com.ct;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Created by chentao on 2017/5/23 0023.
 */
public class ImageDownloader implements Runnable {
    private final BlockingQueue<Map<String, Set<String>>> queue;
    public ImageDownloader(BlockingQueue<Map<String, Set<String>>> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {

        try {
//            Map<String, Set<String>> ret = queue.poll(5000, TimeUnit.MILLISECONDS);
            Map<String, Set<String>> ret = queue.take();
            Set<Map.Entry<String, Set<String>>> entries = ret.entrySet();
            for (Map.Entry<String, Set<String>> entry : entries) {
                String storePath =  Constant.FILE_BASE_PATH + entry.getKey();
                File dir = new File(storePath);
                if(!dir.exists()){
                    dir.mkdirs();
                }
                for (String imgUrl : entry.getValue()) {
                    String imgName = imgUrl.substring(imgUrl.lastIndexOf("/") + 1);
                    String fileName = storePath + "/" + imgName;
                    fileName = fileName.replaceAll("\\f|\\n|\\r|\\t|\\v", "");
                    File file = new File( fileName);
                    if (file.exists()) {
                        continue;
                    }

                    try {
                        file.createNewFile();
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("创建文件:" + fileName + "报错");
                        continue;
                    }

                    System.out.println("下载图片:" + entry.getKey() + ":" + imgName);
                    OutputStream os = new FileOutputStream(file);

                    //创建一个url对象
                    URL url = new URL(imgUrl);
                    InputStream is = url.openStream();
                    byte[] buff = new byte[1024];
                    while(true) {
                        int readed = is.read(buff);
                        if(readed == -1) {
                            break;
                        }
                        byte[] temp = new byte[readed];
                        System.arraycopy(buff, 0, temp, 0, readed);
                        //写入文件
                        os.write(temp);
                    }
                    System.out.println("存储路径"+file.getAbsolutePath());
                    is.close();
                    os.close();
                }
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

//        ImageDownloader downloader = new ImageDownloader("http://images.metmuseum.org/CRDImages/as/web-large/DT7033.jpg", "测试图1");
        ExecutorService service = Executors.newFixedThreadPool(20);
        BlockingQueue<Map<String, Set<String>>> queue = new LinkedBlockingQueue<>();
        for (int i = 1; i <10; i++){
            Spider spider = new Spider(queue, i);
            service.execute(spider);
        }

        for (int i = 1; i < 10; i++){
            ImageDownloader downloader = new ImageDownloader(queue);
            service.execute(downloader);
        }

    }

}
