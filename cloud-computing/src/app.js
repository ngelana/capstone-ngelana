const express = require("express");
const dotenv = require("dotenv");
const app = express();
dotenv.config();
const PORT = process.env.PORT;
const { limiter } = require("./services/AuthServices");
const userController = require("./controllers/UserController");
const planController = require("./controllers/PlanController");
const placeController = require("./controllers/PlaceController");
const reviewController = require("./controllers/ReviewController");
const preferenceController = require("./controllers/PreferenceController");

app.use(express.json());

app.use("/user", userController, limiter);
app.use("/plan", planController, limiter);
app.use("/place", placeController, limiter);
app.use("/review", reviewController, limiter);
app.use("/preference", preferenceController, limiter);

app.listen(PORT, () => {
  console.log("Express API running in port: " + PORT);
});
