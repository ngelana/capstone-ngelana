const prisma = require("../db");

async function isValidUserId(id) {
  try {
    const isValid = await prisma.user.findUnique({
      where: {
        id,
      },
    });

    if (isValid) {
      return true;
    } else {
      return false;
    }
  } catch (error) {
    console.error(error);
    return error;
  }
}

module.exports = { isValidUserId };
