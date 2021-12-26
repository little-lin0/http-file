CREATE TABLE `file_item`
(
    `id`   varchar(100) NOT NULL COMMENT '文件uuid',
    `name` varchar(100)  NOT NULL COMMENT '文件原始文件名',
    `type` varchar(45)  NOT NULL COMMENT '文件类型',
    `size` bigint       NOT NULL COMMENT '文件大小',
    `url`  varchar(200) NOT NULL COMMENT '文件位置',
    `date` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '文件创建时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;