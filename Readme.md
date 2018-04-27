# Attention Reviewer(s)

The API Key to allow the project to hit themoviedb.org's endpoints must be passed to the following method in `populateUI()` in `HomeActivity.java`

```java
// Pass your key in place of the APIKey.KEY argument

mMovieRequestor = new MovieRequestor(this, APIKey.KEY, this);
```