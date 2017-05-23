package com.ct;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * Created by chentao on 2017/5/23 0023.
 */
public class ImageDownloader implements Runnable {

    private String downloadUrl;
    private String storeName;

    public ImageDownloader(String downloadUrl, String storeName) {
        this.downloadUrl = downloadUrl;
        this.storeName = storeName;
    }

    @Override
    public void run() {
        File dir = new File(Constant.FILE_STORE_PATH);
        if(!dir.exists()){
            dir.mkdirs();
        }
        String sufix = downloadUrl.substring(downloadUrl.lastIndexOf(".")+1);
        try {
            File file = new File( Constant.FILE_STORE_PATH + File.separator + storeName + "." + sufix);
            if (file.exists()) {
                file.createNewFile();
            }
            OutputStream os = new FileOutputStream(file);
            //创建一个url对象
            URL url = new URL(downloadUrl);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        ImageDownloader downloader = new ImageDownloader("http://images.metmuseum.org/CRDImages/as/web-large/DT7033.jpg", "测试图1");
        Thread t = new Thread(downloader);
        t.start();
    }

}
