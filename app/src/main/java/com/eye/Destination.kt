package com.eye

open class Destination(open val route: String) {
    object Screen{
        object UserScreen : Destination("user-screen")
        object AdminScreen : Destination("admin-screen")
    }
}