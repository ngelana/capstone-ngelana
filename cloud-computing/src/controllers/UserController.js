const express = require("express");
const prisma = require("../db");
const router = express.Router();
const {
  accessValidation,
  createHashedPass,
  createToken,
  compareHashedPass,
} = require("../services/AuthServices");
// Middleware auth

// Register user
router.post("/register", async (req, res) => {
  const { name, email, password } = req.body;
  const hashedPassword = createHashedPass(password);
  const result = await prisma.user.create({
    data: {
      name,
      email,
      password: hashedPassword,
    },
  });
  res.status(201).json({ message: "User registered successfully!" });
});

// Login user
router.post("/login", async (req, res) => {
  const { email, password } = req.body;

  const user = await prisma.user.findUnique({
    where: {
      email: email,
    },
  });

  if (!user) {
    return res.status(404).json({
      message: "User not found",
    });
  }

  if (!user?.password) {
    return res.status(404).json({
      message: "Password not set",
    });
  }

  const isPasswordValid = await compareHashedPass(password, user?.password);

  if (isPasswordValid) {
    const payload = {
      id: user.id,
      name: user.name,
      email: user.email,
    };
    const token = createToken(payload);
    return res.status(200).json({
      data: {
        id: user.id,
        name: user.name,
        email: user.email,
      },
      token: token,
    });
  } else {
    return res.status(403).json({
      message: "Wrong password!",
    });
  }
});

// Create user
router.post("/", accessValidation, async (req, res) => {
  const { name, email, password } = req.body;
  const result = await prisma.user.create({
    data: {
      name: name,
      email: email,
      password: password,
    },
  });
  res.status(201).json({
    data: result,
    message: "User Created!",
  });
});

// Get preferences list
router.get("/preferences", accessValidation, async (req, res) => {
  const result = await prisma.preference.findMany();
  return res.status(200).json({
    data: result,
    message: "Preference Listed!",
  });
});

// Create user preferences
router.post("/:id/preferences", accessValidation, async (req, res) => {
  const { id } = req.params;
  const { preferenceIds } = req.body;

  if (!Array.isArray(preferenceIds) || preferenceIds.length === 0) {
    return res.status(400).json({
      message: "preferenceIds must be a non-empty array",
    });
  }

  try {
    // Create an array of objects to be inserted
    const data = preferenceIds.map((preferenceId) => ({
      userId: id,
      preferenceId: preferenceId,
    }));

    // Use Prisma's createMany method to create multiple records at once
    const result = await prisma.userPreferences.createMany({
      data: data,
      skipDuplicates: true, // Skip creating records that already exist
    });

    res.status(201).json({
      data: result,
      message: "User Preferences Created!",
    });
  } catch (error) {
    res.status(500).json({
      message: "Error creating user preferences",
      error: error.message,
    });
  }
});

// Read user
router.get("/", accessValidation, async (req, res) => {
  const result = await prisma.user.findMany();
  return res.status(200).json({
    data: result,
    message: "Users Listed!",
  });
});

// Update user from id
router.patch("/:id", accessValidation, async (req, res) => {
  const { id } = req.params;
  const { name, email, password, phone, birthdate, gender } = req.body;

  const result = await prisma.user.update({
    data: {
      name: name,
      email: email,
      password: password,
      phone: phone,
      birthdate: birthdate,
      gender: gender,
    },
    where: {
      id: id,
    },
  });

  res.status(200).json({ data: result, message: `User ${id} Updated!` });
});

// Delete user from id
router.delete("/:id", accessValidation, async (req, res) => {
  const { id } = req.params;

  const result = await prisma.user.delete({
    where: {
      id: id,
    },
  });
  res.status(200).json({ message: `User ${id} Deleted!` });
});

module.exports = router;
