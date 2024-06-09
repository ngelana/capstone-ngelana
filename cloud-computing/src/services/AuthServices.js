const bcrypt = require("bcrypt");
const jwt = require("jsonwebtoken");

const secret = process.env.JWT_SECRET;

// Middleware auth

const accessValidation = (req, res, next) => {
  const { authorization } = req.headers;

  if (!authorization) {
    return res.status(401).json({
      message: "Token needed !!!",
    });
  }

  const token = authorization.split(" ")[1];
  const secret = process.env.JWT_SECRET;
  try {
    const jwtDecode = jwt.verify(token, secret);
    req.userData = jwtDecode;
    next();
  } catch (error) {
    return res.status(401).json({
      message: "Unauthorized!",
    });
  }
};

function createToken(payload) {
  return jwt.sign(payload, secret);
}

function createHashedPass(password) {
  return bcrypt.hash(password, 10);
}

function compareHashedPass(password, encrypted) {
  return bcrypt.compare(password, encrypted);
}

module.exports = {
  accessValidation,
  createHashedPass,
  createToken,
  compareHashedPass,
};
