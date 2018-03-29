[![](https://jitpack.io/v/travelbank/Knit.svg)](https://jitpack.io/#travelbank/Knit)
[![Build Status](https://travis-ci.org/travelbank/Knit.svg?branch=master)](https://travis-ci.org/travelbank/Knit)   

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

Version 1.1

- Supports multiple threads for generators to be executed on
- Usage tree for components
- Support for reporting errors from Generators added
- Test Kit added to make testing easy
- Smarter life-cycle callbacks(Removed Knit.show(view), Knit.dismiss(view))
- Access to view through contract is now null safe. Threads won't fire an NPE even if component is killed
- Support for individual handlers for View events
- Bug fixes

### Adding Knit to the project:
```
allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
  implementation 'com.github.travelbank:knit:knitlib:v1.1.0'
  annotationProcessor 'com.github.travelbank:nit:knitprocessor:v1.1.0'
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

    @Generates(GET_REPOS)
    Generator1<List<Repo>,String> getRepos = new Generator1<List<Repo>,String>() {
        @Override
        public List<Repo> generate(String s) {
            try {
                return apiManager.accessCalls().listRepos(s).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
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
        getContract().recMes(string);
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
```


### Technical Details

Component Initialization:

Components are initialized by Knit framework on demand as it will be explained below. Presenters and Models have lifecycle callbacks we are all familiar with such as ```onCreate()``` ,```onDestroy()``` . Knit is smart in managing lifecycles of the components but it is still your duty to free up heavy objects on do any kind of `unsubscribe` operation inside ```onDestroy()``` . 

Memory Management:

Components are initialized on a top down basis top being your view and bottom being models required. Each view has a `component` it's associated with. This component will have a presenter and models. The presenter and views are tied together(1to1). However, each presenter may require multiple models. All components inside the associated `component` will be initialized. Initialized is done by keeping a track of usage of each component. Everytime a model is required for instance, it's usage count will be incremented. If it goes up to 1 from 0. it will be initialized and `onCreate()` will be called. If the component is marked for killing and model no longer is needed, the usage count will be decremented. If it gets to 0 , it will be destroyed and `onDestroy()` will be called. To help you visualize it, check out this image below.

![Usage Tree](https://github.com/OmerUygurOzer/Knit/blob/master/UsageTree.png)

This tree-like structure is called a `UsageTree`. When View1 is shown, Presenter 1 , Misc and Rest models will be created and their `usage count`s will be incremented to 1. So when View2 is shown, they won't be re-created. Their `usage count`s will just be incremented to 2 but the Umbrella and Database models will be initialized and their `usage count`s will be set to 1. When View2 is destroyed however, only Presenter2 , Umbrella and Database models will be destroyed since their `usage count`s will be decremented to 0 . Misc and Rest models will still have a `usage count` of 0 and stay alive.

Concurrency:

Concurrency in Knit is achieved through `Schedulers`. 

`IOScheduler` : Runs task on a ThreadPool of 4. Tasks are distributed evenly. Results are reported to a separated `receiver` thread. This thread then handles the task of sending the result to the following scheduler and calling the consumer on that scheduler. Best use cases: Rest calls, local database look-ups, computations.

`MainScheduler`: Runs tasks on the AndroidMain thread. Often used to report back to the UI thread after having completed tasks asynchronuously.

`ImmediateScheduler`: Runs tasks on the thread it's called on. If you run tasks and `IOScheduler` and consume them on `ImmediateScheduler` , the consume operation will be done on the `receiver` thread of the IOScheduler since that's the thread responsible for handling the consume operation. If the `ImmeduateScheduler` is called after the `MainScheduler` then the operation will run on the UI thread. 

`HeavyScheduler`: Designed for heavier tasks such as downloading files/images. The thread pool for this is different than the one from `IOScheduler`. These tasks will outlive the life-cycle of all components and keep running in the background unless there's an error or they are completed. Only 4 heavy tasks can be ran simultaneously.

Accessing Generators: Accessing Models is thread safe. Multiple threads can access the same model . However, each generator's generate block is behind it's own thread lock. So access to generate blocks will be atomic. For this reason. try not to run the same geneator on different threads at the same time. If a model has multiple generators using the same resources. You should have your own concurrency system in hand to avoid issues.




