const prisma = require("../db");

async function isValidUserId(id) {
  try {
    const isValid = await prisma.user.findUnique({
      where: {
        id,
      },
    });
    return !!isValid;
  } catch (error) {
    console.error(error);
    return false;
  }
}

async function getPlacesWithUrlPlaceholder() {
  try {
    // Fetch all places
    const places = await prisma.place.findMany();

    // Fetch all preferences
    const preferences = await prisma.preference.findMany();

    // Combine the data based on primaryTypes and name
    const placesWithUrlPlaceholders = places.map((place) => {
      const matchedPreference = preferences.find(
        (pref) => pref.name === place.primaryTypes
      );
      return {
        ...place,
        urlPlaceholder: matchedPreference
          ? matchedPreference.urlPlaceholder
          : null,
      };
    });

    // Return the combined data
    return placesWithUrlPlaceholders;
  } catch (error) {
    console.error(error);
    throw error;
  }
}

async function getPlacesWithUrlPlaceholdersByPlaceID(ids) {
  try {
    // Fetch all places
    const places = await prisma.place.findMany({
      where: {
        id: { in: ids },
      },
    });

    // Fetch all preferences
    const preferences = await prisma.preference.findMany();

    // Combine the data based on primaryTypes and name
    const placesWithUrlPlaceholders = places.map((place) => {
      const matchedPreference = preferences.find(
        (pref) => pref.name === place.primaryTypes
      );
      return {
        ...place,
        urlPlaceholder: matchedPreference
          ? matchedPreference.urlPlaceholder
          : null,
      };
    });

    // Return the combined data
    return placesWithUrlPlaceholders;
  } catch (error) {
    console.error(error);
    throw error;
  }
}

async function getPlacesWithUrlPlaceholderByType(type) {
  try {
    // Fetch all places matching the query
    const places = await prisma.place.findMany({
      where: {
        primaryTypes: type,
      },
    });

    // Fetch all preferences
    const preferences = await prisma.preference.findMany();

    // Combine the data based on primaryTypes and name
    const placesWithUrlPlaceholders = places.map((place) => {
      const matchedPreference = preferences.find(
        (pref) => pref.name === place.primaryTypes
      );
      return {
        ...place,
        urlPlaceholder: matchedPreference
          ? matchedPreference.urlPlaceholder
          : null,
      };
    });

    // Return the combined data
    return placesWithUrlPlaceholders;
  } catch (error) {
    console.error(error);
    throw error;
  }
}

async function getPlacesWithUrlPlaceholderByQuery(query) {
  try {
    // Fetch all places matching the query
    const places = await prisma.place.findMany({
      where: {
        name: {
          contains: query,
        },
      },
    });

    // Fetch all preferences
    const preferences = await prisma.preference.findMany();

    // Combine the data based on primaryTypes and name
    const placesWithUrlPlaceholders = places.map((place) => {
      const matchedPreference = preferences.find(
        (pref) => pref.name === place.primaryTypes
      );
      return {
        ...place,
        urlPlaceholder: matchedPreference
          ? matchedPreference.urlPlaceholder
          : null,
      };
    });

    // Return the combined data
    return placesWithUrlPlaceholders;
  } catch (error) {
    console.error(error);
    throw error;
  }
}

async function getPlaceWithUrlPlaceholderbyId(id) {
  try {
    // Fetch all places
    const place = await prisma.place.findUnique({
      where: {
        id,
      },
    });

    if (!place) {
      throw new Error(`Place with id ${id} not found`);
    }

    // Fetch all preferences
    const preferences = await prisma.preference.findMany();

    // Find the matching preference based on primaryTypes and name
    const matchedPreference = preferences.find(
      (pref) => pref.name === place.primaryTypes
    );

    // Combine the data
    const placeWithUrlPlaceholder = {
      ...place,
      urlPlaceholder: matchedPreference
        ? matchedPreference.urlPlaceholder
        : null,
    };

    // Return the combined data
    return placeWithUrlPlaceholder;
  } catch (error) {
    console.error(error);
    throw error;
  }
}

async function getPlacesWithPrimaryType(type) {
  try {
    // Define the mapping of display types to internal types
    const displayTypeMapping = {
      tourist_attraction: "tourist_attraction",
      lodging: "lodging",
      restaurant: "restaurant",
    };

    const internalType = displayTypeMapping[type];

    if (!internalType) {
      throw new Error(`Invalid type: ${type}`);
    }

    console.log(`Searching for places with type: ${internalType}`);

    // Fetch places that contain the internal type in the types field
    const places = await prisma.place.findMany({
      where: {
        types: {
          contains: internalType,
        },
      },
    });

    console.log(`Found places:`, places);

    // Fetch all preferences
    const preferences = await prisma.preference.findMany();

    // Combine the data and include urlPlaceholder based on primaryTypes
    const updatedPlaces = places.map((place) => {
      const matchedPreference = preferences.find(
        (pref) => pref.name === place.primaryTypes
      );

      return {
        ...place,
        urlPlaceholder: matchedPreference
          ? matchedPreference.urlPlaceholder
          : null,
      };
    });

    // Return the combined data
    return updatedPlaces;
  } catch (error) {
    console.error(error);
    throw error;
  }
}

module.exports = {
  getPlaceWithUrlPlaceholderbyId,
  isValidUserId,
  getPlacesWithUrlPlaceholder,
  getPlacesWithUrlPlaceholderByQuery,
  getPlacesWithUrlPlaceholderByType,
  getPlacesWithPrimaryType,
  getPlacesWithUrlPlaceholdersByPlaceID,
};
