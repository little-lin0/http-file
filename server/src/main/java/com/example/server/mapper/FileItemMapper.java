package com.example.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.server.entity.FileItem;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;

/**
 * @author LIN
 */
@Mapper
public interface FileItemMapper extends BaseMapper<FileItem> {
}
