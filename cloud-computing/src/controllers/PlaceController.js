const express = require("express");
const prisma = require("../db");
const router = express.Router();
const axios = require("axios");
const { accessValidation } = require("../services/AuthServices");
const {
  getPlacesWithUrlPlaceholder,
  getPlaceWithUrlPlaceholderbyId,
  getPlacesWithUrlPlaceholderByQuery,
  getPlacesWithUrlPlaceholderByType,
} = require("../services/DbServices");

// Get places list
router.get("/", accessValidation, async (req, res) => {
  try {
    const result = await getPlacesWithUrlPlaceholder();
    return res.status(200).json({
      data: result,
      message: "Places Listed!",
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      message: `Error retrieving place!`,
    });
  }
});

// Read place by id
router.get("/:id", accessValidation, async (req, res) => {
  const { id } = req.params;

  try {
    // const getResponseSimilarPlacesId = await axios.post(
    //   `https://YOUR_FASTAPI_APP_URL/${id}`
    // );

    // const placeIds = getResponseSimilarPlacesId.data.ids;
    // const placeIdsArray = placeIds.split(",");

    // const similarPlaces = await prisma.place.findMany({
    //   where: {
    //     id: {
    //       in: placeIdsArray,
    //     },
    //   },
    //   select: {
    //     id: true,
    //     name: true,
    //     rating: true,
    //   },
    // });

    const result = await getPlaceWithUrlPlaceholderbyId(id);
    return res.status(200).json({
      data: result,
      // similarPlaces,
      message: `Place with id ${id} and similar places listed!`,
    });
  } catch (error) {
    console.log(error.message);
    return res.status(500).json({
      message: `Error retrieving place!`,
    });
  }
});

// get places by QUERY params | request example : /search-place?query=park
router.post("/search-place/", accessValidation, async (req, res) => {
  const { query } = req.query;

  try {
    const result = await getPlacesWithUrlPlaceholderByQuery(query);

    if (!result || result.length === 0) {
      return res.status(404).json({
        message: "Invalid query input or no places found!",
      });
    }
    return res.status(200).json({
      data: result,
      message: `Places with query name ${query} listed!`,
    });
  } catch (error) {
    console.log(error.message);
    return res.status(500).json({
      message: `Error retrieving places!`,
    });
  }
});

// Get places by filter type
router.post("/:type", accessValidation, async (req, res) => {
  const { type } = req.params;

  try {
    const result = await getPlacesWithUrlPlaceholderByType(type);

    if (!result || result.length === 0) {
      return res.status(404).json({
        message: "Invalid query input or no places found!",
      });
    }
    return res.status(200).json({
      data: result,
      message: `Places with type name ${type} Listed!`,
    });
  } catch (error) {
    console.log(error.message);
    return res.status(500).json({
      message: `Error retrieving places!`,
    });
  }
});

module.exports = router;
