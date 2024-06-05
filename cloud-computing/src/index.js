const express = require("express");
const dotenv = require("dotenv");
const { PrismaClient } = require("@prisma/client");

const prisma = new PrismaClient();
const app = express();

app.use(express.json());

dotenv.config();

const PORT = process.env.PORT;

app.listen(PORT, () => {
  console.log("express api running in port : " + PORT);
});
