package com.example.mediahub.navigation
// this defines the navigation paths to our screens definations
// inside Screens.kt
// allows us to write compose functions // render composable elements
import android.window.SplashScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
// this is the navigation manager : allows movement
// from one screen to another through path defination
import androidx.navigation.NavHostController
// allows us to define navigation type i.e
// backstack : previous screen  // foreground : screen in view
import androidx.navigation.NavType
// container for all our navigation screens
import androidx.navigation.compose.NavHost
// allows defination of navigation composable functions
import androidx.navigation.compose.composable
// carries path route name to different screens // navigation
import androidx.navigation.navArgument
// importing all our screens
import com.example.mediahub.ui.screens.*
@Composable
fun MediaHubNavGraph(navController: NavHostController){
    // we define our navigation container
    // stipulate the default start destination(where does
// the app start)
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ){
          // state our app screens that are def. in screen.
        composable(Screen.Splash.route){
            SplashScreen(navController)
        }
        composable(Screen.Login.route){
            LoginScreen(navController)
        }
        composable(Screen.Register.route){
            RegisterScreen(navController)
        }
        composable(Screen.ForgotPassword.route){
            ForgotPasswordScreen(navController)
        }
        composable(Screen.Dashboard.route){
            DashboardScreen(navController)
        }
        composable(Screen.UploadMedia.route){
            UploadMediaScreen(navController)
        }
        // for screens which require info on navigation
        // we use the arguments attribute together with
        // navtype to define data type of info shared to
        // access route
        composable(route=Screen.MediaDetail.route,
            arguments = listOf(navArgument("mediaId")
            {type = NavType.StringType}) ){ backStack ->
                // inside we maintain a backstack
            // i.e. when user presses back we go back to
            // the previous screen without the ID
            // pick up the mediaId , if not present replace
            // with an empty string
            val mediaId = backStack.arguments?.
            getString("mediaId") ?: ""
            MediaDetailScreen(navController, mediaId)
        }
        composable(route=Screen.EditMedia.route,
            arguments = listOf(navArgument("mediaId")
            {type = NavType.StringType}) ){ backStack ->
            val mediaId = backStack.arguments?.
            getString("mediaId") ?: ""
            EditMediaScreen(navController, mediaId)
        }
    }
}













