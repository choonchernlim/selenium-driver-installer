package com.github.choonchernlim.selenium

import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.attribute.PosixFilePermission

class SeleniumDriverInstallerSpec extends Specification {

    def "chrome - given execution, system property should be set and file should be executable"() {
        given:
        assert !System.getProperty(SeleniumChromeDriverInstaller.CHROME_DRIVER_EXE_PROPERTY)?.trim()

        when:
        SeleniumDriverInstaller.chrome()

        then:
        def filePath = System.getProperty(SeleniumChromeDriverInstaller.CHROME_DRIVER_EXE_PROPERTY)
        filePath =~ /.*?\/chromedriver-\d+.tmp/

        def file = new File(filePath)
        file.exists()

        Files.getPosixFilePermissions(file.toPath()) == [
                PosixFilePermission.OWNER_READ,
                PosixFilePermission.OWNER_WRITE,
                PosixFilePermission.OWNER_EXECUTE
        ] as Set

        cleanup:
        System.clearProperty(SeleniumChromeDriverInstaller.CHROME_DRIVER_EXE_PROPERTY)
    }
}
