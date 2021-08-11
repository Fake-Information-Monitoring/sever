/*
 Navicat Premium Data Transfer

 Source Server         : fakenew
 Source Server Type    : MySQL
 Source Server Version : 50731
 Source Host           : 118.178.255.237:3306
 Source Schema         : fake_news

 Target Server Type    : MySQL
 Target Server Version : 50731
 File Encoding         : 65001

 Date: 12/08/2021 00:29:33
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` int(11) NOT NULL,
  `manage_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for certified_pool
-- ----------------------------
DROP TABLE IF EXISTS `certified_pool`;
CREATE TABLE `certified_pool`  (
  `user_id` int(11) NOT NULL,
  `certified_type` int(1) NOT NULL,
  `id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`) USING BTREE,
  CONSTRAINT `FKqbq6aa54y4fd8f498axqmmfen` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for commit
-- ----------------------------
DROP TABLE IF EXISTS `commit`;
CREATE TABLE `commit`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `commit_at` datetime(0) NOT NULL,
  `index` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK31873vxjeuj4jrm5wc7ttev9r`(`user_id`) USING BTREE,
  CONSTRAINT `FK31873vxjeuj4jrm5wc7ttev9r` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for enterprise_certified
-- ----------------------------
DROP TABLE IF EXISTS `enterprise_certified`;
CREATE TABLE `enterprise_certified`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `license` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK4usot39kstjjxirn07fb05mg`(`user_id`) USING BTREE,
  CONSTRAINT `FK4usot39kstjjxirn07fb05mg` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for fake_message_info
-- ----------------------------
DROP TABLE IF EXISTS `fake_message_info`;
CREATE TABLE `fake_message_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `worse_account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `worse_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `worse_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `worse_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `time` datetime(0) NULL DEFAULT NULL,
  `key_id` int(11) NOT NULL,
  `words` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKadhlnf78opcn3w62kx187g6e7`(`key_id`) USING BTREE,
  CONSTRAINT `FKadhlnf78opcn3w62kx187g6e7` FOREIGN KEY (`key_id`) REFERENCES `key` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 261 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for key
-- ----------------------------
DROP TABLE IF EXISTS `key`;
CREATE TABLE `key`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key_str` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `user_id` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `model_id` int(11) NULL DEFAULT NULL,
  `enterprise_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKfu0fb74m57byhk7okuxpe5ye4`(`user_id`) USING BTREE,
  INDEX `IDXcu2qvbxbdolkkb9ei2jjy1ihp`(`key_str`) USING BTREE,
  CONSTRAINT `FKfu0fb74m57byhk7okuxpe5ye4` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 134 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for model_data
-- ----------------------------
DROP TABLE IF EXISTS `model_data`;
CREATE TABLE `model_data`  (
  `model` blob NOT NULL,
  `model_id` int(11) NULL DEFAULT NULL,
  INDEX `FKochp8wul3mv75ge4tmd3k5lcv`(`model_id`) USING BTREE,
  CONSTRAINT `FKochp8wul3mv75ge4tmd3k5lcv` FOREIGN KEY (`model_id`) REFERENCES `model_info` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for model_info
-- ----------------------------
DROP TABLE IF EXISTS `model_info`;
CREATE TABLE `model_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key_id` int(11) NOT NULL,
  `model` int(11) NULL DEFAULT NULL,
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKtilfxwxroq3a0buhlime1wcbe`(`key_id`) USING BTREE,
  CONSTRAINT `FKtilfxwxroq3a0buhlime1wcbe` FOREIGN KEY (`key_id`) REFERENCES `key` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for person_certified
-- ----------------------------
DROP TABLE IF EXISTS `person_certified`;
CREATE TABLE `person_certified`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `card_id` varbinary(18) NOT NULL,
  `work` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKpeg9rl5ryc6bcy0q17s9e0er3`(`user_id`) USING BTREE,
  CONSTRAINT `FKpeg9rl5ryc6bcy0q17s9e0er3` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `avatar_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `last_actived_at` datetime(0) NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `phone_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `updated_at` datetime(0) NOT NULL,
  `ceritified_id` int(11) NULL DEFAULT NULL,
  `ceritified_type` int(1) NULL DEFAULT NULL,
  `ceritified_admin` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `IDX7q0t3vs48p6s072hxp46o8xwo`(`phone_number`) USING BTREE,
  INDEX `IDXoshmjvr6wht0bg9oivn75aajr`(`email`) USING BTREE,
  INDEX `FKndl38g5tb9ebqutl3n3uylh28`(`ceritified_admin`) USING BTREE,
  CONSTRAINT `FKndl38g5tb9ebqutl3n3uylh28` FOREIGN KEY (`ceritified_admin`) REFERENCES `admin` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
