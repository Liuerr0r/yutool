DROP TABLE IF EXISTS `audit_record`;
CREATE TABLE `audit_record` (
    `id` BIGINT(19) NOT NULL COMMENT '审计记录ID',
    `table_name` VARCHAR(100) DEFAULT NULL COMMENT '数据表名',
    `record_id` VARCHAR(32) DEFAULT NULL COMMENT '审计数据ID',
    `field_name` VARCHAR(60) DEFAULT NULL COMMENT '字段名',
    `field_description` VARCHAR(255) DEFAULT NULL COMMENT '字段描述',
    `old_value` VARCHAR(255) DEFAULT NULL COMMENT '更新之前的值',
    `new_value` VARCHAR(255) DEFAULT NULL COMMENT '更新之后的值',
    `status` VARCHAR(10) DEFAULT NULL COMMENT '审批状态',
    `created_time` DATETIME DEFAULT NULL COMMENT '创建时间',
    `created_by` VARCHAR(19) DEFAULT NULL COMMENT '创建ID',
    `last_modified_time` DATETIME DEFAULT NULL COMMENT '更新时间',
    `last_modified_by` VARCHAR(19) DEFAULT NULL COMMENT '更新人ID',
    PRIMARY KEY (`id`),
    KEY `idx_table_name` (`table_name`),
    KEY `idx_record_id` (`record_id`),
    KEY `idx_field_name` (`field_name`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '审计记录';