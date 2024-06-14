function parseDate(dateString) {
  const [day, month, year] = dateString.split("/");
  const date = new Date(Date.UTC(year, month - 1, day)); // Use Date.UTC to avoid timezone issues
  return date; // INPUT : DD/MM/YYYY ... OUTPUT : YYYY-MM-DDTHH:MM:SS
}
module.exports = parseDate;


