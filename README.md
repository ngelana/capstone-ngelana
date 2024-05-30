# Data-Extractor-Hore
This is a repository for tools that Machine Learning path use to scrape data from Google Maps without breaking terms and conditions for Bangkit 2024 Capstone Project.

---
> For the Capstone Group Repository, visit the
> [Capstone Ngelana Repository](https://github.com/REDummy/capstone-hore)

## Requirements
This dataset extractor requires you to use:

- [Python 3.11](https://www.python.org/)
- Pip install the [requirements.txt](https://github.com/anggerbudi/Data-Extractor-Hore/blob/main/requirements.txt)
- Create and use your own Google Cloud Application Default Credentials for API key and authentication


## Notes
The extractor currently still use circle radius for the nearby search. 
This means that it has flaws and efficiency problem while scraping places data.
We planning to use square based area for next extraction to reduce API call and fix the blankspot flaw

![area problem](https://github.com/anggerbudi/Data-Extractor-Hore/assets/31381698/051a0502-f905-4db9-ac78-a90d89c0c8cb)


## TODO

- Proceed using square area for the places extraction to avoid blankspot
