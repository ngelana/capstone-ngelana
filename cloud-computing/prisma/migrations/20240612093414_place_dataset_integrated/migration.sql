/*
  Warnings:

  - You are about to drop the column `contact` on the `place` table. All the data in the column will be lost.
  - You are about to drop the column `type` on the `place` table. All the data in the column will be lost.
  - The primary key for the `userpreferences` table will be changed. If it partially fails, the table could be left without primary key constraint.
  - Added the required column `latitude` to the `Place` table without a default value. This is not possible if the table is not empty.
  - Added the required column `longitude` to the `Place` table without a default value. This is not possible if the table is not empty.
  - Added the required column `urlPlaceholder` to the `Preference` table without a default value. This is not possible if the table is not empty.
  - Added the required column `assignedBy` to the `UserPreferences` table without a default value. This is not possible if the table is not empty.

*/
-- AlterTable
ALTER TABLE `place` DROP COLUMN `contact`,
    DROP COLUMN `type`,
    ADD COLUMN `latitude` DOUBLE NOT NULL,
    ADD COLUMN `longitude` DOUBLE NOT NULL,
    ADD COLUMN `phone` VARCHAR(191) NULL,
    ADD COLUMN `priceLevel` INTEGER NULL,
    ADD COLUMN `primaryTypes` VARCHAR(191) NULL,
    ADD COLUMN `rating` DOUBLE NULL,
    ADD COLUMN `ratingCount` INTEGER NULL,
    ADD COLUMN `status` VARCHAR(191) NULL,
    ADD COLUMN `types` VARCHAR(191) NULL;

-- AlterTable
ALTER TABLE `plan` ADD COLUMN `name` VARCHAR(191) NULL;

-- AlterTable
ALTER TABLE `preference` ADD COLUMN `urlPlaceholder` VARCHAR(191) NOT NULL;

-- AlterTable
ALTER TABLE `userpreferences` DROP PRIMARY KEY,
    ADD COLUMN `assignedAt` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    ADD COLUMN `assignedBy` VARCHAR(191) NOT NULL,
    ADD PRIMARY KEY (`userId`, `preferenceId`);
