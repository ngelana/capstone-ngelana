const express = require("express");
const prisma = require("../db");
const router = express.Router();
const {
  isValidUserId,
  getPlacesWithUrlPlaceholdersByPlaceID,
} = require("../services/DbServices");
const { accessValidation } = require("../services/AuthServices");
const { parseDate } = require("../services/UtilServices");
const axios = require("axios");

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
    if (!(await isValidUserId(userId))) {
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

    if (!plans || plans.length === 0) {
      return res.status(400).json({
        message: `No plans made from user: ${userId}`,
      });
    }

    // Fetch all preferences
    const preferences = await prisma.preference.findMany();

    // Map over plans to include urlPlaceholder in places
    const plansWithUrlPlaceholders = plans.map((plan) => ({
      ...plan,
      places: plan.places.map((placeItem) => {
        const place = placeItem.place;
        const matchedPreference = preferences.find(
          (pref) => pref.name === place.primaryTypes
        );

        return {
          ...placeItem,
          place: {
            ...place,
            urlPlaceholder: matchedPreference
              ? matchedPreference.urlPlaceholder
              : null,
          },
        };
      }),
    }));

    return res.status(200).json({
      data: plansWithUrlPlaceholders,
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
    if (!(await isValidUserId(userId))) {
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

// router.post("/recommend", accessValidation, async (req, res) => {
//   const { date, userId, inputPreferences } = req.body;

//   try {
//     // console.log(inputPreferences);

//     if (!(await isValidUserId(userId))) {
//       return res.status(404).json({
//         message: `User not found!`,
//       });
//     }
//     if (!inputPreferences || inputPreferences.length == 0) {
//       return res
//         .status(404)
//         .json({ message: "Input Preferences must not be empty!" });
//     }
//     if (!date) {
//       return res.status(400).json({
//         message: "Date is required",
//       });
//     }

//     // get Preference name from input
//     const getInputPreferencesName = await prisma.preference.findMany({
//       where: { id: { in: inputPreferences } },
//       select: {
//         name: true,
//       },
//     });
//     // Store preference names in array
//     const inputPreferencesNames = getInputPreferencesName.map(
//       (pref) => pref.name
//     );
//     // get userPreferences
//     const userPreferences = await prisma.userPreferences.findMany({
//       where: { userId },
//       include: {
//         preference: true,
//       },
//     });

//     // Extract preference names
//     const preferenceNames = userPreferences.map((up) => up.preference.name);
//     // console.log(preferenceNames);

//     // Convert preferences array to a comma-separated string
//     // const userPreferencesString = JSON.stringify(preferenceNames);
//     // const inputPreferencesString = JSON.stringify(inputPreferencesNames);
//     // console.log(typeof userPreferencesString);
//     // console.log(typeof inputPreferencesString);
//     // return res.json({
//     //   userPreferences: userPreferencesString,
//     //   inputPreferences: inputPreferencesString,
//     // });

//     // json object to send
//     // console.log(preferenceNames);
//     // console.log(inputPreferencesNames);
//     // return res.json({
//     //   userPreferences: preferenceNames,
//     //   inputPreferences: inputPreferencesNames,
//     // });

//     // Send request to the FastAPI app with the formatted body
//     // const response = await axios.post(
//     //   "http://110.136.181.224:4000/recommend-places/",
//     //   {
//     //     userPreferences: preferenceNames,
//     //     inputPreferences: inputPreferencesNames,
//     //   }
//     // );
//     // console.log(response);
//     // return res.status(200).json(response.data);

//     // const idsArray = response.data.ids;

//     // const placeIds = await generatePlaceIds();
//     // console.log(placeIds);
//     // const places = await getPlacesWithUrlPlaceholdersByPlaceID(placeIds);
//     // return res.status(200).json({
//     //   places,
//     //   message: "Successfully generated recommendations",
//     // });
//   } catch (error) {
//     console.log(error.message);
//     return res.status(500).json({
//       message: "Error generating recommendations",
//     });
//   }
// });

// // router.post("/finalize", accessValidation, async (req, res) => {
// //   const { date, userId, places, name } = req.body;
// //   const formattedDate = date ? parseDate(date) : null;

// //   try {
// //     if (!date || !places || !name) {
// //       return res.status(400).json({
// //         message: "Date, places, and plan name are required",
// //       });
// //     }
// //     // Check if the userId exists
// //     if (!(await isValidUserId(userId))) {
// //       return res.status(404).json({
// //         message: `User not found!`,
// //       });
// //     }

// //     const createPlan = await prisma.plan.create({
// //       data: {
// //         date: formattedDate,
// //         userId,
// //         name,
// //         places: {
// //           create: places.map((place) => ({
// //             placeId: place.id,
// //             assignedBy: userId,
// //             assignedAt: new Date(),
// //           })),
// //         },
// //       },
// //       select: {
// //         id: true,
// //         name: true,
// //         userId: true,
// //         date: true,
// //         places: {
// //           select: {
// //             place: true,
// //           },
// //         },
// //       },
// //     });

// //     return res.status(201).json({
// //       data: createPlan,
// //       message: "Successfully created a plan!",
// //     });
// //   } catch (error) {
// //     console.log(error.message);
// //     return res.status(500).json({
// //       message: "Error creating a plan",
// //     });
// //   }
// // });

// router.post("/finalize", accessValidation, async (req, res) => {
//   const { date, userId, places, name } = req.body;
//   const formattedDate = date ? parseDate(date) : null;

//   try {
//     if (!date || !places || !name) {
//       return res.status(400).json({
//         message: "Date, places, and plan name are required",
//       });
//     }

//     // Check if the userId exists
//     if (!(await isValidUserId(userId))) {
//       return res.status(404).json({
//         message: `User not found!`,
//       });
//     }

//     // Fetch places with URL placeholders
//     const placeIds = places.map((place) => place.id);
//     const placesWithUrlPlaceholders =
//       await getPlacesWithUrlPlaceholdersByPlaceID(placeIds);

//     // Map the URL placeholders to the places being added
//     const placesData = places.map((place) => {
//       const matchedPlace = placesWithUrlPlaceholders.find(
//         (p) => p.id === place.id
//       );
//       return {
//         placeId: place.id,
//         assignedBy: userId,
//         assignedAt: new Date(),
//         urlPlaceholder: matchedPlace ? matchedPlace.urlPlaceholder : null,
//       };
//     });

//     // Create the plan
//     const createPlan = await prisma.plan.create({
//       data: {
//         date: formattedDate,
//         userId,
//         name,
//         places: {
//           create: placesData.map((place) => ({
//             placeId: place.placeId,
//             assignedBy: place.assignedBy,
//             assignedAt: place.assignedAt,
//           })),
//         },
//       },
//       select: {
//         id: true,
//         name: true,
//         userId: true,
//         date: true,
//         places: {
//           select: {
//             place: true,
//           },
//         },
//       },
//     });

//     // Include the URL placeholders in the response
//     const responseData = {
//       ...createPlan,
//       places: createPlan.places.map((planPlace) => {
//         const matchedPlace = placesWithUrlPlaceholders.find(
//           (p) => p.id === planPlace.place.id
//         );
//         return {
//           ...planPlace,
//           urlPlaceholder: matchedPlace ? matchedPlace.urlPlaceholder : null,
//         };
//       }),
//     };

//     return res.status(201).json({
//       data: responseData,
//       message: "Successfully created a plan!",
//     });
//   } catch (error) {
//     console.log(error.message);
//     return res.status(500).json({
//       message: "Error creating a plan",
//     });
//   }
// });

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
    if (!(await isValidUserId(userId))) {
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
    if (!(await isValidUserId(userId))) {
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
