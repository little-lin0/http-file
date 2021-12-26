package com.example.server.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author LIN
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileItem {

    @TableId
    private String id;
    private String name;
    private String type;
    private Long size;
    private String url;
    private String date;
}
