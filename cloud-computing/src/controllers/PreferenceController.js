const express = require("express");
const prisma = require("../db");
const router = express.Router();
const { accessValidation } = require("../services/AuthServices");

// Get preferences list
router.get("/", accessValidation, async (req, res) => {
  try {
    const result = await prisma.preference.findMany();
    return res.status(200).json({
      data: result,
      message: "Preference Listed!",
    });
  } catch (error) {
    return res.status(500).json({
      message: `Error getting list of preference, Error:${err.message}`,
    });
  }
});

// Create user preferences WORKING !!!!!!!!!!!
router.post("/", accessValidation, async (req, res) => {
  const { userId, preferenceIds } = req.body;
  if (!preferenceIds || !Array.isArray(preferenceIds)) {
    return res.status(400).json({ error: "Invalid input data" });
  }

  try {
    // Check if the user exists
    const user = await prisma.user.findUnique({
      where: { id: userId },
    });

    if (!user) {
      return res.status(404).json({ error: "User not found" });
    }

    // Fetch preferences from the database
    const preferences = await prisma.preference.findMany({
      where: {
        id: {
          in: preferenceIds,
        },
      },
    });

    if (preferences.length !== preferenceIds.length) {
      return res
        .status(400)
        .json({ error: "One or more preferences not found" });
    }

    // Map preference IDs to user preferences
    const userPreferencesData = preferenceIds.map((preferenceId) => ({
      userId,
      preferenceId,
      assignedBy: userId,
    }));

    // Insert user preferences
    await prisma.userPreferences.createMany({
      data: userPreferencesData,
      skipDuplicates: true,
    });

    // Fetch the newly created user preferences with preference details
    const userPreferences = await prisma.userPreferences.findMany({
      where: {
        userId: userId,
        preferenceId: {
          in: preferenceIds,
        },
      },
      include: {
        preference: true,
      },
    });

    res.status(201).json(userPreferences);
  } catch (error) {
    console.error(error);
    res
      .status(500)
      .json({ error: "An error occurred while adding preferences" });
  }
});

router.get("/:id", async (req, res) => {
  const { id } = req.params;

  try {
    // Check if the user exists + fetch userPreferences
    const user = await prisma.user.findUnique({
      where: { id },
      select: {
        id: true,
        name: true,
        userPreferences: {
          select: { preference: true },
        },
      },
    });

    if (!user) {
      return res.status(404).json({ error: "User not found" });
    }

    // // Fetch user preferences with related preference details
    // const userPreferences = await prisma.userPreferences.findMany({
    //   where: {
    //     userId: id,
    //   },
    //   include: {
    //     preference: true,
    //   },
    // });

    res
      .status(200)
      .json({ user, message: `Listed preferences from user: ${id}` });
  } catch (error) {
    console.error(error);
    res
      .status(500)
      .json({ error: "An error occurred while fetching preferences" });
  }
});

router.patch("/:id", async (req, res) => {
  const { id } = req.params;
  const { preferenceIds } = req.body;

  if (!preferenceIds || !Array.isArray(preferenceIds)) {
    return res.status(400).json({ error: "Invalid input data" });
  }

  try {
    // Check if the user exists
    const user = await prisma.user.findUnique({
      where: { id },
    });

    if (!user) {
      return res.status(404).json({ error: "User not found" });
    }

    // Fetch current user preferences
    const currentPreferences = await prisma.userPreferences.findMany({
      where: { userId: id },
    });

    const currentPreferenceIds = currentPreferences.map(
      (up) => up.preferenceId
    );

    // Determine preferences to add and to remove
    const preferencesToAdd = preferenceIds.filter(
      (id) => !currentPreferenceIds.includes(id)
    );
    const preferencesToRemove = currentPreferenceIds.filter(
      (id) => !preferenceIds.includes(id)
    );

    // Remove old preferences
    await prisma.userPreferences.deleteMany({
      where: {
        userId: id,
        preferenceId: { in: preferencesToRemove },
      },
    });

    // Add new preferences
    const newUserPreferences = preferencesToAdd.map((preferenceId) => ({
      userId: id,
      preferenceId,
      assignedBy: id,
    }));

    await prisma.userPreferences.createMany({
      data: newUserPreferences,
      skipDuplicates: true,
    });

    // Fetch updated user preferences with related preference details
    const updatedUserPreferences = await prisma.userPreferences.findMany({
      where: { userId: id },
      include: { preference: true },
    });

    res.status(200).json({
      updatedUserPreferences,
      message: "Successfully updated preferences!",
    });
  } catch (error) {
    console.error(error);
    res
      .status(500)
      .json({ error: "An error occurred while updating preferences" });
  }
});

module.exports = router;
