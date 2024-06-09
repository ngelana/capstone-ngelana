const express = require("express");
const prisma = require("../db");
const router = express.Router();
const { accessValidation } = require("../services/AuthServices");

// Placeholder function to simulate ML model for generating placeIds
const generatePlaceIds = async () => {
  // Simulate fetching or generating place IDs
  // Replace this with your ML model logic later
  const places = await prisma.places.findMany({
    select: { id: true },
    take: 3, // Simulate getting 3 place IDs, adjust as needed
  });
  return places.map((place) => place.id);
};

// Get list of plans for a user
router.get("/", accessValidation, async (req, res) => {
  const userId = req.user.id; // Assuming user ID is available in the request object

  try {
    const plans = await prisma.plan.findMany({
      where: { userId: userId },
      include: {
        PlanPlaces: {
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
  const userId = req.user.id; // Assuming user ID is available in the request object
  const { date } = req.body; // Expecting date in the request body

  if (!date) {
    return res.status(400).json({
      message: "Date is required",
    });
  }

  try {
    // Generate place IDs using the placeholder function
    const placeIds = await generatePlaceIds();

    // Create the plan with the generated place IDs
    const plan = await prisma.plan.create({
      data: {
        userId: userId,
        date: new Date(date),
        PlanPlaces: {
          create: placeIds.map((placeId) => ({
            place: {
              connect: { id: placeId },
            },
          })),
        },
      },
      include: {
        PlanPlaces: {
          include: {
            place: true, // Include place data
          },
        },
      },
    });

    res.status(201).json({
      data: plan,
      message: "Plan created successfully!",
    });
  } catch (error) {
    res.status(500).json({
      message: "Error creating plan",
      error: error.message,
    });
  }
});

module.exports = router;
