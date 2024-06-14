const express = require("express");
const prisma = require("../db");
const router = express.Router();
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
    return res.status(500).json({
      message: `Error retrieving place! Error:${error.message}`,
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
    return res.status(500).json({
      message: `Error retrieving place! Error:${error.message}`,
    });
  }
});

module.exports = router;
