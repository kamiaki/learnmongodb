package com.aki.mongodb.learnmongodb.controller;

import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
    public void getFile(@PathVariable("fileId") String fileId, HttpServletResponse response) {

        //根据id查询文件
//        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("metadata.tags").is(fileId)));
        if (gridFSFile == null) {
            throw new RuntimeException("No file with id: " + fileId);
        }
        //获取流对象
        GridFsResource resource = gridFsTemplate.getResource(gridFSFile);

        InputStream inputStream = null;
        BufferedInputStream bis = null;
        OutputStream os;
        /*可根据实际需求进行数据的获取*/
        try {
            os = response.getOutputStream();

            inputStream = resource.getInputStream();
            bis = new BufferedInputStream(inputStream);
            byte[] buffer = new byte[1024];
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
