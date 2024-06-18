function parseDate(dateString) {
  const [day, month, year] = dateString.split("/");
  const date = new Date(Date.UTC(year, month - 1, day)); // Use Date.UTC to avoid timezone issues
  return date; // INPUT : DD/MM/YYYY ... OUTPUT : YYYY-MM-DDTHH:MM:SS
}

function reverseParseDate(isoDateString) {
  const date = new Date(isoDateString);
  const day = String(date.getUTCDate()).padStart(2, "0");
  const month = String(date.getUTCMonth() + 1).padStart(2, "0");
  const year = date.getUTCFullYear();
  return `${day}/${month}/${year}`;
}

module.exports = { parseDate, reverseParseDate };
