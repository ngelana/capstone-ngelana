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
  getPlacesWithPrimaryType,
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
    // const getResponseSimilarPlacesId = await axios.get(
    //   `http://110.136.181.224:4000/similar-places/${id}`
    // );

    // const placeIds = getResponseSimilarPlacesId.data;

    // const similarPlaces = await prisma.place.findMany({
    //   where: {
    //     id: {
    //       in: placeIds,
    //     },
    //   },
    //   select: {
    //     id: true,
    //     name: true,
    //     rating: true,
    //   },
    // });

    const result = await getPlaceWithUrlPlaceholderbyId(id);
    // const resultWithSimilarPlaces = { ...result, similarPlaces };
    return res.status(200).json({
      // data: resultWithSimilarPlaces,
      data: result,
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

// Get places by filter primaryType
router.post("/primary-type/:type", accessValidation, async (req, res) => {
  const { type } = req.params;

  try {
    const result = await getPlacesWithPrimaryType(type);

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
