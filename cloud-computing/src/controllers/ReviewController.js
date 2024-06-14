const express = require("express");
const { user } = require("../db");
const prisma = require("../db");
const router = express.Router();
const { accessValidation } = require("../services/AuthServices");
const parseDate = require("../services/UtilServices");

// get all reviews from one user
router.get("/", accessValidation, async (req, res) => {
  const { userId } = req.body;

  try {
    const user = await prisma.userReview.findMany({
      where: { userId: userId },
    });

    if (!user) {
      return res.status(404).json({
        message: "Invalid User ID!",
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
      message: `Reviews from userId:${userId} listed!`,
    });
  } catch (error) {
    return res.status(400).json({
      message: `Failed listing reviews from user : ${userId}, error:${error.message}`,
    });
  }
});

// create user review
router.post("/", accessValidation, async (req, res) => {
  const { userId, review, star, date, placeId } = req.body;
  const formattedDate = parseDate(date);
  try {
    const user = await prisma.user.findUnique({
      where: {
        id: userId,
      },
    });

    if (!user) {
      return res.status(404).json({
        message: "Invalid User ID!",
      });
    }

    const place = await prisma.place.findUnique({
      where: {
        id: placeId,
      },
    });

    if (!place) {
      return res.status(404).json({
        message: "Place ID not found",
      });
    }

    const reviews = await prisma.userReview.create({
      data: {
        review,
        star,
        date: formattedDate,
        placeId,
        userId,
      },
    });
    return res.status(200).json({
      data: reviews,
      message: `Review ID : ${userId} created!`,
    });
  } catch (error) {
    return res.status(500).json({
      message: `Failed creating review from user : ${userId}, error:${error.message}`,
    });
  }
});

// read review by review id
router.get("/:id", accessValidation, async (req, res) => {
  const { id } = req.params;
  const { userId } = req.body;

  try {
    const reviews = await prisma.userReview.findUnique({
      where: { userId, id },
    });
    if (!reviews.userId) {
      return res.status(404).json({
        message: "User not found",
      });
    }
    if (!reviews) {
      return res.status(404).json({
        message: "Review not found or empty",
      });
    }
    return res.status(200).json({
      data: reviews,
      message: `Review id :${id} listed!`,
    });
  } catch (error) {
    return res.status(400).json({
      message: `Failed listing reviews from user : ${id}, error:${error.message}`,
    });
  }
});

// Update review from ReviewId
router.patch("/:id", accessValidation, async (req, res) => {
  const { id } = req.params;
  const { userId, review, star, date, placeId } = req.body;
  const formattedDate = parseDate(date);
  try {
    const result = await prisma.userReview.update({
      data: {
        review,
        star,
        date: formattedDate,
      },
      where: {
        id,
        placeId,
        userId,
      },
    });
    if (!result) {
      return res.status(404).json({
        message: "Failed updating review. Review not found!",
      });
    }

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
        id,
      },
    });
    if (!review) {
      return res.status(404).json({
        message: "Review not found",
      });
    }
    const result = await prisma.userReview.delete({
      where: {
        id,
      },
    });
    if (!result) {
      return res.status(404).json({
        message: "Delete review failed!",
      });
    }
    return res
      .status(200)
      .json({ message: `Success. Deleted Review ID : ${id}` });
  } catch (error) {
    return res.status(500).json({
      message: `Error deleting review! Error : ${error.message}`,
    });
  }
});

module.exports = router;
