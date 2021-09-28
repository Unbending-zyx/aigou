package com.yuxiao.aigou.file.util;

import com.alibaba.fastjson.JSON;
import com.yuxiao.aigou.file.dto.FastDFSFileDTO;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


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
    public static String[]  update(FastDFSFileDTO fastDFSFileDTO) throws Exception {
        //创建一个Tracker访问的客户端对象 TrackerClient
        TrackerClient trackerClient=new TrackerClient();
        //通过TrackerClient访问TrackerServer服务，获取TrackerServer连接信息
        TrackerServer trackerServer = trackerClient.getConnection();
        //通过TrackerServer连接信息可以获取到Storage连接信息  创建StorageClient对象存储Storage连接信息
        StorageClient storageClient=new StorageClient(trackerServer,null);

        //封装附加参数
        NameValuePair[] meta_list=new NameValuePair[3];
        meta_list[0]=new NameValuePair("fileName",fastDFSFileDTO.getFileName());
        meta_list[1]=new NameValuePair("author",fastDFSFileDTO.getAuthor());
        meta_list[2]=new NameValuePair("md5",fastDFSFileDTO.getMd5());



        //通过StorageClient访问Storage，实现文件上传，并且获取文件上传后的存储信息
        /**
         * 方法属性介绍
         *  byte[] file_buff  : 文件的字节数组
         *  String file_ext_name ： 文件扩展名
         *  NameValuePair[] meta_list : 附加参数
         */
        String[] uploadFile = storageClient.upload_file(fastDFSFileDTO.getContent(), fastDFSFileDTO.getExt(), meta_list);
        /**
         * 返回参数介绍
         *  上传文件返回的参数是一个数组
         *  uploadFile[0]  返回的是 文件上传至哪个group中
         *  uploadFile[1]  返回的是文件在storage中保存的虚拟路径
         *
         *  示例：
         *      上传文件的返回值为：["group1","M00/00/00/rBBRH2FP6MqAa9OTAAABNJUm7P0325.txt"]
         *
         *  文件访问方式：
         *      ip:port/group名/文件在storage中的虚拟路径
         *      例：
         *          http://172.16.81.31:8080/group1/M00/00/00/rBBRH2FP6MqAa9OTAAABNJUm7P0325.txt
         */
        System.out.println("上传文件的返回值为："+ JSON.toJSONString(uploadFile));
        return uploadFile;
    }

    /**
     * 获取fastDFS中的文件信息  这个信息是文件在服务器（FastDFS）上的的一些信息
     * @param groupName 文件所属组名  例 group1
     * @param remotefileName 文件的虚拟路径 最前面无/ 例：M00/00/00/rBBRH2FP6MqAa9OTAAABNJUm7P0325.txt
     * @return
     * @throws Exception
     */
    public static FileInfo getFileInfo(String groupName,String remotefileName) throws Exception {
        //创建Storage客户端
        StorageClient storageClient=getStorageClient();
        FileInfo fileInfo = storageClient.get_file_info(groupName, remotefileName);
        System.out.println("获取的文件信息为："+JSON.toJSONString(fileInfo));
        return fileInfo;
    }

    /**
     * 获取fastDFS中的文件信息 上传文件时自定义传入的一些信息
     * @param groupName 文件所属组名  例 group1
     * @param remotefileName 文件的虚拟路径 最前面无/ 例：M00/00/00/rBBRH2FP6MqAa9OTAAABNJUm7P0325.txt
     * @return
     * @throws Exception
     */
    public static NameValuePair[] getFileInfoCustomize(String groupName,String remotefileName) throws Exception {
        //创建Storage客户端
        StorageClient storageClient=getStorageClient();
        NameValuePair[] metadata = storageClient.get_metadata(groupName, remotefileName);
        System.out.println("获取的文件信息（自定义信息）为："+JSON.toJSONString(metadata));
        return metadata;
    }

    /**
     * 文件下载
     * @param groupName 文件所属组名  例 group1
     * @param remotefileName 文件的虚拟路径 最前面无/ 例：M00/00/00/rBBRH2FP6MqAa9OTAAABNJUm7P0325.txt
     * @return
     * @throws Exception
     */
    public static InputStream downLoadFile(String groupName, String remotefileName) throws Exception {
        StorageClient storageClient=getStorageClient();
        //文件下载
        byte[] bytes = storageClient.download_file(groupName, remotefileName);
        return new ByteArrayInputStream(bytes);
    }

    /**
     * 文件删除
     * @param groupName 文件所属组名  例 group1
     * @param remotefileName 文件的虚拟路径 最前面无/ 例：M00/00/00/rBBRH2FP6MqAa9OTAAABNJUm7P0325.txt
     * @return
     * @throws Exception
     */
    public static void deleteFile(String groupName, String remotefileName) throws Exception {
        StorageClient storageClient=getStorageClient();
        //文件删除
        storageClient.delete_file(groupName, remotefileName);
    }

    /**
     * 获取Storage信息
     */
    public static StorageServer getStorageInfo() throws Exception {
        //创建一个Tracker访问的客户端对象 TrackerClient
        TrackerClient trackerClient=new TrackerClient();
        //通过TrackerClient访问TrackerServer服务，获取TrackerServer连接信息
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageServer storeStorage = trackerClient.getStoreStorage(trackerServer);
        System.out.println(JSON.toJSONString(storeStorage));
        return storeStorage;
    }

    /**
     * 根据文件组名和文件路径获取文件所在Storage所在ip及端口信息
     * @param groupName
     * @param remotefileName
     * @return
     * @throws Exception
     */
    public static ServerInfo[] getStorageInfo(String groupName, String remotefileName) throws Exception {
        //创建一个Tracker访问的客户端对象 TrackerClient
        TrackerClient trackerClient=new TrackerClient();
        //通过TrackerClient访问TrackerServer服务，获取TrackerServer连接信息
        TrackerServer trackerServer = trackerClient.getConnection();
        ServerInfo[] fetchStorages = trackerClient.getFetchStorages(trackerServer, groupName, remotefileName);
        System.out.println(JSON.toJSONString(fetchStorages));
        return fetchStorages;
    }


    /**
     * 获取Tracker信息  Trakcer的访问地址
     */
    public static String getTrackerInfo() throws Exception {
        //创建一个Tracker访问的客户端对象 TrackerClient
        TrackerClient trackerClient=new TrackerClient();
        //通过TrackerClient访问TrackerServer服务，获取TrackerServer连接信息
        TrackerServer trackerServer = trackerClient.getConnection();
        //8080 fdfs_client.conf 中配置的属性
        int tracker_http_port = ClientGlobal.getG_tracker_http_port();
        //Tracker获取ip地址
        String ip = trackerServer.getInetSocketAddress().getHostString();
        String url = "http://"+ip+":"+tracker_http_port;
        return url;

    }


    /**
     * 获取StorageClient 用于文件操作
     * @return
     * @throws Exception
     */
    private static StorageClient getStorageClient() throws Exception {
        //创建一个Tracker访问的客户端对象 TrackerClient
        TrackerClient trackerClient=new TrackerClient();
        //通过TrackerClient访问TrackerServer服务，获取TrackerServer连接信息
        TrackerServer trackerServer = trackerClient.getConnection();
        //通过TrackerServer连接信息可以获取到Storage连接信息  创建StorageClient对象存储Storage连接信息
        StorageClient storageClient=new StorageClient(trackerServer,null);
        return storageClient;
    }


    public static void main(String[] args) {
        try {
            getFileInfo("group1","M00/00/00/rBBRH2FP6MqAa9OTAAABNJUm7P0325.txt");
            getFileInfoCustomize("group1","M00/00/00/rBBRH2FP6MqAa9OTAAABNJUm7P0325.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            StorageServer storageInfo = getStorageInfo();
            getStorageInfo("group1","M00/00/00/rBBRH2FP6MqAa9OTAAABNJUm7P0325.txt");
            String trackerInfo = getTrackerInfo();
            System.out.println(trackerInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
