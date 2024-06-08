const express = require("express");
const dotenv = require("dotenv");

const app = express();

dotenv.config();

const PORT = process.env.PORT;

app.use(express.json());

app.get("/api", (req, res) => {
  res.send("Selamat datang di API akuh");
});

const userController = require("./controllers/UserController");

app.use("/user", userController);

app.listen(PORT, () => {
  console.log("Express API running in port: " + PORT);
});
