/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50722
 Source Host           : localhost:3306
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 50722
 File Encoding         : 65001

 Date: 25/03/2019 13:33:24
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for serial_number
-- ----------------------------
DROP TABLE IF EXISTS `serial_number`;
CREATE TABLE `serial_number`  (
  `auto_id` int(11) NOT NULL AUTO_INCREMENT,
  `serial_rule_id` int(11) DEFAULT NULL COMMENT '流水号规则id',
  `serial_year` int(11) DEFAULT NULL COMMENT '当前年份',
  `serial_month` int(11) DEFAULT NULL COMMENT '当前月份',
  `serial_day` int(11) DEFAULT NULL COMMENT '当前日期',
  `current_serial` int(11) DEFAULT NULL COMMENT '当前序列号',
  `update_time` datetime(0) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`auto_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '流水号当前序列号记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of serial_number
-- ----------------------------
INSERT INTO `serial_number` VALUES (1, 1, 2019, 3, NULL, 40, '2019-03-25 12:42:21');
INSERT INTO `serial_number` VALUES (2, 1, 2019, 2, NULL, 80, '2019-03-25 12:48:38');
INSERT INTO `serial_number` VALUES (3, 1, 2019, 2, 23, 10, '2019-03-25 12:51:20');

-- ----------------------------
-- Table structure for serial_rule
-- ----------------------------
DROP TABLE IF EXISTS `serial_rule`;
CREATE TABLE `serial_rule`  (
  `serial_rule_id` int(11) NOT NULL AUTO_INCREMENT,
  `module_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '模块编码（唯一约束）',
  `module_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '模块名称',
  `with_year` bit(1) DEFAULT NULL COMMENT '是否包含年',
  `year_length` int(11) DEFAULT NULL COMMENT '年份的长度，4：yyyy，2：yy',
  `with_month` bit(1) DEFAULT NULL COMMENT '是否包含月，mm',
  `with_day` bit(1) DEFAULT NULL COMMENT '是否包含天，dd',
  `prefix` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '流水号前缀',
  `serial_length` int(11) NOT NULL COMMENT '流水号序号长度，例如4,则为0001',
  PRIMARY KEY (`serial_rule_id`) USING BTREE,
  UNIQUE INDEX `module_code`(`module_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '流水号生存规则表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of serial_rule
-- ----------------------------
INSERT INTO `serial_rule` VALUES (1, 'model_order', '订单流水号', b'1', 4, b'1', b'1', 'O_', 4);

SET FOREIGN_KEY_CHECKS = 1;
