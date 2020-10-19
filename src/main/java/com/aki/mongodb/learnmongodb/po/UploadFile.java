package com.aki.mongodb.learnmongodb.po;

import lombok.Data;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
/**
 * @Document
 * 标注在实体类上，类似于hibernate的entity注解，标明由mongo来维护该表
 */
@Document
/**
 * @CompoundIndex
 * 复合索引，加复合索引后通过复合索引字段查询将大大提高速度。
 * 写法如上，lastName和age将作为复合索引，数字参数指定索引的方向，1为正序，-1为倒序。方向对单键索引和随机存不要紧，但如果你要执行分组和排序操作的时候，它就非常重要了。
 */
@CompoundIndexes({
        @CompoundIndex(name = "lianhe", def = "{'name' : 1, 'contentType' : -1}")
})
public class UploadFile {

    /**
     * @Id
     * 主键，不可重复，自带索引，可以在定义的列名上标注，需要自己生成并维护不重复的约束。如果自己不设置@Id主键，mongo会自动生成一个唯一主键，并且插入时效率远高于自己设置主键。原因可参考上一篇mongo和mysql的性能对比。
     * 在实际业务中不建议自己设置主键，应交给mongo自己生成，自己可以设置一个业务id，如int型字段，用自己设置的业务id来维护相关联的表。
     */
    @Id
    private String id;

    /**
     * @Indexed
     * 声明该字段需要加索引，加索引后以该字段为条件检索将大大提高速度。
     * 唯一索引的话是@Indexed(unique = true)。
     * 也可以对数组进行索引，如果被索引的列是数组时，MongoDB会索引这个数组中的每一个元素。
     * 也可以对整个Document进行索引，排序是预定义的按插入BSON数据的先后升序排列。
     * 也可以对关联的对象的字段进行索引，譬如User对关联的address.city进行索引。
     */
    @Indexed
    private String name; // 文件名
    /**
     * @Field
     * 代表一个字段，可以不加，不加的话默认以参数名为列名。
     */
    @Field(value = "createdTime")
    private Date createdTime; // 上传时间
    private Binary content; // 文件内容
    private String contentType; // 文件类型
    private long size; // 文件大小

    /**
     * @Transient
     * 被该注解标注的，将不会被录入到数据库中。只作为普通的javaBean属性。
     */
    @Transient
    private String ps;


    /**
     * @DBRef
     * 关联另一个document对象。类似于mysql的表关联，但并不一样，mongo不会做级联的操作。
     */
}
