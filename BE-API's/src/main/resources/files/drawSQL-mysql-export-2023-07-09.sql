CREATE TABLE `Role`(
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL
);
CREATE TABLE `Log`(
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `invoice_id` BIGINT NOT NULL,
    `created_at` DATETIME NOT NULL,
    `action` VARCHAR(255) NOT NULL,
    `description` VARCHAR(255) NOT NULL
);
CREATE TABLE `Invoice`(
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY
);
CREATE TABLE `User`(
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `isVerified` TINYINT(1) NOT NULL,
    `column_6` BIGINT NOT NULL
);
CREATE TABLE `user_roles`(
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `role_id` BIGINT NOT NULL
);
ALTER TABLE
    `user_roles` ADD CONSTRAINT `user_roles_user_id_foreign` FOREIGN KEY(`user_id`) REFERENCES `User`(`id`);
ALTER TABLE
    `user_roles` ADD CONSTRAINT `user_roles_role_id_foreign` FOREIGN KEY(`role_id`) REFERENCES `Role`(`id`);
ALTER TABLE
    `Log` ADD CONSTRAINT `log_invoice_id_foreign` FOREIGN KEY(`invoice_id`) REFERENCES `Invoice`(`id`);
ALTER TABLE
    `Log` ADD CONSTRAINT `log_user_id_foreign` FOREIGN KEY(`user_id`) REFERENCES `User`(`id`);