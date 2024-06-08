const express = require("express");
const prisma = require("../db");
const bcrypt = require("bcrypt");
const e = require("express");
const router = express.Router();

// Register user
router.use("/register", async (req, res) => {
  const { name, email, password } = req.body;
  const hashedPassword = await bcrypt.hash(password, 10);
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
router.use("/login", async (req, res) => {
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

  const isPasswordValid = await bcrypt.compare(password, user?.password);

  if (isPasswordValid) {
    return res.status(200).json({
      data: {
        id: user.id,
        name: user.name,
        email: user.email,
      },
    });
  } else {
    return res.status(403).json({
      message: "Wrong password!",
    });
  }
});

// Create user
router.post("/", async (req, res) => {
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

// Read user
router.get("/", async (req, res) => {
  const result = await prisma.user.findMany();
  res.status(200).json({
    data: result,
    message: "Users Listed!",
  });
});

// Update user from id
router.patch("/:id", async (req, res) => {
  const { id } = req.params;
  const { name, email, password } = req.body;

  const result = await prisma.user.update({
    data: {
      name: name,
      email: email,
      password: password,
    },
    where: {
      id: id,
    },
  });

  res.status(200).json({ data: result, message: `User ${id} Updated!` });
});

// Delete user from id
router.delete("/:id", async (req, res) => {
  const { id } = req.params;

  const result = await prisma.user.delete({
    where: {
      id: id,
    },
  });
  res.status(200).json({ message: `User ${id} Deleted!` });
});

module.exports = router;
