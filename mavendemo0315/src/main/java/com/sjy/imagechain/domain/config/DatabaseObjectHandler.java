package com.sjy.imagechain.domain.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author shuomc
 * @Date 2025/5/16
 * @Description
 */
@Component // 将处理器注册为 Spring Bean
public class DatabaseObjectHandler implements MetaObjectHandler {

    // 插入时自动填充
    @Override
    public void insertFill(MetaObject metaObject) {
        // 插入时填充 createTime
        // this.strictInsertFill(metaObject, 字段名, 字段类型, 填充值);
        // 第一个参数 metaObject 是元对象
        // 第二个参数 "createTime" 是实体类中的字段名
        // 第三个参数 LocalDateTime.class 是字段的类型
        // 第四个参数 LocalDateTime.now() 是要填充的值
        boolean hasCreateTime = metaObject.hasSetter("createTime");
        if (hasCreateTime && metaObject.getValue("createTime") == null) {
            // 检查字段是否存在且当前值为 null，避免覆盖手动设置的值
            this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        }
        // 插入时同时填充 updateTime (因为使用了 FieldFill.INSERT_UPDATE)
        boolean hasUpdateTime = metaObject.hasSetter("updateTime");
        if (hasUpdateTime && metaObject.getValue("updateTime") == null) {
            this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        }

        // 如果 isDeleted 字段也需要插入时设置默认值（例如 false），也可以在这里处理
        boolean hasIsDeleted = metaObject.hasSetter("isDeleted");
        if (hasIsDeleted && metaObject.getValue("isDeleted") == null) {
            this.strictInsertFill(metaObject, "isDeleted", Boolean.class, false);
        }
    }

    // 更新时自动填充
    @Override
    public void updateFill(MetaObject metaObject) {
        // 更新时填充 updateTime
        // this.strictUpdateFill(metaObject, 字段名, 字段类型, 填充值);
        boolean hasUpdateTime = metaObject.hasSetter("updateTime");
        if (hasUpdateTime) {
            // 更新时通常直接覆盖旧值
            this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        }
        // 注意：通常更新时不填充 createTime
    }
}
