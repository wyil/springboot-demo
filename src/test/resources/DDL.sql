CREATE TABLE `system_update_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `version` varchar(50) DEFAULT NULL COMMENT '系统版本',
  `update_no` int(11) DEFAULT NULL COMMENT '更新序号',
  `update_date` date DEFAULT NULL COMMENT '更新时间',
  `context` varchar(500) DEFAULT NULL COMMENT '更新内容（富文本）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统更新日志';