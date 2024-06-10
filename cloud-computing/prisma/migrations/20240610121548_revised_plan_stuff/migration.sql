/*
  Warnings:

  - You are about to drop the `places` table. If the table is not empty, all the data it contains will be lost.
  - You are about to drop the `planplaces` table. If the table is not empty, all the data it contains will be lost.

*/
-- DropForeignKey
ALTER TABLE `planplaces` DROP FOREIGN KEY `PlanPlaces_placeId_fkey`;

-- DropForeignKey
ALTER TABLE `planplaces` DROP FOREIGN KEY `PlanPlaces_planId_fkey`;

-- DropForeignKey
ALTER TABLE `userreview` DROP FOREIGN KEY `UserReview_placeId_fkey`;

-- DropTable
DROP TABLE `places`;

-- DropTable
DROP TABLE `planplaces`;

-- CreateTable
CREATE TABLE `Place` (
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(191) NOT NULL,
    `address` VARCHAR(191) NOT NULL,
    `type` VARCHAR(191) NOT NULL,
    `url` VARCHAR(191) NULL,
    `contact` VARCHAR(191) NULL,

    UNIQUE INDEX `Place_name_key`(`name`),
    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `PlacesOnPlans` (
    `planId` VARCHAR(191) NOT NULL,
    `placeId` INTEGER NOT NULL,
    `assignedAt` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `assignedBy` VARCHAR(191) NOT NULL,

    PRIMARY KEY (`planId`, `placeId`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- AddForeignKey
ALTER TABLE `UserReview` ADD CONSTRAINT `UserReview_placeId_fkey` FOREIGN KEY (`placeId`) REFERENCES `Place`(`id`) ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `PlacesOnPlans` ADD CONSTRAINT `PlacesOnPlans_planId_fkey` FOREIGN KEY (`planId`) REFERENCES `Plan`(`id`) ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `PlacesOnPlans` ADD CONSTRAINT `PlacesOnPlans_placeId_fkey` FOREIGN KEY (`placeId`) REFERENCES `Place`(`id`) ON DELETE RESTRICT ON UPDATE CASCADE;
