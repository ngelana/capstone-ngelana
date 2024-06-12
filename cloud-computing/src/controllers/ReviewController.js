const express = require("express");
const prisma = require("../db");
const router = express.Router();
const { accessValidation } = require("../services/AuthServices");

// get all reviews from one user
router.get("/", accessValidation, async (req, res) => {
  const { userId } = req.body;

  try {
    const user = await prisma.userReview.findUnique({
      where: {
        userId,
      },
    });

    if (!user) {
      return res.status(404).json({
        message: "User not found",
      });
    }
    const reviews = await prisma.userReview.findMany({
      where: { userId },
    });
    if (!reviews) {
      return res.status(404).json({
        message: "Reviews not found or empty",
      });
    }
    return res.status(200).json({
      data: reviews,
      message: `Reviews from userId:${id} listed!`,
    });
  } catch (error) {
    return res.status(400).json({
      message: `Failed listing reviews from user : ${id}, error:${err.message}`,
    });
  }
});

// create user review
router.post("/", accessValidation, async (req, res) => {
  const { userId, review, star, date, placeId } = req.body;

  try {
    const user = await prisma.userReview.findUnique({
      where: {
        userId,
      },
    });

    if (!user) {
      return res.status(404).json({
        message: "User not found",
      });
    }
    const reviews = await prisma.userReview.create({
      data: {
        id,
        review,
        star,
        date,
        placeId,
        userId,
      },
    });
    return res.status(200).json({
      data: reviews,
      message: `Review ID : ${id} created!`,
    });
  } catch (error) {
    return res.status(500).json({
      message: `Failed creating review from user : ${userId}, error:${err.message}`,
    });
  }
});

// Update review from ReviewId
router.patch("/:id", accessValidation, async (req, res) => {
  const { id } = req.params;
  const { userId, review, star, date } = req.body;
  try {
    const reviewid = await prisma.userReview.findUnique({
      where: {
        id: id,
      },
    });

    if (!reviewid) {
      return res.status(404).json({
        message: "Review not found",
      });
    }
    const result = await prisma.userReview.update({
      data: {
        review,
        star,
        date,
      },
      where: {
        id: id,
        userId,
      },
    });

    res.status(200).json({
      data: result,
      message: `Review id:${id} from User ${userId} Updated!`,
    });
  } catch (error) {
    return res.status(500).json({
      message: `Error updating review! Error : ${error.message}`,
    });
  }
});

// Delete review from ReviewId
router.delete("/:id", accessValidation, async (req, res) => {
  const { id } = req.params;

  try {
    const review = await prisma.userReview.findUnique({
      where: {
        id: id,
      },
    });

    if (!review) {
      return res.status(404).json({
        message: "Review not found",
      });
    }
    const result = await prisma.userReview.delete({
      where: {
        id: id,
      },
    });
    return res.status(200).json({ message: `Review ID : ${id} Deleted!` });
  } catch (error) {
    return res.status(500).json({
      message: `Error deleting review! Error : ${error.message}`,
    });
  }
});

module.exports = router;
