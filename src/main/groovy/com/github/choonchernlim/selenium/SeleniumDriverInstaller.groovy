package com.github.choonchernlim.selenium

/**
 * Factory for installing latest selenium drivers.
 */
class SeleniumDriverInstaller {

    /**
     * Installs latest Google Chrome driver.
     */
    static void chrome() {
        new SeleniumChromeDriverInstaller().init()
    }
}
