# Giftlist

Android mobile application developed for the course in "Design and Implementation of Mobile Applications" at [Politecnico di Milano](http://www.polimi.it/) by [Alessandro Pappalardo](https://github.com/volcacius/) and [Elena Sacchi](https://github.com/Lifeindeath/).

GiftList helps the user to find suitable gift for any person and occasion, creating personalized wishlists. Asking some simple question, the app understands the user's preferences and finds relevant products on online stores, allowing the user to save the ones he likes. It also allows to user to add new products, either from online stores or through an offline mode, to existing wishlist and to eventually help him choose the one he wants to buy.

## Architecture and Design Patterns

The architectural Design Pattern adopted is the MVP (Model-View-Presenter):
- **Model** comprises the business logic: DataBase Management, Helper classes and API connection
- **View** comprises the visualization part and the control part: it processes external inputs with listeners and provides the UI elements
- **Presenter** is the middleman between Model and View: it transforms raw input coming from the model with asynchronous transformations and tells the View when data is ready to be displayed.

We based our MVP implementation on [Ribot's boilerplate](https://github.com/ribot/android-boilerplate)

## Libraries

In the implementation of GiftList, we are willing to use the following libraries:
- [Mosby](https://github.com/sockeqwe/mosby) for MVP calls
- [Butterknife](https://github.com/JakeWharton/butterknife) for inflating views
- [Dagger](http://google.github.io/dagger/) for dependencies injection
- [IcePick](https://github.com/frankiesardo/icepick) for saving and restoring the state
- [Otto](http://square.github.io/otto/) for the EventBus implementation

We will keep this page updated as we further procede in GiftList implementation
