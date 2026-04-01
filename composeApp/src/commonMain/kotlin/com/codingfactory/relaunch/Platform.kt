package com.codingfactory.relaunch

interface Platform { val name: String }

expect fun getPlatform(): Platform