const express = require("express");
const dotenv = require("dotenv");
const app = express();
const cors = require("cors");
dotenv.config();
const PORT = process.env.PORT || 8080;
const { limiter } = require("./services/AuthServices");
const userController = require("./controllers/UserController");
const planController = require("./controllers/PlanController");
const placeController = require("./controllers/PlaceController");
const reviewController = require("./controllers/ReviewController");
const preferenceController = require("./controllers/PreferenceController");

app.use(express.json());
app.use(cors());

app.get("/", (req, res) => {
  return res.status(200).json({
    message: "Hi! Welcome to Ngelana API.",
  });
});

app.use("/user", userController, limiter);
app.use("/plan", planController, limiter);
app.use("/place", placeController, limiter);
app.use("/review", reviewController, limiter);
app.use("/preference", preferenceController, limiter);

app.listen(PORT, () => {
  console.log("Express API running in port: " + PORT);
});
