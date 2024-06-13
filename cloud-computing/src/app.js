const express = require("express");
const dotenv = require("dotenv");
const app = express();
dotenv.config();
const PORT = process.env.PORT;
const userController = require("./controllers/UserController");
const planController = require("./controllers/PlanController");
const placeController = require("./controllers/PlaceController");
const reviewController = require("./controllers/PlaceController");
const preferenceController = require("./controllers/PreferenceController");

app.use(express.json());

app.use("/user", userController);
app.use("/plan", planController);
app.use("/place", placeController);
app.use("/review", reviewController);
app.use("/preference", preferenceController);

app.listen(PORT, () => {
  console.log("Express API running in port: " + PORT);
});
