# README [![CircleCI](https://circleci.com/bb/eyewellbeing/touchnotedemo.svg?style=svg&circle-token=6df3bafb890a179ecdbee3196ad2f434e4dcacad)](https://circleci.com/bb/eyewellbeing/touchnotedemo)

Android MVP demo showing fruit facts by Friederike Wild.

### Fruits Demo App

* Implemented following MVP architecture
* Split into three layers with data (including repositories), domain and presentation layer inspired by [Clean Architecture](https://8thlight.com/blog/uncle-bob/2012/08/13/the-clean-architecture.html) and [Google Android Architecture Blueprints](https://github.com/googlesamples/android-architecture)

* Fetch json test data and cache elements per id for quick detail viewing

* Data is provided between layers via rxJava Flowables

### Screens

* **Overview** provides RecyclerView with list and grid display option (aka *OverviewLayoutType*). Pull2Refresh is available to fetch again when first started without network connection

** Using CardView for fruits in list since content is more than three lines of text
** Card layouts follow [Material Design Guidelines](https://material.io/guidelines/components/cards.html#cards-content)

* **Detail** provides larger image of one entry, square and fully visible in both portrait and landscape


### Notes

* View state of Overview screen (active list/grid layout style) is stored into the *Bundle* by the Presenter. When coming back from a Detail the type is restored as well.

* The scrolling position of the *RecyclerView* is currently not restored. Using *currentLayoutManager.onSaveInstanceState()* did not look as smooth on rotation. Therefore left out, until the full list is cached and the list can be immediately set and scrolled when resuming the view.

* FruitsRepository is currently always fetching the full list from the server when requested. Fruits are only cached on a weak map basis for the detail screen.

