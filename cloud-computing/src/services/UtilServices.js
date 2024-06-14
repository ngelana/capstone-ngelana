function parseDate(dateString) {
  const [month, day, year] = dateString.split("/");
  const date = new Date(Date.UTC(year, month - 1, day)); // Use UTC to avoid timezone issues
  return date;
}

module.exports = parseDate;
