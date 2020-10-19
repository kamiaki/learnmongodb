package com.aki.mongodb.learnmongodb.controller;

import com.aki.mongodb.learnmongodb.po.JSONResult;
import com.aki.mongodb.learnmongodb.po.UploadFile;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

@Controller
@RequestMapping(value = "image")
public class ImagesController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @RequestMapping(value = "images")
    public String mainPage() {
        return "images";
    }

    @RequestMapping("upload")
    @ResponseBody
    public JSONResult uploadImage(HttpServletRequest request, @RequestParam(value = "image") MultipartFile file) {
        if (file.isEmpty())
            return new JSONResult(200, "请选择一张图片", null);

        // 返回的 JSON 对象，这种类可自己封装
        JSONResult jsonResult;
        String fileName = file.getOriginalFilename();
        try {
            UploadFile uploadFile = new UploadFile();
            uploadFile.setName(fileName);
            uploadFile.setCreatedTime(new Date());
            uploadFile.setContent(new Binary(file.getBytes()));
            uploadFile.setContentType(file.getContentType());
            uploadFile.setSize(file.getSize());

            UploadFile savedFile = mongoTemplate.save(uploadFile);
            String url = request.getScheme()
                    + "://"
                    + request.getLocalAddr()
                    + ":" + request.getLocalPort()
                    + request.getContextPath()
                    + "/image/get/"
                    + savedFile.getId();

            jsonResult = new JSONResult(200, "图片上传成功", url);
        } catch (IOException e) {
            e.printStackTrace();
            jsonResult = new JSONResult(500, "图片上传失败", null);
        }
        return jsonResult;
    }


    /**
     * produces：表示返回给前端的类型，比如文本、GIF、PDF 等等，这里我们当然返回图片了
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/get/{id}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @ResponseBody
    public byte[] image(@PathVariable String id) {
        byte[] data = null;
        Query query = new Query(Criteria.where("id").is(id));
        UploadFile file = mongoTemplate.findOne(query, UploadFile.class);
        if (file != null) {
            data = file.getContent().getData();
        }
        return data;
    }
}
