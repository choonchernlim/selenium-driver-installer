package com.github.choonchernlim.selenium

import groovy.transform.PackageScope
import groovy.util.logging.Slf4j

import java.nio.file.Files
import java.nio.file.attribute.PosixFilePermission
import java.util.zip.ZipInputStream

/**
 * This class installs the latest Google Chrome driver.
 */
@Slf4j
@PackageScope
class SeleniumChromeDriverInstaller {
    private static final String BASE_URL = 'https://chromedriver.storage.googleapis.com'
    public static final String CHROME_DRIVER_EXE_PROPERTY = 'webdriver.chrome.driver'

    /**
     * Runs the installer.
     */
    void init() {
        final String latestRelease = "${BASE_URL}/LATEST_RELEASE".toURL().text
        final String downloadUrl = "${BASE_URL}/${latestRelease}/${getDownloadFileName()}"

        log.info("Downloading latest driver from URL [${downloadUrl}]...")

        final File file = writeToFile(downloadUrl)

        makeFileExecutable(file)

        log.info("Setting system property key [${CHROME_DRIVER_EXE_PROPERTY}] with value [${file.absolutePath}]...")

        System.setProperty(CHROME_DRIVER_EXE_PROPERTY, file.absolutePath)
    }

    /**
     * Extracts downloaded zip file and writes it to temp file that will delete on exit.
     *
     * @param downloadUrl Download URL
     * @return Extracted zip file
     */
    private File writeToFile(final String downloadUrl) {
        assert downloadUrl?.trim()

        final File file = File.createTempFile('chromedriver-', null)
        file.deleteOnExit()

        log.info("Extracting downloaded zip content to file [${file.absolutePath}]...")

        try {
            new ZipInputStream(downloadUrl.toURL().newInputStream()).withCloseable { zis ->
                zis.nextEntry

                final byte[] buffer = new byte[2048]

                new BufferedOutputStream(new FileOutputStream(file), buffer.length).withCloseable { bos ->
                    int size

                    while ((size = zis.read(buffer, 0, buffer.length)) != -1) {
                        bos.write(buffer, 0, size)
                    }
                }

                return
            }
        }
        catch (IOException e) {
            throw new RuntimeException('Unable to get latest release zip', e)
        }

        return file
    }

    /**
     * Determines the download file name based on the "os.name" system property.
     * This file name must match with the value in the XML from {@code BASE_URL}.
     *
     * @return File name
     */
    private String getDownloadFileName() {
        final String operatingSystem = System.getProperty('os.name').toUpperCase()

        if (operatingSystem.contains('WIN')) {
            return 'chromedriver_win32.zip'
        }
        else if (operatingSystem.contains('MAC')) {
            return 'chromedriver_mac64.zip'
        }
        else if (operatingSystem.contains('LINUX')) {
            return 'chromedriver_linux64.zip'
        }

        throw new RuntimeException("Unsupported operating system: ${operatingSystem}")
    }

    /**
     * Sets file permission to make it executable by the owner.
     *
     * @param file File
     */
    private void makeFileExecutable(final File file) {
        assert file

        final Set<PosixFilePermission> permissions = [
                PosixFilePermission.OWNER_READ,
                PosixFilePermission.OWNER_WRITE,
                PosixFilePermission.OWNER_EXECUTE
        ]

        log.info("Setting permissions [${permissions.join(', ')}] file [${file.absolutePath}]...")

        try {
            Files.setPosixFilePermissions(file.toPath(), permissions)
        }
        catch (IOException e) {
            throw new RuntimeException('Unable to make file executable', e)
        }
    }
}
