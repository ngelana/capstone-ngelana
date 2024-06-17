const express = require("express");
const prisma = require("../db");
const router = express.Router();
const { isValidUserId } = require("../services/UserServices");
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
    if (!isValidUserId(userId)) {
      return res.status(404).json({
        message: `User not found!`,
      });
    }

    const plans = await prisma.plan.findMany({
      where: { userId },
      select: {
        id: true,
        name: true,
        userId: true,
        date: true,
        places: {
          select: {
            place: true,
          },
        },
      },
    });

    if (!plans) {
      return res.status(400).json({
        message: `No plans made from user : ${userId}`,
      });
    }

    return res.status(200).json({
      data: plans,
      message: "Plans listed successfully!",
    });
  } catch (error) {
    console.log(error.message);
    return res.status(500).json({
      message: "Error retrieving plans",
    });
  }
});

// Get plan details from plan ID
router.get("/:id", accessValidation, async (req, res) => {
  const { userId } = req.body;

  try {
    if (!isValidUserId(userId)) {
      return res.status(404).json({
        message: `User not found!`,
      });
    }

    const plans = await prisma.plan.findMany({
      where: { userId },
      select: {
        id: true,
        name: true,
        userId: true,
        date: true,
        places: {
          select: {
            place: true,
          },
        },
      },
    });

    if (!plans) {
      return res.status(400).json({
        message: `No plans made from user : ${userId}`,
      });
    }

    return res.status(200).json({
      data: plans,
      message: "Plans listed successfully!",
    });
  } catch (error) {
    console.log(error.message);
    return res.status(500).json({
      message: "Error retrieving plans",
    });
  }
});

// Create a new plan
// router.post("/", accessValidation, async (req, res) => {
//   const { date, userId } = req.body; // Expecting date in the request body
//   const formattedDate = date ? parseDate(date) : null;
//   const placeIds = await generatePlaceIds();

//   console.log(placeIds);
//   console.log(placeIds[0]);
//   try {
//     if (!isValidUserId(userId)) {
//       return res.status(404).json({
//         message: `User not found!`,
//       });
//     }
//     if (!date) {
//       return res.status(400).json({
//         message: "Date is required",
//       });
//     }
//     const createPlan = await prisma.plan.create({
//       data: {
//         date: formattedDate,
//         userId,
//         places: {
//           create: placeIds.map((placeId) => ({
//             placeId,
//             assignedBy: userId,
//             assignedAt: new Date(),
//           })),
//         },
//       },
//       include: {
//         places: {
//           include: {
//             place: true,
//           },
//         },
//       },
//     });

//     return res.status(201).json({
//       data: createPlan,
//       message: "Success created a plan!",
//     });
//   } catch (error) {
//     console.log(error.message);
//     res.status(500).json({
//       message: "Error creating a plan",
//     });
//   }
// });

router.post("/recommend", accessValidation, async (req, res) => {
  const { date, userId } = req.body;

  try {
    if (!date) {
      return res.status(400).json({
        message: "Date is required",
      });
    }
    if (!isValidUserId(userId)) {
      return res.status(404).json({
        message: `User not found!`,
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
    console.log(error.message);
    return res.status(500).json({
      message: "Error generating recommendations",
    });
  }
});

router.post("/finalize", accessValidation, async (req, res) => {
  const { date, userId, places, planName } = req.body;
  const formattedDate = date ? parseDate(date) : null;

  try {
    if (!date || !places || !planName) {
      return res.status(400).json({
        message: "Date, places, and plan name are required",
      });
    }
    // Check if the userId exists
    if (!isValidUserId(userId)) {
      return res.status(404).json({
        message: `User not found!`,
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
      select: {
        id: true,
        name: true,
        userId: true,
        date: true,
        places: {
          select: {
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
    console.log(error.message);
    return res.status(500).json({
      message: "Error creating a plan",
    });
  }
});

router.put("/:id", accessValidation, async (req, res) => {
  const { id } = req.params;
  const { date, places, userId } = req.body;

  const formattedDate = date ? parseDate(date) : null;

  try {
    if (!date && (!places || places.length === 0)) {
      return res
        .status(400)
        .json({ message: "Either date or places must be provided for update" });
    }
    // Check if the userId exists
    if (!isValidUserId(userId)) {
      return res.status(404).json({
        message: `User not found!`,
      });
    }
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
      select: {
        id: true,
        name: true,
        userId: true,
        date: true,
        places: {
          select: {
            place: true,
          },
        },
      },
    });

    return res.status(200).json({
      data: updatedPlan,
      message: "Plan updated successfully!",
    });
  } catch (error) {
    console.log(error.message);
    return res.status(500).json({
      message: "Error updating plan",
    });
  }
});

router.delete("/:id", accessValidation, async (req, res) => {
  const { id } = req.params;
  const { userId } = req.body;

  try {
    // Check if the userId exists
    if (!isValidUserId(userId)) {
      return res.status(404).json({
        message: `User not found!`,
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

    return res.status(200).json({
      message: `Plan ${id} and its related places deleted successfully`,
    });
  } catch (error) {
    console.log(error.message);
    return res.status(500).json({
      message: "Error deleting plan",
    });
  }
});

module.exports = router;
