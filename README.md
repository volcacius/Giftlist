#Giftlist
This project was developed for the course "Design and Implementation of Mobile Application at [Politecnico di Milano](www.polimi.it) by [Alessandro Pappalardo](https://github.com/volcacius) and [Elena Sacchi](https://github.com/lifeindeath) and is currently under final revision.
The Giftlist project consists in a shopper assistant oriented to the choice and purchase of gifts for friends and loved ones. It helps the user searching for possible gifts for different people and occasions, proposing them a set of items he can either discard or save in a wishlist later accessible to make the final decision for the purchase.
It does not handle the purchase and delivery themselves: once the user has chosen the perfect gift, the app readdresses him/her to the retailer web page.

#Architecture
##introduction
With Giftlist, we took the opportunity to explore the vast Android ecosystem and implement the most advanced libraries for handling complex backend tasks like dependency injection, http calls, database synchronization; but keeping also in mind that a nice and modern look&feel is essential.
We also decide to base our application on the Model View Presenter design pattern, with which the library we employed fit nicely. 
We now present the libraries we employed and the underlying architecture of Giftlist.

##Model-View-Presenter
Our application is developed following the [Model-View-Presenter](http://hannesdorfmann.com/mosby/mvp/) design pattern: all the core functions were designed exploiting the MVP principles, because they allowed us to decouple well the UI from the backend.

![MVP](https://github.com/volcacius/Giftlist/blob/master/images/MVP.png)

* __Model:__ Contains the data displayed in the user interface. It normally involves the local database, but also external REST services and repositories.
* __Presenter:__ The middle-man between front-end and back-end. Its role is getting user interactions from the View and call the right methods on the Model, or fetching data from model and give them to the View to display them.
* __View:__ the front-end of the application. It handles users inputs and calls the right methods in the presenter. It is also called \textbf{Passive view}, because it does exactly what the presenter says.

It's important to stress the fact that the Presenter and the View are highly decoupled: the Presenter cannot implement Listeners to user inputs, and the View cannot interact directly with the database. In this way, concurrency and multithreading are much easier to handle.

##The LCE implementation of MVP
For our application we choose to adopt the Loading-Content-Error of the MVP pattern provided by the [Mosby MVP library](https://github.com/sockeqwe/mosby). This implementation particularly suits applications which pulls data asynchronously from the web: the Presenter chooses which View (among Loading, Content or Error) to display with respect to the availability of data coming from the Model:
* __Loading:__ View displayed while fetching data.
* __Content:__ View displayed when data are available.
* __Error:__ View displayed if something went wrong or the data are not available.

This schema of the Wishlist Activity and all the related classes well explains how the pattern was implemented:

![MVP implementation](https://github.com/volcacius/Giftlist/blob/master/images/MVP_Lce.png)

All the *Base* classes in the diagram are extended from the Mosby library and take care of all the base MVP mechanism.
The **Adapter** and **Holder** classes are the ones really in charge of displaying the content: we used a **RecyclerView** to display the list of wishlist, and those classes are in charge of handling it.
The input listeners are implemented in the **ViewHolder** class, though the click is first propagate up to the fragment that calls the appropriate methods in the **Presenter**.
The **UseCase** class is the one responsible for the Rx observables: the Presenter does not directly call UseCase methods, but subscribes to them and reacts in response to the changes in the data source (in this case is just the database, but it also work with online repositories).

#Libraries and main technologies
##Rx Java and external services integration
The MVP pattern also allows to works well with the already mentioned [Rx Java paradigm](https://github.com/ReactiveX/RxJava/wiki), a relatively new technology that is of great help in handling asynchronous tasks like calling external APIs or loading large.
We followed [this post](https://labs.ribot.co.uk/android-application-architecture-8b6e34acda65) from Ribot(from which we also take the following image) to wire it up with the MVP pattern.

![MVP and RX](https://github.com/volcacius/Giftlist/blob/master/images/MVP_RxJava.png)

The key idea behind RxJava is the following: an Observable object (the external repositories) emits items (the Products), that are consumed as soon as they are ready by the Subscriber (the Model logic in the MVP pattern).

The Model makes extensive use of Rx technology in the product fetching phase: this way even if the API call retrieves multiple products at once, the first product can be displayed as soon as it is ready, without waiting for the end of the API call. As soon as a product is ready, the Model and subsequently the Presenter is immediately notified and the View can be updated.
For the API call itself we integrated the Rx Java paradigm with two libraries: [OkHttp](https://github.com/square/okhttp), to handle http calls, enrich them with authentication information and deserialize the XML response; and [Retrofit2](https://github.com/square/retrofit), to access the API of external services.

##External service APIs
For the Giftlist application is essential to exploit external web services. We employed three different API services: [Ebay API service](https://go.developer.ebay.com/api-documentation) and [Etsy API service](https://www.etsy.com/developers/documentation/) were exploited to look for product and to propose them to the user, while [European Central Bank](https://sdw-wsrest.ecb.europa.eu/) API service is used for retrieving the up-to-date exchange rates for price conversion.

![API call flow](https://github.com/volcacius/Giftlist/blob/master/images/APIcall.png)

##Dependency Injection
Another powerful paradigm we exploited in the application development is  performing dependency injection (DI) with the [Dagger2](http://google.github.io/dagger/) library. We aimed at a hugely decoupled architecture, and provided an extensive system of models and components to cover all the activities in the application. 

![API call flow](https://github.com/volcacius/Giftlist/blob/master/images/Injection.png)

DI with Dagger2 works around 3 type of classes: Modules, Componenents, and the objects to perform injection in. 

Modules generates the required dependencies through scoped methods, meaning the lifecycle of the instance generated by the method is bounded to its scope, which in our implementation are *@Singleton* and *@PerActivity*. *@Singleton* instances are injected application wise, and are for example the [Picasso](http://square.github.io/picasso/) library instance, that we used for managing the display images, the [StoreIOSQLite](https://github.com/pushtorefresh/storio/blob/master/docs/StorIOSQLite.md) database wrapper, and the [Greenrobot](https://github.com/greenrobot/EventBus) eventbus. 

Components are the glue between modules and the objects to perform injection in. Each component has a specificed scope that bounds its lifecycle. The ApplicationComponent instance is bounded to the entire application lifecycle. In our implementation, Ccomponents such as WishlistComponent have a lifecycle that is bounded to the respective Activity, in this case WishlistActivity. 

In Dagger2, the components are organized into a DAG by taking advantage of the *@SubComponent* annotation. Each component is associated to a set of modules and can access all the *@Provides* of its own and its parents' modules in the DAG, up to the DAG's root. A *@SubComponent* can be the child only of a component with the same or wider scope.

This way, in our implementation, a \texttt{@PerActivity} component can access both the *@PerActivity@Provides* methods in its own modules, and the *@Singleton* ones in the modules of its parent component, which is ApplicationComponent. 

Each *@PerActivity* component is declared in an Activity and is built by the parent component, i.e. ApplicationComponent, by passing from the Activity to the builder a new instance of each module required by the component. By calling the inject method on the component in the class, the DAG is traversed and all the *@Inject* annotated fields in the class are instantiated automatically through all the *@Provides* that are accessible to the component from inject is called from.

By exploiting a novel kind of pattern in the usage of Dagger2, we are also able to inject automatically different kind of objects depending on the type of build, i.e. Debug or Release, at compile time. This way for example we can inject an interceptor of OkHttp requests in debug builds in order to be able to observe the flow of data.

##Annotations
We make extensive use of a variety of libraries that reduce the verbosity of the source code by generating boilerplate code at compile-time through annotations. Such libraries are [Butterknife](http://jakewharton.github.io/butterknife/), [IcePick](https://github.com/frankiesardo/icepick), [ParcelablePlease](https://github.com/sockeqwe/ParcelablePlease), [FragmentArgument](https://github.com/sockeqwe/fragmentargs), and the already described Dagger2. 

The advantage of compile-time code generation is that it doesn't introduce any performance overhead, since reflection is not used.

##Material design
For the User Interface we embraced the general principles of material design. In this subsection we explain the main libraries we employed to get a nice look\&feel:

* __[WelcomeCoordinator:](https://github.com/txusballesteros/welcome-coordinator)__ we used this library to get nice wizards for settings and for the initial tutorial, to avoid boring forms.
* __[AdvancedRecyclerView:](https://github.com/h6ah4i/android-advancedrecyclerview)__ with this library we were able to achieve many UI functionalities like multiple selection and delete, swipe to delete and drag and drop sorting.
* __[MaterialEditText:](https://github.com/rengwuxian/MaterialEditText)__ with this library we enriched the TextViews of the application with material design style and animations.
* __[LikeButton:](https://github.com/jd-alexander/LikeButton)__ this simple library is used to animate the heart and the trash-bin buttons in the save/discard phase.
