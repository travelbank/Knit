[![](https://jitpack.io/v/travelbank/Knit.svg)](https://jitpack.io/#travelbank/Knit)
[![Build Status](https://travis-ci.org/travelbank/Knit.svg?branch=master)](https://travis-ci.org/travelbank/Knit)   

Api Documentation : [Knit Docs](https://travelbank.github.io/Knit/)

Issues and comments: omer@travelbank.com / [Omer Ozer](https://github.com/OmerUygurOzer)

# Knit 
MVP Framework for Android apps. 

Features:

1. Event based system pattern as shown here : [Android MVP — Doing it “right”](https://android.jlelse.eu/android-mvp-doing-it-right-dac9d5d72079)  
2. Smart Presenters: they outlive the activities they are attached to and persist their states unless memory is low.
3. Contracts are auto-generated.
4. No need to tie components(Model,View,Presenter) together, all communication is handled under the hood.
5. Umbrella Models: Models can fetch data from other models and expose a single data type. 
6. View-States supported: User can handle configuration changes such as screen rotation.
7. Navigation is supported as outlined here : [Navigation in the context of MVP](https://medium.com/@nikita.kozlov/navigation-in-the-context-of-mvp-f474ed313901)  
8. Easy integration with other libraries such as Dagger.

Version 1.3

- Fragment navigation support on presenters
- Atomic entity instances prevents NPE's & leaks caused by un-finished async tasks.
- Support for reporting errors from Generators added
- Test Kit added to make testing easy
- Support for individual handlers for View events
- Bug fixes
- Full API documentation

Version 2.0

- Tuples to create simple data wrappers when passing multiple objects around
- Interactors added to avoid having to use of generators for simple non-asnyc get/set operations
- View attachments such as Adapters. View events can now be fired from inside any view attachments.
- Intent flags added to the Navigator


### Adding Knit to the project:
```
allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}

Stable version 1.3.0

dependencies {
    implementation 'com.github.travelbank.Knit:knitlib:v2.0.0'
    annotationProcessor 'com.github.travelbank.Knit:knitprocessor:v2.0.0'
    testImplementation 'com.github.travelbank.Knit:knittestkit:v2.0.0'
}
```


Models:
```java
@Model
public class RestLayer extends KnitModel {

    public static final String GET_REPOS = "i";

    @Inject
    ApiManager apiManager;

    @Override
    public void onCreate() {
        DaggerModelsComponent.create().inject(this);
    }

    @Collects(value = "umbrella" , needs = {"testN","test"})
    Generator0<String> collector = new Generator0<String>() {

        @Override
        public KnitResponse<String> generate() {
            Log.d("KNIT_TEST","UMBRELLA CALL");
            KnitResponse<String> t1 = requestImmediately("testN","YAHH");
            KnitResponse<String> t2 = requestImmediately("test");
            String result = t1.getBody() + "=/=" + t2.getBody();
            return new KnitResponse<String>(result);
        }
    };

    @Inputs("input")
    Inputter1<String> stringInputter = new Inputter1<String>() {
        @Override
        public void input(String param1) {

        }
    };
    
    @Generates("test")
    public Generator0<String> generateTestString = new Generator0<String>() {

        @Override
        public KnitResponse<String> generate() {
            Log.d("KNIT_TEST","TEST CALL");
            return new KnitResponse<>("TEEEESST STRING");
        }
    };
}
```

Views:
```java
@KnitView
public class InputActivity extends Activity{

    public static final String SEARCH_CLICK = "sc";

    EditText editText;

    Button button;
    
    ViewEvents viewEvents;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        editText = (EditText)findViewById(R.id.edit_text);
        button = (Button)findViewById(R.id.button);
        viewEvents = Knit.getInstance().getViewEvents();
        viewEvents.onClick(SEARCH_CLICK,this, button);
    }

    public String getUserName(){
        return editText.getText().toString().trim();
    }


}
```

Presenters:
```java
@Presenter(RepoActivity.class)
public class RepoActivityPresenter extends KnitPresenter<RepoActivityContract> {


@Override
    public void receiveMessage(KnitMessage message) {
        super.receiveMessage(message);
        this.string = message.<String>getData("txt");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("KNIT_TEST","PRESENTER TWO CREATED");
    }


    @Override
    public void onViewStart() {
        super.onViewStart();
        getContract().recMes(getInteractor().getTestString());
    }

    @Override
    public void onViewResume() {
        super.onViewResume();
    }

    
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("KNIT_TEST","PRESENTER TWO DESTROYED");
    }

    @ViewEvent(BUTTON_CLICK)
    public void handle(ViewEventEnv eventEnv) {
        getNavigator()
                .toActivity()
                .from(getView())
                .setMessage(newMessage().putData("txt",getContract().get()))
                .target(SecondActivity.class)
                .go();
    }

    @ModelEvent("umbrella")
    void updateData2(KnitResponse<String> data){
        getContract().recMes(data.getBody());
    }

    @ModelEvent("Ttest")
    void updateDatat2(KnitResponse<List<StringWrapper>> data){
        getContract().recMes(data.getBody().get(0).string);
    }
}

Adding attachments to views:

Knit.getInstance().getAttachmentMap()
                .attach()
                .toView(this)//Activity, fragments etc...
                .add(adapter1)
                .add(adapter2).done();

```


### Technical Details

Component Initialization:

Components are initialized by Knit framework on demand as it will be explained below. Presenters and Models have lifecycle callbacks we are all familiar with such as ```onCreate()``` ,```onDestroy()``` . Knit is smart in managing lifecycles of the components but it is still your duty to free up heavy objects on do any kind of `unsubscribe` operation inside ```onDestroy()``` . 

LifeCycle Management:

Components initializations are triggered by the initialization of the views. Once a view is created, then first , all models it requires will be created, then it's presenter. The presenter and views are tied together(1to1). However, each presenter may require multiple models. Initialization is done by keeping a track of usage of each component(Reference count). Every time a view that depends ona  model is initialized. The refernce counts for those models will be incremented. If it goes up to 1 from 0. it will be initialized and `onCreate()` will be called. If the view is destroyed and model no longer is needed, the reference count will be decremented. If it gets to 0 , it will be destroyed and `onDestroy()` will be called. 

Knit will automatically determine your dependencies by going scanning your presenter class for `ModelEvent` and marking the models that generate the values that are required by your presenter as a dependency. So when the view is created, these models will also be initialized for you. However, if your presenter simply inputs data and does not listen for any, then you'll have to add these data fields to your `Presenter` annotation such as ``` @Presenter(value = {MyActivity.class}, needs={TestModel.DATA})``` . This will allow Knit to know that ```TestModel``` is indeed a dependency for `MyActivity` .

To utilize "Umbrella" models. You'll have to do something similar and add these data fields to your model's `Collects` tag such as ``` @Collects(value = "umbrella" , needs = {"data1","data2"})```. This way Knit will find models that generates these data fields and mark them as a dependency for your "Umbrella" model.

Singleton models will be initialized lazily. Meaning they won't be booted until a view that depends on them is created.Once booted, they'll never be cleared so be careful with these.

![Component Lifecycles](https://github.com/travelbank/Knit/blob/master/docs/KnitLifecyclesPng.png)

Component Communication:

Knit is an event based framework. Meaning all components communicate via events. The only exception being presenter to view communication. The reason for this is the need to keep views as less verbose as possible. View is wrapper around by a Contract. This contract then is exposed to the presenter.
All Non-Android specific and non-private methods of the view will be automatically added to the contract by the KnitProcessor. For everything else. Events will be used. Since the presenter is the intermediate between the views and the models, it will listen to both ```ViewEvents``` and ```ModelEvents```.
To listen to a model, simply create a non-private method and annotate it with a ```ModelEvent``` that has the data tag that you'd like to listen to . The method should only accept the associated ```KnitResponse``` as a param. Doing this will also allow Knit to add the Model that generates that data as a dependency for your view. 


![Component Communication](https://github.com/travelbank/Knit/blob/master/docs/KnitComponentsPng.png)


Concurrency:

Concurrency in Knit is achieved through `Schedulers`. 

`IOScheduler` : Runs task on a ThreadPool of 4. Tasks are distributed evenly. Results are reported to a separated `receiver` thread. This thread then handles the task of sending the result to the following scheduler and calling the consumer on that scheduler. Best use cases: Rest calls, local database look-ups, computations.

`MainScheduler`: Runs tasks on the AndroidMain thread. Often used to report back to the UI thread after having completed tasks asynchronuously.

`ImmediateScheduler`: Runs tasks on the thread it's called on. If you run tasks and `IOScheduler` and consume them on `ImmediateScheduler` , the consume operation will be done on the `receiver` thread of the IOScheduler since that's the thread responsible for handling the consume operation. If the `ImmeduateScheduler` is called after the `MainScheduler` then the operation will run on the UI thread. 

`HeavyScheduler`: Designed for heavier tasks such as downloading files/images. The thread pool for this is different than the one from `IOScheduler`. These tasks will outlive the life-cycle of all components and keep running in the background unless there's an error or they are completed. Only 4 heavy tasks can be ran simultaneously.

Accessing Generators: Accessing Models is thread safe. Multiple threads can access the same model . However, each generator's generate block is behind it's own thread lock. So access to generate blocks will be atomic. For this reason. try not to run the same geneator on different threads at the same time. If a model has multiple generators using the same resources. You should have your own concurrency system in hand to avoid issues.




