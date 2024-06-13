const express = require("express");
const prisma = require("../db");
const router = express.Router();
const { accessValidation } = require("../services/AuthServices");
const parseDate = require("../services/UtilServices");

// Placeholder function to simulate ML model for generating placeIds
const generatePlaceIds = async () => {
  // Simulate fetching or generating place IDs
  // Replace this with your ML model logic later
  const places = await prisma.place.findMany({
    select: { id: true },
    take: 3, // Simulate getting 3 place IDs, adjust as needed
  });
  return places.map((place) => place.id);
};

// Get list of plans for a user
router.get("/", accessValidation, async (req, res) => {
  const { userId } = req.body;

  try {
    const plans = await prisma.plan.findMany({
      where: { userId },
      include: {
        places: {
          include: {
            place: true, // Include place data
          },
        },
      },
    });

    res.status(200).json({
      data: plans,
      message: "Plans listed successfully!",
    });
  } catch (error) {
    res.status(500).json({
      message: "Error retrieving plans",
      error: error.message,
    });
  }
});

// Create a new plan
router.post("/", accessValidation, async (req, res) => {
  const { date, userId } = req.body; // Expecting date in the request body
  const formattedDate = date ? parseDate(date) : null;
  const placeIds = await generatePlaceIds();

  console.log(placeIds);
  console.log(placeIds[0]);
  if (!date) {
    return res.status(400).json({
      message: "Date is required",
    });
  }
  try {
    const createPlan = await prisma.plan.create({
      data: {
        date: formattedDate,
        userId,
        places: {
          create: placeIds.map((placeId) => ({
            placeId,
            assignedBy: userId,
            assignedAt: new Date(),
          })),
        },
      },
      include: {
        places: {
          include: {
            place: true,
          },
        },
      },
    });

    return res.status(201).json({
      data: createPlan,
      message: "Success created a plan!",
    });
  } catch (error) {
    res.status(500).json({
      message: "Error creating a plan",
      error: error.message,
    });
  }
});

router.post("/recommend", accessValidation, async (req, res) => {
  const { date, userId } = req.body;

  try {
    if (!date) {
      return res.status(400).json({
        message: "Date is required",
      });
    }
    const userExists = await prisma.user.findUnique({
      where: { id: userId },
    });

    if (!userExists) {
      return res.status(400).json({
        message: "User ID does not exist",
      });
    }
    const placeIds = await generatePlaceIds();
    const places = await prisma.place.findMany({
      where: {
        id: { in: placeIds },
      },
    });

    return res.status(200).json({
      places,
      message: "Successfully generated recommendations",
    });
  } catch (error) {
    return res.status(500).json({
      message: "Error generating recommendations",
      error: error.message,
    });
  }
});

router.post("/finalize", accessValidation, async (req, res) => {
  const { date, userId, places, planName } = req.body;
  const formattedDate = date ? parseDate(date) : null;
  if (!date || !places || !planName) {
    return res.status(400).json({
      message: "Date, places, and plan name are required",
    });
  }

  try {
    // Check if the userId exists
    const userExists = await prisma.user.findUnique({
      where: { id: userId },
    });

    if (!userExists) {
      return res.status(400).json({
        message: "User ID does not exist",
      });
    }
    const createPlan = await prisma.plan.create({
      data: {
        date: formattedDate,
        userId,
        name: planName,
        places: {
          create: places.map((place) => ({
            placeId: place.id,
            assignedBy: userId,
            assignedAt: new Date(),
          })),
        },
      },
      include: {
        places: {
          include: {
            place: true,
          },
        },
      },
    });

    return res.status(201).json({
      data: createPlan,
      message: "Successfully created a plan!",
    });
  } catch (error) {
    return res.status(500).json({
      message: "Error creating a plan",
      error: error.message,
    });
  }
});

router.put("/:id", accessValidation, async (req, res) => {
  const { id } = req.params;
  const { date, places, userId } = req.body;

  const formattedDate = date ? parseDate(date) : null;
  if (!date && (!places || places.length === 0)) {
    return res
      .status(400)
      .json({ message: "Either date or places must be provided for update" });
  }

  try {
    const updateData = {};

    // Update date if provided
    if (date) {
      updateData.date = formattedDate;
    }

    // Update places if provided
    if (places && places.length > 0) {
      updateData.places = {
        deleteMany: {}, // Remove all existing places
        create: places.map((place) => ({
          placeId: place.id,
          assignedBy: userId,
          assignedAt: new Date(),
        })),
      };
    }

    const updatedPlan = await prisma.plan.update({
      where: { id },
      data: updateData,
      include: {
        places: {
          include: {
            place: true,
          },
        },
      },
    });

    res.status(200).json({
      data: updatedPlan,
      message: "Plan updated successfully!",
    });
  } catch (error) {
    res.status(500).json({
      message: "Error updating plan",
      error: error.message,
    });
  }
});

router.delete("/:id", accessValidation, async (req, res) => {
  const { id } = req.params;
  const { userId } = req.body;

  try {
    // Check if the user exists
    const userExists = await prisma.user.findUnique({
      where: { id: userId },
    });

    if (!userExists) {
      return res.status(400).json({
        message: "User ID does not exist",
      });
    }

    // Check if the plan exists and belongs to the user
    const existingPlan = await prisma.plan.findUnique({
      where: { id },
      include: {
        user: true,
      },
    });

    if (!existingPlan) {
      return res.status(404).json({ message: "Plan not found" });
    }

    if (existingPlan.userId !== userId) {
      return res.status(403).json({
        message: "You are not authorized to delete this plan",
      });
    }

    // Delete related PlacesOnPlans records first
    await prisma.placesOnPlans.deleteMany({
      where: { planId: id },
    });

    // Delete the plan
    await prisma.plan.delete({
      where: { id },
    });

    res.status(200).json({
      message: "Plan and its related places deleted successfully",
    });
  } catch (error) {
    res.status(500).json({
      message: "Error deleting plan",
      error: error.message,
    });
  }
});

module.exports = router;
