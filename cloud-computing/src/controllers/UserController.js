const express = require("express");
const prisma = require("../db");
const router = express.Router();
const {
  accessValidation,
  createHashedPass,
  createToken,
  compareHashedPass,
} = require("../services/AuthServices");
const parseDate = require("../services/UtilServices");
const { isValidUserId } = require("../services/UserServices");

// Middleware auth

// Register user
router.post("/register", async (req, res) => {
  const { name, email, password } = req.body;
  const hashedPassword = createHashedPass(password);
  try {
    const result = await prisma.user.create({
      data: {
        name,
        email,
        password: hashedPassword,
      },
    });
    return res.status(201).json({ message: "User registered successfully!" });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      message: `Error registering user`,
    });
  }
});

// Login user
router.post("/login", async (req, res) => {
  const { email, password } = req.body;
  try {
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
        message: "Logged in",
      });
    } else {
      return res.status(403).json({
        message: "Wrong password!",
      });
    }
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      message: `Error! Cannot login to app`,
    });
  }
});

// Create user [Shouldn't be accessed by user]
router.post("/", accessValidation, async (req, res) => {
  const { name, email, password } = req.body;

  try {
    const result = await prisma.user.create({
      data: {
        name: name,
        email: email,
        password: password,
      },
      select: {
        id: true,
        name: true,
        email: true,
        phone: true,
        birthdate: true,
        gender: true,
      },
    });
    res.status(201).json({
      data: result,
      message: "User Created!",
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      message: `Error creating user!`,
    });
  }
});

// Read all users
router.get("/", accessValidation, async (req, res) => {
  try {
    const result = await prisma.user.findMany();
    return res.status(200).json({
      data: result,
      message: "Users Listed!",
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      message: `Error listing users! `,
    });
  }
});

// Read user by ID
router.get("/:id", accessValidation, async (req, res) => {
  const { id } = req.params;
  try {
    const result = await prisma.user.findUnique({
      where: { id },
      select: {
        id: true,
        name: true,
        email: true,
        phone: true,
        birthdate: true,
        gender: true,
      },
    });

    if (!isValidUserId(id)) {
      return res.status(404).json({
        message: `User not found!`,
      });
    }
    return res.status(200).json({
      data: result,
      message: `Details of UserID: ${id} Listed!`,
    });
  } catch (error) {
    console.log(error);
    return res.status(500).json({
      message: `Error reading user!`,
    });
  }
});

// Update user from id
router.patch("/:id", accessValidation, async (req, res) => {
  const { id } = req.params;
  const { name, email, password, phone, birthdate, gender } = req.body;
  const hashedPassword = createHashedPass(password);
  try {
    if (!isValidUserId(id)) {
      return res.status(404).json({
        message: `User not found!`,
      });
    }
    // Parse the birthdate
    const parsedBirthdate = birthdate ? parseDate(birthdate) : null;

    const result = await prisma.user.update({
      data: {
        name: name,
        email: email,
        password: hashedPassword,
        phone: phone,
        birthdate: parsedBirthdate,
        gender: gender,
      },
      where: {
        id: id,
      },
      select: {
        id: true,
        name: true,
        email: true,
        phone: true,
        birthdate: true,
        gender: true,
        updatedAt: true,
      },
    });

    return res
      .status(201)
      .json({ data: result, message: `User ${id} Updated!` });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      message: `Error updating user! `,
    });
  }
});

// Delete user from id
router.delete("/:id", accessValidation, async (req, res) => {
  const { id } = req.params;

  try {
    const user = await prisma.user.findUnique({
      where: {
        id: id,
      },
    });

    if (!user) {
      return res.status(404).json({
        message: "User not found",
      });
    }
    const result = await prisma.user.delete({
      where: {
        id: id,
      },
    });
    return res.status(200).json({ message: `User ${id} Deleted!` });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      message: `Error deleting user!`,
    });
  }
});

// Logout user
router.post("/logout", accessValidation, (req, res) => {
  try {
    const payload = {
      id: req.userData.id,
      name: req.userData.name,
      email: req.userData.email,
    };
    const token = createToken(payload, "1ms");
    return res.status(200).json({
      message: "Logged out successfully",
      token: token,
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({
      message: `Error logging out user from server.`,
    });
  }
});

module.exports = router;
