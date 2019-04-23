
CREATE DATABASE `test` CHARACTER SET 'utf8' COLLATE 'utf8_general_ci';
SET FOREIGN_KEY_CHECKS=0;

use test;
-- ----------------------------
-- Table structure for t_demo
-- ----------------------------
DROP TABLE IF EXISTS `t_demo`;
CREATE TABLE `t_demo` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键id',
  `name` varchar(60) DEFAULT NULL COMMENT ' 名称',
  `date` datetime DEFAULT NULL COMMENT '数据日期',
  `timestamp` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新记录时刷新当前时间戳记时',
  `KEY` varchar(30) DEFAULT NULL COMMENT '测试关键字',
  `ac dd` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `indx_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_demo
-- ----------------------------
INSERT INTO `t_demo` VALUES ('1', '张三', '2018-07-11 16:00:25', '2018-07-27 12:29:40', '1', '1');
INSERT INTO `t_demo` VALUES ('2', '李四', null, '2018-07-27 12:29:40', '1', '1');
INSERT INTO `t_demo` VALUES ('3', '王五', null, '2018-07-27 12:29:40', '3', '3');
INSERT INTO `t_demo` VALUES ('4', '赵六', '2018-07-02 16:01:32', '2018-07-27 12:29:40', '23', '23');
INSERT INTO `t_demo` VALUES ('5', 'lisa', null, '2018-07-27 12:29:40', 'sdf', 'sdf');
INSERT INTO `t_demo` VALUES ('6', 'lily', null, '2018-07-27 12:29:40', 'sg的', 'sg的');
INSERT INTO `t_demo` VALUES ('7', 'jeny', '2018-07-27 16:01:26', '2018-07-27 12:29:40', '手段', '手段');
INSERT INTO `t_demo` VALUES ('8', 'jack', null, '2018-07-27 12:29:40', 'sdf', 'sdf');
INSERT INTO `t_demo` VALUES ('9', 'owen', null, '2018-07-27 12:29:40', '手段', '手段');
INSERT INTO `t_demo` VALUES ('10', 'reven', null, '2018-07-27 12:29:40', '个', '个');