package com.aki.mongodb.learnmongodb.controller;

import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : maybesuch
 * @version : 1.0
 * @Description : GridFS文件基本操作
 * @Date : 2020/1/8 10:16
 * @Copyright : Copyright (c) 2020 All Rights Reserved
 **/
@Controller
@RequestMapping("/file")
public class GridFSController {

    @Autowired
    private GridFsTemplate gridFsTemplate;


    @RequestMapping(value = "filePage")
    public String mainPage() {
        return "filePage";
    }

    /**
     * 上传文件
     *
     * @param multipartFile 文件
     * @return 上传成功文件id
     */
    @RequestMapping("/upload")
    @ResponseBody
    public String uploadFile(@RequestParam(value = "file") MultipartFile multipartFile) {

        // 设置meta数据值
        Map<String, String> metaData = new HashMap<>();
        metaData.put("tags", "test");
        // ...
        try (
                InputStream inputStream = multipartFile.getInputStream();
        ) {
            // 获取文件的源名称
            String fileName = multipartFile.getOriginalFilename();
            // 进行文件存储
            ObjectId objectId = gridFsTemplate.store(inputStream, fileName, metaData);
            // 返回文件的id
            return objectId.toHexString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取文件信息
     * @param fileId 文件id     */
    @RequestMapping("/get/{fileId}")
    @ResponseBody
    public void getFile(@PathVariable("fileId") String fileId) {

        //根据id查询文件
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));

        if (gridFSFile == null) {
            throw new RuntimeException("No file with id: " + fileId);
        }
        //获取流对象
        GridFsResource resource = gridFsTemplate.getResource(gridFSFile);

        /*可根据实际需求进行数据的获取*/
        try {
            //获取流中的数据
            String content = IOUtils.toString(resource.getInputStream(), "UTF-8");
            //获取byte[]信息
            byte[] bytes = IOUtils.toByteArray(resource.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除文件
     * @param fileId 文件id
     */
    @RequestMapping("/delete/{fileId}")
    @ResponseBody
    public void deleteFile(@PathVariable(value = "fileId") String fileId) {
        // 根据文件id删除fs.files和fs.chunks中的记录
        gridFsTemplate.delete(Query.query(Criteria.where("_id").is(fileId)));

    }

}
