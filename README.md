# README [![CircleCI](https://circleci.com/bb/eyewellbeing/touchnotedemo.svg?style=svg&circle-token=6df3bafb890a179ecdbee3196ad2f434e4dcacad)](https://circleci.com/bb/eyewellbeing/touchnotedemo)

Android assignment for Touchnote by Friederike Wild.

### RecyclerView Demo App

* Fetch provided json test data and cache elements per id for quick detail viewing
* **Overview** provides list and grid display option (aka *OverviewLayoutType*). Pull2Refresh is available to fetch again when first started without network connection
* **Detail** provides larger image of one entry, square and fully visible in both portrait and landscape
* UnitTests constructed for presenter/data usage and model mapping while coding those elements


### Notes
* Implemented following MVP architecture

* Using CardView for items in list since content is more than three lines of text
* Card layouts follow [Material Design Guidelines](https://material.io/guidelines/components/cards.html#cards-content)

* Asynchronous Domain Layer Use Case handling inspired by [Google Android Architecture Blueprints](https://github.com/googlesamples/android-architecture)

* View state of Overview screen (active list/grid layout style) is stored into the *Bundle* by the Presenter. When coming back from a Detail the is restored as well.
* The scrolling position of the *RecyclerView* is currently not restored. Using *currentLayoutManager.onSaveInstanceState()* did not look as smooth on rotation. Therefore left out, until the full list is cached and the list can be immediately set and scrolled when resuming the view.

* ItemsRepository is currently always fetching the full list from the server when requested. Items are only cached on a weak map basis for the detail screen.
