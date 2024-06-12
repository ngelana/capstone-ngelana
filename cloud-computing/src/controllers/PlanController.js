const express = require("express");
const prisma = require("../db");
const router = express.Router();
const { accessValidation } = require("../services/AuthServices");
const { parseISO, format, isValid } = require("date-fns");

// Helper function to format the date
const formatDate = (date) => {
  const parsedDate = parseISO(date);
  if (!isValid(parsedDate)) {
    throw new Error("Invalid date format");
  }
  return format(parsedDate, "yyyy-MM-dd");
};

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
  const { id } = req.body;

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
  const { date, userId } = req.body; // Expecting date in the request body
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
        date: new Date(formatDate(date)),
        userId,
        places: {
          create: placeIds.map((placeId) => ({
            placeId: placeId,
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
  if (!date) {
    return res.status(400).json({
      message: "Date is required",
    });
  }

  try {
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
        date: new Date(formatDate(date)),
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

module.exports = router;
