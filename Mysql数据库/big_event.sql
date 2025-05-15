/*
 Navicat Premium Dump SQL

 Source Server         : Rent
 Source Server Type    : MySQL
 Source Server Version : 80040 (8.0.40)
 Source Host           : localhost:3306
 Source Schema         : big_event

 Target Server Type    : MySQL
 Target Server Version : 80040 (8.0.40)
 File Encoding         : 65001

 Date: 15/05/2025 11:06:58
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `title` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文章标题',
  `content` varchar(10000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文章内容',
  `cover_img` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文章封面',
  `state` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '草稿' COMMENT '文章状态: 只能是[已发布] 或者 [草稿]',
  `category_id` int UNSIGNED NULL DEFAULT NULL COMMENT '文章分类ID',
  `create_user` int UNSIGNED NOT NULL COMMENT '创建人ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_article_category`(`category_id` ASC) USING BTREE,
  INDEX `fk_article_user`(`create_user` ASC) USING BTREE,
  CONSTRAINT `fk_article_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_article_user` FOREIGN KEY (`create_user`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of article
-- ----------------------------
INSERT INTO `article` VALUES (1, '测试标题', '<p>测试内容测试内容测试内容测试内容测试内容</p>', 'https://housing-rent.oss-cn-guangzhou.aliyuncs.com/347f93db-dadb-4b13-92fc-72466bdf2228.jpg', '已发布', 1, 2, '2025-02-15 22:58:29', '2025-02-15 22:58:29');
INSERT INTO `article` VALUES (2, '123', '<p>测试内容测试内容测试内容测试内容</p>', 'https://housing-rent.oss-cn-guangzhou.aliyuncs.com/9b26d91b-d32a-4711-b3ee-533cf63300ee.jpg', '草稿', 2, 2, '2025-02-15 22:59:00', '2025-02-15 22:59:00');
INSERT INTO `article` VALUES (3, '333', '<p>123123123</p>', 'https://housing-rent.oss-cn-guangzhou.aliyuncs.com/df1d682f-d9e5-4646-83a9-d53cc8b7448f.jpg', '已发布', 2, 2, '2025-02-19 13:57:16', '2025-02-19 13:57:16');
INSERT INTO `article` VALUES (4, '33', '<p>123123</p>', 'https://housing-rent.oss-cn-guangzhou.aliyuncs.com/460f4a5a-df4d-4a07-8faf-7862ed5c0045.png', '已发布', 1, 2, '2025-02-24 23:35:59', '2025-02-24 23:35:59');
INSERT INTO `article` VALUES (5, '777', '<p>7777</p>', 'https://housing-rent.oss-cn-guangzhou.aliyuncs.com/f0448690-6fb7-4a7d-964c-f9581ba14235.jpg', '已发布', 2, 2, '2025-02-24 23:36:22', '2025-02-24 23:36:22');
INSERT INTO `article` VALUES (6, '555', '<p>rdtert</p>', 'https://housing-rent.oss-cn-guangzhou.aliyuncs.com/6c4a4205-25ce-426b-bb20-7f6cf961d076.jpg', '已发布', 10, 3, '2025-02-24 23:38:32', '2025-02-24 23:38:32');
INSERT INTO `article` VALUES (7, 'ff', '<p>123123</p>', 'https://housing-rent.oss-cn-guangzhou.aliyuncs.com/90f9355d-cc76-49b9-a906-20ee8cd6719b.jpg', '已发布', 2, 2, '2025-02-24 23:57:52', '2025-02-24 23:57:52');
INSERT INTO `article` VALUES (8, '1', '<p>1</p>', 'https://housing-rent.oss-cn-guangzhou.aliyuncs.com/c80d2093-d661-480d-85ac-57a0563f8652.jpg', '已发布', 2, 2, '2025-03-11 01:00:38', '2025-03-11 01:00:38');

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `category_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名称',
  `category_alias` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类别名',
  `create_user` int UNSIGNED NOT NULL COMMENT '创建人ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `operator_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_category_user`(`create_user` ASC) USING BTREE,
  CONSTRAINT `fk_category_user` FOREIGN KEY (`create_user`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (1, '別墅', 'bs', 2, '2025-02-15 22:57:08', '2025-02-24 15:47:12', '管理员');
INSERT INTO `category` VALUES (2, '地铁旁', 'dtp', 2, '2025-02-15 22:58:44', '2025-02-15 22:58:44', '管理员');
INSERT INTO `category` VALUES (3, '555', '555', 2, '2025-02-19 13:41:42', '2025-02-19 13:41:42', '管理员');
INSERT INTO `category` VALUES (5, '22', '33', 2, '2025-02-24 15:47:18', '2025-02-24 15:47:18', '管理员');
INSERT INTO `category` VALUES (6, '测试', 'cs', 2, '2025-02-24 15:57:49', '2025-02-24 15:57:49', '管理员');
INSERT INTO `category` VALUES (10, 'fd', 'fd', 3, '2025-02-24 16:35:08', '2025-02-24 16:35:08', '房东');
INSERT INTO `category` VALUES (11, 'asd', 'asd', 3, '2025-02-24 16:44:04', '2025-02-24 16:44:04', '房东');
INSERT INTO `category` VALUES (12, 'ee', 'ee', 2, '2025-02-24 21:01:32', '2025-02-24 21:01:32', '管理员');
INSERT INTO `category` VALUES (13, 'eef', 'eef', 3, '2025-02-24 21:14:43', '2025-02-24 21:14:43', '房东');
INSERT INTO `category` VALUES (14, 'tt', 'tt', 3, '2025-02-24 21:15:56', '2025-02-24 21:15:56', '房东');
INSERT INTO `category` VALUES (15, 'gg', 'gg', 2, '2025-02-24 21:16:11', '2025-02-24 21:16:11', '管理员');
INSERT INTO `category` VALUES (16, 'poi', 'poi', 3, '2025-02-24 22:56:01', '2025-02-24 22:56:01', '房东');
INSERT INTO `category` VALUES (17, '2233', '333', 2, '2025-03-10 17:22:02', '2025-03-10 17:22:02', NULL);

-- ----------------------------
-- Table structure for contract
-- ----------------------------
DROP TABLE IF EXISTS `contract`;
CREATE TABLE `contract`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '合同ID',
  `contract_title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '合同标题',
  `house_id` int UNSIGNED NOT NULL COMMENT '房源ID',
  `tenant_id` int UNSIGNED NOT NULL COMMENT '租客ID',
  `landlord_id` int UNSIGNED NULL DEFAULT NULL COMMENT '房东ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '合同内容（HTML 富文本）',
  `sign_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '未签署' COMMENT '签署状态：未签署/已签署',
  `signed_time` datetime NULL DEFAULT NULL COMMENT '签署时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `landlord_signed` tinyint(1) NULL DEFAULT 0,
  `tenant_signed` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `house_id`(`house_id` ASC) USING BTREE,
  INDEX `tenant_id`(`tenant_id` ASC) USING BTREE,
  CONSTRAINT `contract_ibfk_1` FOREIGN KEY (`house_id`) REFERENCES `houserent` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `contract_ibfk_2` FOREIGN KEY (`tenant_id`) REFERENCES `front` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '合同表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of contract
-- ----------------------------
INSERT INTO `contract` VALUES (22, '签订合同', 64, 12, 13, '签订合同签订合同', '已签署', '2025-04-27 01:50:26', '2025-04-27 01:49:43', '2025-04-27 01:50:26', 1, 1);
INSERT INTO `contract` VALUES (23, '合同11', 66, 12, 13, '我是内容111', '已签署', '2025-05-11 00:38:09', '2025-05-11 00:37:49', '2025-05-11 00:38:09', 1, 1);
INSERT INTO `contract` VALUES (24, 'asdasd', 69, 12, 13, '555555', '已签署', '2025-05-11 10:01:55', '2025-05-11 10:01:38', '2025-05-11 10:01:55', 1, 1);

-- ----------------------------
-- Table structure for front
-- ----------------------------
DROP TABLE IF EXISTS `front`;
CREATE TABLE `front`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `nickname` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '昵称',
  `email` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '邮箱',
  `user_pic` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '头像',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '租客' COMMENT '身份权限',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '前台用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of front
-- ----------------------------
INSERT INTO `front` VALUES (2, 'qdyh', 'e10adc3949ba59abbe56e057f20f883e', '墨鱼', 'moyu@123123.com', 'https://housing-rent.oss-cn-guangzhou.aliyuncs.com/d28ce0af-00af-42a8-8e07-1a575123216e.jpg', '2025-02-15 22:46:02', '2025-03-24 19:53:23', '管理员');
INSERT INTO `front` VALUES (12, '888888', '21218cca77804d2ba1922c33e0151105', '租客1', '12121120@qq.com', 'https://housing-rent.oss-cn-guangzhou.aliyuncs.com/00644b67-73bc-4315-9549-0d8527c73b25.jpeg', '2025-03-06 20:31:47', '2025-05-11 00:38:58', '租客');
INSERT INTO `front` VALUES (13, '123123', '4297f44b13955235245b2497399d7a93', '房东1', '91211138012@qq.com', 'https://housing-rent.oss-cn-guangzhou.aliyuncs.com/223e4388-e6e7-4e9b-895c-229bdd4ee68b.jpg', '2025-03-07 00:24:31', '2025-04-25 02:17:11', '房东');
INSERT INTO `front` VALUES (14, '333333', '1a100d2c0dab19c4430e7d73762b3423', '', '', '', '2025-04-07 15:18:29', '2025-04-07 15:18:29', NULL);
INSERT INTO `front` VALUES (15, '777777', 'f63f4fbc9f8c85d409f2f59f2b9e12d5', 'uuu', '970001980@qq.com', '', '2025-04-14 19:36:05', '2025-04-14 19:37:54', '租客');
INSERT INTO `front` VALUES (16, '000000', '670b14728ad9902aecba32e22fa4f6bd', 'opopop', '970001980@qq.com', 'https://housing-rent.oss-cn-guangzhou.aliyuncs.com/ccb592b5-3b88-453b-a71f-6766a463453a.jpg', '2025-04-19 16:08:19', '2025-04-19 16:09:55', '房东');
INSERT INTO `front` VALUES (17, 'kkkkkk', 'c08ac56ae1145566f2ce54cbbea35fa3', '', '', '', '2025-04-19 16:10:24', '2025-04-19 16:10:24', '租客');
INSERT INTO `front` VALUES (18, 'pppppp', 'e882b72bccfc2ad578c27b0d9b472a14', 'lkkl', '970001980@qq.com', 'https://housing-rent.oss-cn-guangzhou.aliyuncs.com/22f35e2b-5f06-49e9-b6be-532d46d25f47.jpeg', '2025-04-19 16:11:11', '2025-04-25 20:01:06', '租客');
INSERT INTO `front` VALUES (19, 'tttttt', 'e3ceb5881a0a1fdaad01296d7554868d', 'ddd', '970001980@qq.com', 'https://housing-rent.oss-cn-guangzhou.aliyuncs.com/0a1a3b67-1a9a-491b-8b36-1f2a5e60c87c.jpg', '2025-04-19 16:53:53', '2025-04-19 17:03:09', '租客');
INSERT INTO `front` VALUES (20, '111111', '96e79218965eb72c92a549dd5a330112', '', '', '', '2025-04-19 17:04:17', '2025-04-19 17:04:17', '租客');
INSERT INTO `front` VALUES (21, 'qqqqqq', 'd785c99d298a4e9e6e13fe99e602ef42', 'nicheng2', '970001980@qq.com', 'https://housing-rent.oss-cn-guangzhou.aliyuncs.com/2778811c-3463-43b9-b27f-012e4dd43095.jpg', '2025-04-19 17:28:32', '2025-04-19 17:29:24', '租客');
INSERT INTO `front` VALUES (23, 'aaaaaa', '0b4e7a0e5fe84ad35fb5f95b9ceeac79', 'nicheng2', '12311231@qq.com', 'https://housing-rent.oss-cn-guangzhou.aliyuncs.com/7ed6e5cf-ce3d-4a3f-9e93-1cc29e880df8.jpg', '2025-04-19 17:31:30', '2025-04-19 17:32:25', '租客');
INSERT INTO `front` VALUES (24, 'bbbbbb', '0b4e7a0e5fe84ad35fb5f95b9ceeac79', '用户B', '12311231@qq.com', 'https://housing-rent.oss-cn-guangzhou.aliyuncs.com/f80a8e4e-4cac-4596-9074-ceb8577b9ed1.jpg', '2025-04-19 17:50:07', '2025-04-19 17:50:42', '租客');
INSERT INTO `front` VALUES (25, 'yyyyyy', '94e7d712742adbbb7a73a1d52a7cc1a9', 'uu', '12311231@qq.com', '', '2025-04-25 02:13:52', '2025-04-25 02:15:29', '租客');
INSERT INTO `front` VALUES (26, '454545', '9ea5e6f10d48803ae38499c0d5e6d93f', '昵称', 'moyuxiugai@123123.com', 'https://housing-rent.oss-cn-guangzhou.aliyuncs.com/e830eee7-2fa1-43ad-8ca2-a315f21db017.jpg', '2025-04-27 13:47:39', '2025-04-27 14:03:34', '租客');
INSERT INTO `front` VALUES (27, '阿斯顿阿斯顿', '4297f44b13955235245b2497399d7a93', '', '', '', '2025-04-27 14:53:09', '2025-04-27 14:53:09', '租客');

-- ----------------------------
-- Table structure for house
-- ----------------------------
DROP TABLE IF EXISTS `house`;
CREATE TABLE `house`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '房源标题',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '详细描述',
  `price` decimal(10, 2) NOT NULL COMMENT '月租金',
  `area_size` double NULL DEFAULT NULL COMMENT '面积(m²)',
  `address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '详细地址',
  `city` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所在城市',
  `room_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '户型',
  `main_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '主图URL',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of house
-- ----------------------------

-- ----------------------------
-- Table structure for houserent
-- ----------------------------
DROP TABLE IF EXISTS `houserent`;
CREATE TABLE `houserent`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `title` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '房屋标题',
  `content` varchar(10000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '房屋内容',
  `cover_img` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '房屋封面',
  `state` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '草稿' COMMENT '房屋状态：在售或者已售出',
  `houserent_id` int UNSIGNED NULL DEFAULT NULL COMMENT '房屋分类ID',
  `create_user` int UNSIGNED NOT NULL COMMENT '创建人ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '租金',
  `area` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '房屋面积',
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地理位置',
  `tags` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标签',
  `region` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '区域代码',
  `audit_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '待审核' COMMENT '审核状态：待审核、已通过、已拒绝',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_houserent_rent`(`houserent_id` ASC) USING BTREE,
  INDEX `fk_houserent_user`(`create_user` ASC) USING BTREE,
  CONSTRAINT `houserent_ibfk_1` FOREIGN KEY (`houserent_id`) REFERENCES `rent` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `houserent_ibfk_2` FOREIGN KEY (`create_user`) REFERENCES `front` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 71 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '房源表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of houserent
-- ----------------------------
INSERT INTO `houserent` VALUES (48, '新房源111', '<p>房源详情</p>', 'https://housing-rent.oss-cn-guangzhou.aliyuncs.com/3ba5157c-9f9b-4c41-9a09-15e717b3f60b.jpeg', '在售', 30, 13, '2025-04-19 17:40:30', '2025-04-19 17:40:30', 111.00, NULL, NULL, NULL, NULL, '已通过');
INSERT INTO `houserent` VALUES (49, '房源22', '<p>房源22</p>', 'https://housing-rent.oss-cn-guangzhou.aliyuncs.com/c7e55eee-0bf2-4eff-ab79-92d268da8278.jpeg', '在售', 30, 13, '2025-04-19 17:40:49', '2025-04-19 17:40:49', 222.00, NULL, NULL, NULL, NULL, '已通过');
INSERT INTO `houserent` VALUES (54, '昂贵房', '<p>详情详情详情</p>', 'https://housing-rent.oss-cn-guangzhou.aliyuncs.com/dd705f7d-6799-4f5f-bc7b-e88e34c8b9ce.jpg', '已售空', 30, 13, '2025-04-19 17:54:18', '2025-04-19 17:56:23', 6666.00, NULL, NULL, NULL, NULL, '已拒绝');
INSERT INTO `houserent` VALUES (56, '新房源', '<p>333</p>', 'https://housing-rent.oss-cn-guangzhou.aliyuncs.com/62942d9d-414f-4ebb-bf2d-c3e779c10fa2.jpeg', '已售空', 30, 13, '2025-04-25 02:27:41', '2025-04-25 20:00:40', 222.00, NULL, NULL, NULL, NULL, '已拒绝');
INSERT INTO `houserent` VALUES (58, '审核222', '<p>222</p>', 'https://housing-rent.oss-cn-guangzhou.aliyuncs.com/ce932f3a-5498-40fc-b925-0ea02e9df562.jpg', '在售', 22, 13, '2025-04-26 15:43:13', '2025-04-26 15:43:13', 222.00, NULL, NULL, NULL, NULL, '已拒绝');
INSERT INTO `houserent` VALUES (59, '二次审核', '<p>333</p>', 'https://housing-rent.oss-cn-guangzhou.aliyuncs.com/f3bbb5a6-64ef-4acc-9649-79f54db556a7.jpg', '已售空', 30, 13, '2025-04-26 16:19:25', '2025-04-27 01:14:25', 333.00, NULL, NULL, NULL, NULL, '已通过');
INSERT INTO `houserent` VALUES (60, '三次审核', '<p>33</p>', 'https://housing-rent.oss-cn-guangzhou.aliyuncs.com/bd4b0b37-9cd5-40b3-9836-4952b9af1ab2.jpg', '已售空', 32, 13, '2025-04-26 16:22:18', '2025-04-27 13:47:51', 333.00, NULL, NULL, NULL, NULL, '已通过');
INSERT INTO `houserent` VALUES (62, '已售出等条件房源', '<p>888</p>', 'https://housing-rent.oss-cn-guangzhou.aliyuncs.com/123805ca-8177-4dd8-a3cf-8767e0fb72a8.jpeg', '已售空', 30, 13, '2025-04-27 00:14:31', '2025-04-27 00:37:47', 333.00, NULL, NULL, NULL, NULL, '已通过');
INSERT INTO `houserent` VALUES (63, '房东发布房源', '<p>详情</p>', 'https://housing-rent.oss-cn-guangzhou.aliyuncs.com/128d4263-e7ae-40d3-9b60-0648f700c06d.jpeg', '在售', 30, 13, '2025-04-27 00:44:54', '2025-04-27 00:44:54', 333.00, NULL, NULL, NULL, NULL, '待审核');
INSERT INTO `houserent` VALUES (64, '审核通过', '<p>审核通过</p>', 'https://housing-rent.oss-cn-guangzhou.aliyuncs.com/aea21a15-bdcd-4511-a260-9df3339cd481.jpeg', '已售空', 30, 13, '2025-04-27 01:26:12', '2025-04-27 01:47:07', 222.00, NULL, NULL, NULL, NULL, '已通过');
INSERT INTO `houserent` VALUES (66, '房源1', '<p>我是详情</p>', 'https://housing-rent.oss-cn-guangzhou.aliyuncs.com/a398af5b-8961-4756-9f16-a61154bda9d4.jpeg', '已售空', 30, 13, '2025-05-11 00:36:55', '2025-05-11 00:37:16', 222.00, NULL, NULL, NULL, NULL, '已通过');
INSERT INTO `houserent` VALUES (69, '新房源5.11', '<p>999</p>', 'https://housing-rent.oss-cn-guangzhou.aliyuncs.com/e13eb9b0-611a-4def-8a45-883a01f2407b.jpeg', '已售空', 30, 13, '2025-05-11 09:02:46', '2025-05-11 10:00:46', 456.00, NULL, NULL, NULL, NULL, '已通过');
INSERT INTO `houserent` VALUES (70, '审核222', '<p>9999999</p>', 'https://housing-rent.oss-cn-guangzhou.aliyuncs.com/9194b2b0-4fbe-4cf9-938a-decc319689a5.jpeg', '在售', 30, 13, '2025-05-11 10:03:14', '2025-05-11 10:03:14', 5555.00, NULL, NULL, NULL, NULL, '已通过');

-- ----------------------------
-- Table structure for rent
-- ----------------------------
DROP TABLE IF EXISTS `rent`;
CREATE TABLE `rent`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `rent_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名称',
  `rent_alias` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类别名',
  `create_user` int UNSIGNED NOT NULL COMMENT '创建人ID',
  `create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_rent_user`(`create_user` ASC) USING BTREE,
  CONSTRAINT `rent_ibfk_1` FOREIGN KEY (`create_user`) REFERENCES `front` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '房源标签表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of rent
-- ----------------------------
INSERT INTO `rent` VALUES (22, '皇1', 'hg1', 13, '2025-04-14 19:42:53', '2025-04-14 19:42:53');
INSERT INTO `rent` VALUES (30, '近地铁', 'jdt', 13, '2025-03-11 11:18:02', '2025-03-11 11:18:02');
INSERT INTO `rent` VALUES (31, '体育馆旁', 'tygp', 13, '2025-03-11 17:37:53', '2025-03-11 17:37:53');
INSERT INTO `rent` VALUES (32, '测试', 'cs', 13, '2025-03-24 20:59:54', '2025-03-24 20:59:54');
INSERT INTO `rent` VALUES (33, '管理员测试', 'glycs', 2, '2025-03-25 16:22:50', '2025-03-25 16:22:50');
INSERT INTO `rent` VALUES (34, '555', '555', 13, '2025-03-30 12:37:49', '2025-03-30 12:37:49');
INSERT INTO `rent` VALUES (35, '773', '773', 13, '2025-04-19 16:16:13', '2025-04-19 16:16:13');
INSERT INTO `rent` VALUES (37, '新分类', 'xfl', 13, '2025-04-19 17:33:52', '2025-04-19 17:33:52');
INSERT INTO `rent` VALUES (38, '分类的1', '分类的', 13, '2025-04-19 17:54:33', '2025-04-19 17:54:34');
INSERT INTO `rent` VALUES (39, '新标签', 'xbq', 13, '2025-04-25 02:16:58', '2025-04-25 02:16:58');

-- ----------------------------
-- Table structure for rent_order
-- ----------------------------
DROP TABLE IF EXISTS `rent_order`;
CREATE TABLE `rent_order`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `house_id` int UNSIGNED NOT NULL COMMENT '房源ID',
  `tenant_id` int UNSIGNED NOT NULL COMMENT '租客ID',
  `start_date` date NOT NULL COMMENT '租赁开始时间',
  `end_date` date NOT NULL COMMENT '租赁结束时间',
  `total_price` decimal(10, 2) NOT NULL COMMENT '总租金',
  `order_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '待支付' COMMENT '订单状态（待支付、已支付、已取消）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_order_house`(`house_id` ASC) USING BTREE,
  INDEX `fk_order_tenant`(`tenant_id` ASC) USING BTREE,
  CONSTRAINT `fk_order_house` FOREIGN KEY (`house_id`) REFERENCES `houserent` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_order_tenant` FOREIGN KEY (`tenant_id`) REFERENCES `front` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '租赁订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of rent_order
-- ----------------------------
INSERT INTO `rent_order` VALUES (17, 54, 24, '2025-04-19', '2025-05-19', 6666.00, '已支付', '2025-04-19 17:56:23', '2025-04-19 17:56:23');
INSERT INTO `rent_order` VALUES (19, 56, 18, '2025-04-25', '2025-05-25', 222.00, '已支付', '2025-04-25 20:00:40', '2025-04-25 20:00:40');
INSERT INTO `rent_order` VALUES (21, 62, 12, '2025-04-27', '2025-05-27', 333.00, '已支付', '2025-04-27 00:37:47', '2025-04-27 00:37:47');
INSERT INTO `rent_order` VALUES (22, 59, 12, '2025-04-27', '2025-05-27', 333.00, '已支付', '2025-04-27 01:14:25', '2025-04-27 01:14:25');
INSERT INTO `rent_order` VALUES (23, 64, 12, '2025-04-27', '2025-05-27', 222.00, '已支付', '2025-04-27 01:47:07', '2025-04-27 01:47:07');
INSERT INTO `rent_order` VALUES (24, 60, 26, '2025-04-27', '2025-05-27', 333.00, '已支付', '2025-04-27 13:47:51', '2025-04-27 13:47:51');
INSERT INTO `rent_order` VALUES (25, 66, 12, '2025-05-11', '2025-06-11', 222.00, '已支付', '2025-05-11 00:37:16', '2025-05-11 00:37:16');
INSERT INTO `rent_order` VALUES (27, 69, 12, '2025-05-11', '2025-06-11', 456.00, '已支付', '2025-05-11 10:00:46', '2025-05-11 10:00:46');

-- ----------------------------
-- Table structure for repair_progress
-- ----------------------------
DROP TABLE IF EXISTS `repair_progress`;
CREATE TABLE `repair_progress`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `request_id` int UNSIGNED NOT NULL COMMENT '维修请求ID',
  `operator_id` int UNSIGNED NOT NULL COMMENT '操作人ID',
  `action` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作内容',
  `details` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '详细说明',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_progress_request`(`request_id` ASC) USING BTREE,
  INDEX `fk_progress_operator`(`operator_id` ASC) USING BTREE,
  CONSTRAINT `fk_progress_operator` FOREIGN KEY (`operator_id`) REFERENCES `front` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_progress_request` FOREIGN KEY (`request_id`) REFERENCES `repair_request` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '维修进度记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of repair_progress
-- ----------------------------

-- ----------------------------
-- Table structure for repair_request
-- ----------------------------
DROP TABLE IF EXISTS `repair_request`;
CREATE TABLE `repair_request`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `house_id` int UNSIGNED NOT NULL COMMENT '房屋ID',
  `user_id` int UNSIGNED NOT NULL COMMENT '报修用户ID',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '报修标题',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '问题描述',
  `images` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '图片URL(多个用逗号分隔)',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'pending' COMMENT '状态: pending/processing/completed',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_repair_house`(`house_id` ASC) USING BTREE,
  INDEX `fk_repair_user`(`user_id` ASC) USING BTREE,
  CONSTRAINT `fk_repair_house` FOREIGN KEY (`house_id`) REFERENCES `houserent` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_repair_user` FOREIGN KEY (`user_id`) REFERENCES `front` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 39 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '维修请求表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of repair_request
-- ----------------------------
INSERT INTO `repair_request` VALUES (32, 54, 24, '维修第三步', '111', 'https://housing-rent.oss-cn-guangzhou.aliyuncs.com/8ddf0927-84fd-4559-b7dd-e4e76cf4ebe7.jpg', 'completed', '2025-04-19 17:56:40', '2025-04-19 17:56:49');
INSERT INTO `repair_request` VALUES (33, 54, 25, '777', '7777', 'https://housing-rent.oss-cn-guangzhou.aliyuncs.com/7a2bbd62-a495-4b78-b2df-47995d32226f.jpeg', 'processing', '2025-04-25 02:15:05', '2025-04-25 02:18:14');
INSERT INTO `repair_request` VALUES (35, 62, 12, '我是维修标题', '我是维修问题', 'https://housing-rent.oss-cn-guangzhou.aliyuncs.com/8a971498-b8ff-4abc-bd11-dd4aa31e6915.jpeg', 'completed', '2025-04-27 00:38:13', '2025-04-27 00:40:18');
INSERT INTO `repair_request` VALUES (36, 64, 13, '有问题', '有问题', 'https://housing-rent.oss-cn-guangzhou.aliyuncs.com/ec62fff4-30fd-4721-87bb-21569b113a4d.jpeg', 'processing', '2025-04-27 13:51:37', '2025-04-27 13:55:33');
INSERT INTO `repair_request` VALUES (37, 66, 12, '房源1出问题', 'www', 'https://housing-rent.oss-cn-guangzhou.aliyuncs.com/65ca46ee-d21d-4649-a22c-edc240f49a30.jpg', 'processing', '2025-05-11 00:38:31', '2025-05-11 00:38:44');
INSERT INTO `repair_request` VALUES (38, 69, 12, '4444', 'yyyyy', 'https://housing-rent.oss-cn-guangzhou.aliyuncs.com/630b2e6d-1c77-411a-a3f2-02a2f6fec7c1.jpeg', 'processing', '2025-05-11 10:02:14', '2025-05-11 10:02:27');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `nickname` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '昵称',
  `email` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '邮箱',
  `user_pic` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '头像',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (2, 'moyuchishi', 'e10adc3949ba59abbe56e057f20f883e', '墨鱼', 'moyuxiugai@123123.com', 'https://housing-rent.oss-cn-guangzhou.aliyuncs.com/44473850-06f6-4ae7-b284-ecd623d716b7.jpg', '2025-02-15 22:46:02', '2025-04-27 00:33:02', '管理员');
INSERT INTO `user` VALUES (3, '123456', 'e10adc3949ba59abbe56e057f20f883e', '', '', '', '2025-02-24 14:30:35', '2025-02-24 14:30:35', '房东');
INSERT INTO `user` VALUES (4, '666666', 'f379eaf3c831b04de153469d1bec345e', 'moyu', '123@itcast.cn', '', '2025-02-24 22:48:07', '2025-04-25 00:02:41', NULL);
INSERT INTO `user` VALUES (5, '111111', '96e79218965eb72c92a549dd5a330112', '', '', '', '2025-02-26 12:58:17', '2025-02-26 12:58:17', NULL);
INSERT INTO `user` VALUES (6, '555555', '5b1b68a9abf4d2cd155c81a9225fd158', '', '', '', '2025-02-26 12:59:43', '2025-02-26 12:59:43', NULL);
INSERT INTO `user` VALUES (8, '888888', '21218cca77804d2ba1922c33e0151105', '', '', '', '2025-02-26 13:00:45', '2025-02-26 13:00:45', NULL);
INSERT INTO `user` VALUES (9, 'qianduanyonghu', 'e10adc3949ba59abbe56e057f20f883e', '', '', '', '2025-02-26 13:23:07', '2025-02-26 13:23:07', NULL);
INSERT INTO `user` VALUES (11, 'tttttt', 'bcc720f2981d1a68dbd66ffd67560c37', '', '', '', '2025-02-26 13:58:55', '2025-02-26 13:58:55', NULL);
INSERT INTO `user` VALUES (12, '12456', 'e10adc3949ba59abbe56e057f20f883e', '', '', '', '2025-04-24 23:52:25', '2025-04-24 23:52:25', NULL);

SET FOREIGN_KEY_CHECKS = 1;
