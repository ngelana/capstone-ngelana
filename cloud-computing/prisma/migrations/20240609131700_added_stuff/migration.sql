/*
  Warnings:

  - You are about to drop the column `preferences` on the `preference` table. All the data in the column will be lost.
  - The primary key for the `userpreferences` table will be changed. If it partially fails, the table could be left without primary key constraint.
  - Added the required column `date` to the `Plan` table without a default value. This is not possible if the table is not empty.
  - Added the required column `updatedAt` to the `Plan` table without a default value. This is not possible if the table is not empty.
  - Added the required column `name` to the `Preference` table without a default value. This is not possible if the table is not empty.

*/
-- AlterTable
ALTER TABLE `plan` ADD COLUMN `createdAt` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    ADD COLUMN `date` DATETIME(3) NOT NULL,
    ADD COLUMN `updatedAt` DATETIME(3) NOT NULL;

-- AlterTable
ALTER TABLE `preference` DROP COLUMN `preferences`,
    ADD COLUMN `name` VARCHAR(191) NOT NULL;

-- AlterTable
ALTER TABLE `userpreferences` DROP PRIMARY KEY,
    ADD PRIMARY KEY (`userId`);

-- CreateTable
CREATE TABLE `PlanPlaces` (
    `id` VARCHAR(191) NOT NULL,
    `planId` VARCHAR(191) NOT NULL,
    `placeId` VARCHAR(191) NOT NULL,

    UNIQUE INDEX `PlanPlaces_planId_placeId_key`(`planId`, `placeId`),
    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- AddForeignKey
ALTER TABLE `PlanPlaces` ADD CONSTRAINT `PlanPlaces_planId_fkey` FOREIGN KEY (`planId`) REFERENCES `Plan`(`id`) ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `PlanPlaces` ADD CONSTRAINT `PlanPlaces_placeId_fkey` FOREIGN KEY (`placeId`) REFERENCES `Places`(`id`) ON DELETE RESTRICT ON UPDATE CASCADE;
