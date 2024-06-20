const { PrismaClient } = require("@prisma/client");
const prisma = new PrismaClient();
const fs = require("fs");
const csv = require("csv-parser");

const seedPlaces = async () => {
  const places = [];
  return new Promise((resolve, reject) => {
    fs.createReadStream("places.csv")
      .pipe(csv())
      .on("data", (row) => {
        places.push({
          id: row.id,
          name: row.name,
          latitude: parseFloat(row.latitude),
          longitude: parseFloat(row.longitude),
          address: row.address,
          url: row.url || null,
          status: row.status || null,
          phone: row.phone || null,
          primaryTypes: row["primary-type"] || null,
          types: row.types || null,
          rating: row.rating ? parseFloat(row.rating) : null,
          ratingCount: row["rating-count"]
            ? parseInt(row["rating-count"])
            : null,
          priceLevel: row["price-level"].toString() || null,
        });
      })
      .on("end", async () => {
        for (const place of places) {
          try {
            await prisma.place.create({ data: place });
            console.log(`Inserted ${place.name}`);
          } catch (error) {
            console.error(`Error inserting ${place.name}:`, error);
          }
        }
        resolve();
      })
      .on("error", (error) => {
        reject(error);
      });
  });
};

const seedPreferences = async () => {
  //json url placeholder test (Success!)
  const preferences = [];
  return new Promise((resolve, reject) => {
    fs.createReadStream("preferences.csv")
      .pipe(csv())
      .on("data", (row) => {
        const urlPlaceholder = JSON.parse(row.urlPlaceholder);
        preferences.push({
          id: row.id,
          name: row.name,
          urlPlaceholder: urlPlaceholder,
        });
      })
      .on("end", async () => {
        for (const preference of preferences) {
          try {
            await prisma.preference.create({ data: preference });
            console.log(`Inserted ${preference.name}`);
          } catch (error) {
            console.error(`Error inserting ${preference.name}:`, error);
          }
        }
        resolve();
      })
      .on("error", (error) => {
        reject(error);
      });
  });
};

const main = async () => {
  try {
    await seedPlaces();
    await seedPreferences();
  } catch (error) {
    console.error("Error seeding data:", error);
  } finally {
    await prisma.$disconnect();
  }
};

main().catch((e) => {
  console.error(e);
  prisma.$disconnect();
});
