const bcrypt = require("bcrypt");
const jwt = require("jsonwebtoken");
const rateLimit = require("express-rate-limit");

const secret = process.env.JWT_SECRET;

//Rate Limiter
function limiter() {
  return rateLimit({
    windowMs: 15 * 60 * 1000, // 15 minutes
    max: 100, // limit each IP to 100 requests per windowMs
    message: {
      message: "Too many requests, please try again later.",
    },
  });
}

// Middleware auth

const accessValidation = (req, res, next) => {
  const { authorization } = req.headers;

  if (!authorization) {
    return res.status(400).json({
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
  return bcrypt.hashSync(password, 10);
}

function compareHashedPass(password, encrypted) {
  return bcrypt.compareSync(password, encrypted);
}

module.exports = {
  accessValidation,
  createHashedPass,
  createToken,
  compareHashedPass,
  limiter,
};
