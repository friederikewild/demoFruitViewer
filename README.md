# README [![CircleCI](https://circleci.com/bb/eyewellbeing/touchnotedemo.svg?style=svg&circle-token=6df3bafb890a179ecdbee3196ad2f434e4dcacad)](https://circleci.com/bb/eyewellbeing/touchnotedemo)

Android assignment for Touchnote by Friederike Wild.

### RecyclerView Demo App

* Fetch provided json test data
* **Overview** provides list and grid display option
* **Detail** provides larger image of one entry

### Notes
* Implemented following MVP architecture
* Asynchronous Domain Layer Use Case handling inspired by [Google Android Architecture Blueprints](https://github.com/googlesamples/android-architecture)
* Using CardView for items in list since content is more than three lines of text
* Card layouts follow [Material Design Guidelines](https://material.io/guidelines/components/cards.html#cards-content)
