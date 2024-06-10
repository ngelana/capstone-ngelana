/*
  Warnings:

  - The primary key for the `places` table will be changed. If it partially fails, the table could be left without primary key constraint.
  - You are about to alter the column `id` on the `places` table. The data in that column could be lost. The data in that column will be cast from `VarChar(191)` to `Int`.
  - You are about to alter the column `placeId` on the `planplaces` table. The data in that column could be lost. The data in that column will be cast from `VarChar(191)` to `Int`.
  - You are about to alter the column `placeId` on the `userreview` table. The data in that column could be lost. The data in that column will be cast from `VarChar(191)` to `Int`.

*/
-- DropForeignKey
ALTER TABLE `planplaces` DROP FOREIGN KEY `PlanPlaces_placeId_fkey`;

-- DropForeignKey
ALTER TABLE `userreview` DROP FOREIGN KEY `UserReview_placeId_fkey`;

-- AlterTable
ALTER TABLE `places` DROP PRIMARY KEY,
    MODIFY `id` INTEGER NOT NULL AUTO_INCREMENT,
    ADD PRIMARY KEY (`id`);

-- AlterTable
ALTER TABLE `planplaces` MODIFY `placeId` INTEGER NOT NULL;

-- AlterTable
ALTER TABLE `userreview` MODIFY `placeId` INTEGER NOT NULL;

-- AddForeignKey
ALTER TABLE `UserReview` ADD CONSTRAINT `UserReview_placeId_fkey` FOREIGN KEY (`placeId`) REFERENCES `Places`(`id`) ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `PlanPlaces` ADD CONSTRAINT `PlanPlaces_placeId_fkey` FOREIGN KEY (`placeId`) REFERENCES `Places`(`id`) ON DELETE RESTRICT ON UPDATE CASCADE;
