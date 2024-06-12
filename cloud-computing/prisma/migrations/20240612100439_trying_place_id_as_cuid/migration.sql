/*
  Warnings:

  - The primary key for the `place` table will be changed. If it partially fails, the table could be left without primary key constraint.
  - The primary key for the `placesonplans` table will be changed. If it partially fails, the table could be left without primary key constraint.

*/
-- DropForeignKey
ALTER TABLE `placesonplans` DROP FOREIGN KEY `PlacesOnPlans_placeId_fkey`;

-- DropForeignKey
ALTER TABLE `userreview` DROP FOREIGN KEY `UserReview_placeId_fkey`;

-- AlterTable
ALTER TABLE `place` DROP PRIMARY KEY,
    MODIFY `id` VARCHAR(191) NOT NULL,
    ADD PRIMARY KEY (`id`);

-- AlterTable
ALTER TABLE `placesonplans` DROP PRIMARY KEY,
    MODIFY `placeId` VARCHAR(191) NOT NULL,
    ADD PRIMARY KEY (`planId`, `placeId`);

-- AlterTable
ALTER TABLE `userreview` MODIFY `placeId` VARCHAR(191) NOT NULL;

-- AddForeignKey
ALTER TABLE `UserReview` ADD CONSTRAINT `UserReview_placeId_fkey` FOREIGN KEY (`placeId`) REFERENCES `Place`(`id`) ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `PlacesOnPlans` ADD CONSTRAINT `PlacesOnPlans_placeId_fkey` FOREIGN KEY (`placeId`) REFERENCES `Place`(`id`) ON DELETE RESTRICT ON UPDATE CASCADE;
