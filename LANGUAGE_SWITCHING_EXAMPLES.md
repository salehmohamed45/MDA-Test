# Runtime Language Switching Examples

This document provides practical examples of using the runtime language switching feature.

## Example 1: Simple Screen with Language Switching

```kotlin
@Composable
fun MovieListScreen() {
    // Language-aware string
    val title = localizedString(LocalizationKeys.HOME_POPULAR_MOVIES)
    
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge
        )
        
        // When language changes, this Text will automatically 
        // recompose with the new translation
    }
}
```

## Example 2: Form with State Preservation

```kotlin
@Composable
fun ReviewForm() {
    // State is preserved during language change
    var reviewText by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(0) }
    
    Column {
        // Labels update, but input values preserved
        Text(localizedString(LocalizationKeys.REVIEW_RATING))
        
        OutlinedTextField(
            value = reviewText,
            onValueChange = { reviewText = it },
            label = { 
                // This label updates when language changes
                Text(localizedString(LocalizationKeys.REVIEW_TEXT_PLACEHOLDER)) 
            }
        )
        // reviewText value stays the same even after language change!
        
        RatingBar(
            rating = rating,
            onRatingChange = { rating = it },
            label = localizedString(LocalizationKeys.REVIEW_YOUR_RATING)
        )
        // rating value preserved!
    }
}
```

## Example 3: Navigation State Preservation

```kotlin
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    // Navigation stack preserved during language change
    NavHost(navController, startDestination = "home") {
        composable("home") { 
            HomeScreen(navController) 
        }
        composable("details/{id}") { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("id")
            MovieDetailsScreen(movieId)
        }
    }
    
    // If user is on details screen and changes language:
    // 1. They stay on details screen
    // 2. UI updates to new language
    // 3. Back button still works
    // 4. Can still navigate forward/back
}
```

## Example 4: ViewModel State Preservation

```kotlin
class MovieViewModel : ViewModel() {
    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies.asStateFlow()
    
    fun loadMovies() {
        viewModelScope.launch {
            // Load movies from API
            _movies.value = repository.getPopularMovies()
        }
    }
}

@Composable
fun MovieListWithViewModel(viewModel: MovieViewModel = viewModel()) {
    val movies by viewModel.movies.collectAsState()
    
    LazyColumn {
        items(movies) { movie ->
            MovieCard(movie)
        }
    }
    
    // When language changes:
    // 1. ViewModel instance preserved
    // 2. movies list still in memory
    // 3. Only UI text updates
    // 4. No need to reload from API!
}
```

## Example 5: Number and Date Formatting

```kotlin
@Composable
fun MovieStats(movie: Movie) {
    Column {
        // Number formatting per locale
        Text("${localizedString(LocalizationKeys.DETAIL_BUDGET)}: ${localizedCurrency(movie.budget)}")
        // English: Budget: $150,000,000
        // German: Budget: 150.000.000 $
        // Arabic: ١٥٠٬٠٠٠٬٠٠٠ $
        
        // Date formatting per locale
        Text("${localizedString(LocalizationKeys.DETAIL_RELEASE_DATE)}: ${localizedDate(movie.releaseDate)}")
        // English: Release Date: Dec 25, 2023
        // German: Release Date: 25. Dez. 2023
        // Arabic: ٢٥ ديسمبر ٢٠٢٣
        
        // Number formatting
        Text("${localizedString(LocalizationKeys.DETAIL_RATING)}: ${localizedNumber(movie.voteCount)}")
        // English: Rating: 1,234,567
        // German: Rating: 1.234.567
        // Arabic: ١٬٢٣٤٬٥٦٧
    }
}
```

## Example 6: RTL-Aware Layout

```kotlin
@Composable
fun MovieCard(movie: Movie) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        // Arrangement automatically reverses for RTL
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // In LTR: Image | Title > 
        // In RTL: < Title | Image
        
        AsyncImage(
            model = movie.posterPath,
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )
        
        Column(
            modifier = Modifier.weight(1f).padding(start = 8.dp)
            // padding(start) automatically becomes padding(end) in RTL
        ) {
            Text(movie.title)
            Text(localizedDate(movie.releaseDate))
        }
        
        Icon(
            imageVector = Icons.Default.ChevronRight,
            // Automatically flips to ChevronLeft in RTL
            contentDescription = null
        )
    }
}
```

## Example 7: Complete Screen with All Features

```kotlin
@Composable
fun CompleteMovieScreen(
    movieId: Int,
    viewModel: MovieDetailsViewModel = viewModel()
) {
    // State preserved
    val movie by viewModel.movie.collectAsState()
    var userRating by remember { mutableStateOf(0) }
    var reviewText by remember { mutableStateOf("") }
    
    // Automatic RTL detection
    val isRTL = LocalizationFormatters.isRTL()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Title updates on language change
        Text(
            text = localizedString(LocalizationKeys.MOVIE_DETAILS),
            style = MaterialTheme.typography.headlineLarge
        )
        
        // Movie data preserved, only labels update
        movie?.let { m ->
            Text("${localizedString(LocalizationKeys.DETAIL_TITLE)}: ${m.title}")
            Text("${localizedString(LocalizationKeys.DETAIL_RELEASE_DATE)}: ${localizedDate(m.releaseDate)}")
            Text("${localizedString(LocalizationKeys.DETAIL_BUDGET)}: ${localizedCurrency(m.budget)}")
            Text("${localizedString(LocalizationKeys.DETAIL_RATING)}: ${localizedNumber(m.voteCount)}")
        }
        
        Spacer(Modifier.height(16.dp))
        
        // Form state preserved during language change
        Text(localizedString(LocalizationKeys.YOUR_REVIEW))
        
        OutlinedTextField(
            value = reviewText, // ← Preserved!
            onValueChange = { reviewText = it },
            label = { Text(localizedString(LocalizationKeys.REVIEW_TEXT_PLACEHOLDER)) },
            modifier = Modifier.fillMaxWidth()
        )
        
        RatingStars(
            rating = userRating, // ← Preserved!
            onRatingChange = { userRating = it }
        )
        
        Button(
            onClick = { viewModel.submitReview(reviewText, userRating) },
            modifier = Modifier.align(
                if (isRTL) Alignment.Start else Alignment.End
            )
        ) {
            Text(localizedString(LocalizationKeys.SUBMIT_REVIEW))
        }
    }
    
    // When user changes language:
    // ✅ All Text() composables update
    // ✅ reviewText value preserved
    // ✅ userRating value preserved
    // ✅ movie data in ViewModel preserved
    // ✅ Layout flips if switching to/from Arabic
    // ✅ Navigation stays on this screen
}
```

## Example 8: Language Selector in Settings

```kotlin
@Composable
fun LanguageSelector() {
    val context = LocalContext.current
    val manager = remember { LocalizationManager(context) }
    val currentLanguage = LocalAppLanguage.current
    val scope = rememberCoroutineScope()
    
    Column {
        LocalizationManager.Language.values().forEach { language ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        scope.launch {
                            manager.setLanguage(language)
                            // Entire app recomposes automatically!
                        }
                    }
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(language.displayName)
                
                if (currentLanguage == language) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
```

## Testing Example

```kotlin
@Test
fun testLanguageSwitchPreservesFormState() {
    val context = InstrumentationRegistry.getInstrumentation().targetContext
    val manager = LocalizationManager(context)
    
    composeTestRule.setContent {
        LocalizationProvider {
            var text by remember { mutableStateOf("Test Input") }
            
            Column {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text(localizedString(LocalizationKeys.SEARCH_PLACEHOLDER)) }
                )
                
                Text("Current: $text")
            }
        }
    }
    
    // Verify initial state
    composeTestRule.onNodeWithText("Test Input").assertIsDisplayed()
    
    // Change language
    runBlocking {
        manager.setLanguage(LocalizationManager.Language.GERMAN)
    }
    
    // Wait for recomposition
    composeTestRule.waitForIdle()
    
    // Verify text input preserved
    composeTestRule.onNodeWithText("Test Input").assertIsDisplayed()
    
    // Verify label updated to German
    composeTestRule.onNodeWithText("Film, Sendungen, Personen suchen...").assertIsDisplayed()
}
```

## Performance Tips

### Do:
```kotlin
// ✅ Use localizedString() composable
Text(localizedString(LocalizationKeys.HOME_TITLE))

// ✅ Remember expensive computations
val formattedDate = remember(movie.releaseDate) {
    localizedDate(movie.releaseDate)
}

// ✅ Use ViewModel for data
val movies by viewModel.movies.collectAsState()
```

### Don't:
```kotlin
// ❌ Don't hardcode strings
Text("Popular Movies")

// ❌ Don't create new manager every time
fun getTitle(): String {
    val manager = LocalizationManager(context) // Creates new instance!
    return manager.getString(...)
}

// ❌ Don't recreate ViewModels
LaunchedEffect(appLanguage) {
    viewModel = MovieViewModel() // DON'T DO THIS
}
```

## Common Patterns

### Loading State with Localization
```kotlin
@Composable
fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(Modifier.height(8.dp))
            Text(localizedString(LocalizationKeys.COMMON_LOADING))
        }
    }
}
```

### Error State with Localization
```kotlin
@Composable
fun ErrorState(error: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = localizedString(LocalizationKeys.ERROR_OCCURRED),
            style = MaterialTheme.typography.headlineSmall
        )
        Text(error)
        Button(onClick = onRetry) {
            Text(localizedString(LocalizationKeys.COMMON_RETRY))
        }
    }
}
```

These examples demonstrate the complete implementation of runtime language switching with state preservation across the entire app.
