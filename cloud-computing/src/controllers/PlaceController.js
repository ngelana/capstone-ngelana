const express = require("express");
const prisma = require("../db");
const router = express.Router();
const { accessValidation } = require("../services/AuthServices");

// Get places list
router.get("/", accessValidation, async (req, res) => {
  const result = await prisma.place.findMany();
  return res.status(200).json({
    data: result,
    message: "Place Listed!",
  });
});

// Read place by id
router.get("/:id", accessValidation, async (req, res) => {
  const { id } = req.params;
  const result = await prisma.place.findUnique({
    where: {
      id: parseInt(id),
    },
  });
  return res.status(200).json({
    data: result,
    message: `Place with id ${id} Listed!`,
  });
});

module.exports = router;
