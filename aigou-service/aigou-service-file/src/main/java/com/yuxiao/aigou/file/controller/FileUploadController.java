package com.yuxiao.aigou.file.controller;

import com.yuxiao.aigou.common.entry.Result;
import com.yuxiao.aigou.common.entry.StatusCode;
import com.yuxiao.aigou.file.dto.FastDFSFileDTO;
import com.yuxiao.aigou.file.util.FastDFSUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

@RestController
@RequestMapping(value = "/file/upload")
@CrossOrigin
public class FileUploadController {

    /**
     * 文件上传
     * @return
     */
    @PostMapping
    public Result fileUpload(@RequestParam(value = "file") MultipartFile file) throws Exception {
        //封装文件信息 FastDFSFileDTO
        FastDFSFileDTO fastDFSFileDTO=new FastDFSFileDTO();
        String fileName=file.getOriginalFilename();
        fastDFSFileDTO.setFileName(fileName);
        fastDFSFileDTO.setContent(file.getBytes());
        fastDFSFileDTO.setExt(StringUtils.getFilenameExtension(fileName));
        //调用FastDFSUtil工具 将文件上传至FastDFS中
        String[] update = FastDFSUtil.update(fastDFSFileDTO);
        String callUrl=FastDFSUtil.getTrackerInfo()+"/"+update[0]+"/"+update[1];
        HashMap<String, Object> resultMap = new HashMap<>(16);
        resultMap.put("groupName",update[0]);
        resultMap.put("remotefileName",update[1]);
        resultMap.put("callUrl",callUrl);
        return new Result(true, StatusCode.OK,"文件上传成功",resultMap);
    }
}
