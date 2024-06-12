const { PrismaClient } = require("@prisma/client");
const prisma = new PrismaClient();

async function main() {
  for (let i = 0; i < 3; i++) {
    const place = await prisma.place.create({
      data: {
        name: `place${i + 1}`,
        latitude: i,
        longitude: i,
        address: `addr${i}`,
      },
    });
    console.log({ place });
  }
  for (let i = 0; i < 5; i++) {
    const preference = await prisma.preference.create({
      data: {
        name: `preference${i}`,
        urlPlaceholder: `url${i}`,
      },
    });
    console.log({ preference });
  }
}
main()
  .then(async () => {
    await prisma.$disconnect();
  })
  .catch(async (e) => {
    console.error(e);
    await prisma.$disconnect();
    process.exit(1);
  });
