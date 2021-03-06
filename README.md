# README [![CircleCI](https://circleci.com/gh/friederikewild/demoFruitViewer.svg?style=svg&circle-token=1be52784e95b73fb58f601c99c19916d4eff6806)](https://circleci.com/gh/friederikewild/demoFruitViewer) [![Build Status](https://www.bitrise.io/app/f0f0f7962271ec9f/status.svg?token=4PIp8fbLPtiYfGehFS6Hcw)](https://www.bitrise.io/app/f0f0f7962271ec9f)

Android MVP Android demo showing fruit fact cards. The app showcases usage of MVP + Clean Architecture combined with rxJava. 
The look follows the Material Design Guidelines with usage of cards and material system icons.


### Fruits Demo App

* Implemented following [MVP architecture](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93presenter)
* Split into three layers with data (including repositories), domain and presentation layer inspired by [Clean Architecture](https://8thlight.com/blog/uncle-bob/2012/08/13/the-clean-architecture.html) and [Google Android Architecture Blueprints](https://github.com/googlesamples/android-architecture)

* Fetch json test data and cache elements per id for quick detail viewing

* Data is provided between layers via rxJava Flowables


### Screens

* **Overview** provides RecyclerView with list and grid display option (aka *OverviewLayoutType*). Pull2Refresh is available to fetch again when first started without network connection

  * Using CardView for fruits in list since content is more than three lines of text
  * Card layouts follow [Material Design Guidelines](https://material.io/guidelines/components/cards.html#cards-content)

<img src="https://github.com/friederikewild/demoFruitViewer/blob/gh-pages/docs/overview_list.png" width="300" alt="Overview List" />  <img src="https://github.com/friederikewild/demoFruitViewer/blob/gh-pages/docs/overview_grid.png" width="300" alt="Overview Grid" />

<img src="https://github.com/friederikewild/demoFruitViewer/blob/gh-pages/docs/overview_grid_land.png" height="300" alt="Overview Grid Landscape" />

* **Detail** provides larger image of one entry, square and fully visible in both portrait and landscape

<img src="https://github.com/friederikewild/demoFruitViewer/blob/gh-pages/docs/detail.png" width="300" alt="Detail Portrait" />  <img src="https://github.com/friederikewild/demoFruitViewer/blob/gh-pages/docs/detail_land.png" height="300" alt="Detail Ladscape" />


### Notes

* View state of Overview screen (active list/grid layout style) is stored into the *Bundle* by the Presenter. When coming back from a Detail the type is restored as well.

* The scrolling position of the *RecyclerView* is currently not restored. Using *currentLayoutManager.onSaveInstanceState()* did not look as smooth on rotation. Therefore left out, until the full list is cached and the list can be immediately set and scrolled when resuming the view.

* FruitsRepository is currently always fetching the full list from the server when requested. Fruits are only cached on a weak map basis for the detail screen.

* Test data is provided as json and picture assets via Github pages on additional branch [gh-pages](https://github.com/friederikewild/demoFruitViewer/tree/gh-pages)
