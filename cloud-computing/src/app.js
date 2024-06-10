const express = require("express");
const dotenv = require("dotenv");
const app = express();
dotenv.config();
const PORT = process.env.PORT;
const userController = require("./controllers/UserController");
const planController = require("./controllers/PlanController");
const placeController = require("./controllers/PlaceController");

app.use(express.json());

app.use("/user", userController);
app.use("/plan", planController);
app.use("/place", placeController);

app.listen(PORT, () => {
  console.log("Express API running in port: " + PORT);
});
