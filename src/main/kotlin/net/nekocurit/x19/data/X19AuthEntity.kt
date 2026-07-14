package net.nekocurit.x19.data

import net.nekocurit.x19.WPLauncherAccountAPI

abstract class X19AuthEntity: X19Entity() {
    lateinit var api: WPLauncherAccountAPI
}
