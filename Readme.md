# Attention Reviewer(s)

The API Key to allow the project to hit themoviedb.org's endpoints must be passed to the `MovieRequestor` constructor located in `populateUI(Bundle savedInstanceState)` found in the `HomeActivity.java` file

```java
// Pass your key in place of the APIKey.KEY argument

mMovieRequestor = new MovieRequestor(this, APIKey.KEY, this);
```