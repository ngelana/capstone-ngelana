# Data-Extractor-Hore
This is a repository for tools that Machine Learning path use to scrape data from Google Maps without breaking terms and conditions for Bangkit 2024 Capstone Project.

---
> [!IMPORTANT]
> For the Capstone Group Repository, visit the
> [Capstone Ngelana Repository](https://github.com/REDummy/capstone-hore)

## Requirements
This dataset extractor requires you to use:

- [Python 3.11](https://www.python.org/)
- Pip install the [requirements.txt](https://github.com/anggerbudi/Data-Extractor-Hore/blob/main/requirements.txt)
- Create and use your own Google Cloud Application Default Credentials for API key and authentication


## Notes
~~The extractor currently still use circle radius for the nearby search. 
This means that it has flaws and efficiency problem while scraping places data.
We plan to use square based area for next extraction to reduce API call and fix the blankspot flaw~~

Since google maps Places API New only has circle as the location restriction for the places extraction,
we resort to use hexagonal packing strategy to efficiently cover area more than the initial circle strategy. 
Even though it still has blind spot, the blind spot area is reduced greatly

![hexagonal-packing.png](etc/hexagonal-packing.png)

## TODO
- [x] Make code to map all the coordinates for data extraction
- [ ] ~~Proceed using square area for the places extraction to avoid blankspot~~
- [ ] Implement sea and land checking to add only land coordinates
- [ ] Find efficient way to map the place to avoid API calls to search empty space or sea