package com.yuxiao.aigou.file.util;

import com.yuxiao.aigou.file.dto.FastDFSFileDTO;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * 用来实现FaseDFS文件管理（包括 文件上传 下载  删除 文件信息获取 Storage信息获取  Tracker信息获取）
 */
public class FastDFSUtil {

    /**
     * 加载Tracker连接信息
     */
    static {
        try {
            //加载Tracker信息
            //1.加载类路径下的文件  然后获取文件路径  还有另一种方式 直接在init方法中  使用 classpath://fdfs_client.conf 也可加载
            String confFilePath=new ClassPathResource("fdfs_client.conf").getPath();
            //2.加载FastDFS配置文件  用于获取Tracker相关的地址（ip及端口）
            ClientGlobal.init(confFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * FastDFS 实现文件上传
     * @param fastDFSFileDTO 上传的文件封装
     */
    public void  update(FastDFSFileDTO fastDFSFileDTO){
        //创建一个Tracker访问的客户端对象 TrackerClient

        //通过TrackerClient访问TrackerServer服务，获取Storage连接信息



    }
}
