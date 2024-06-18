const express = require("express");
const prisma = require("../db");
const router = express.Router();
const { isValidUserId } = require("../services/UserServices");
const { accessValidation } = require("../services/AuthServices");

// Get places list
router.get("/", accessValidation, async (req, res) => {
  try {
    const result = await prisma.place.findMany();
    return res.status(200).json({
      data: result,
      message: "Place Listed!",
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
    const result = await prisma.place.findUnique({
      where: {
        id,
      },
    });
    return res.status(200).json({
      data: result,
      message: `Place with id ${id} Listed!`,
    });
  } catch (error) {
    console.log(error.message);
    return res.status(500).json({
      message: `Error retrieving place!`,
    });
  }
});

// get places by QUERY params | request example : /search-place?query=park
router.get("/search-place", accessValidation, async (req, res) => {
  const { query } = req.query;

  try {
    const result = await prisma.place.findMany({
      where: {
        name: {
          contains: query,
        },
      },
    });

    if (!result) {
      return res.status(404).json({
        message: "Invalid query input!",
      });
    }
    return res.status(200).json({
      data: result,
      message: `Places with query name ${query} Listed!`,
    });
  } catch (error) {
    console.log(error.message);
    return res.status(500).json({
      message: `Error retrieving places!`,
    });
  }
});

// get places by QUERY params | request example : /search-place?query=park
router.post("/search-place/", accessValidation, async (req, res) => {
  const { query } = req.query;

  try {
    const result = await prisma.place.findMany({
      where: {
        name: {
          contains: query,
        },
      },
    });

    if (!result) {
      return res.status(404).json({
        message: "Invalid query input!",
      });
    }
    return res.status(200).json({
      data: result,
      message: `Places with query name ${query} Listed!`,
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
    const result = await prisma.place.findMany({
      where: {
        primaryTypes: type,
      },
    });

    if (!result) {
      return res.status(404).json({
        message: "Invalid query input!",
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
