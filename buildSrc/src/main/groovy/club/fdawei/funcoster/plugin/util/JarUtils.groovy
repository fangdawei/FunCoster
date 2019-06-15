package club.fdawei.funcoster.plugin.util

import com.android.utils.FileUtils

import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

/**
 * Created by david on 2019/06/12.
 */
final class JarUtils {

    /**
     * 解压jar包
     * @param srcPath
     * @param destPath
     */
    static void unzipJarFile(String srcPath, String destPath) {
        def file = new File(srcPath)
        if (!file.exists()) {
            return
        }
        def destDir = new File(destPath)
        destDir.mkdirs()
        def jarFile = new JarFile(file)
        def entries = jarFile.entries()
        while (entries.hasMoreElements()) {
            def jarEntry = entries.nextElement()
            def outputFile = new File(destDir, jarEntry.name)
            if (jarEntry.directory) {
                outputFile.mkdirs()
                continue
            }
            outputFile.parentFile.mkdirs()
            def outputStream = new FileOutputStream(outputFile)
            def inputStream = jarFile.getInputStream(jarEntry)
            outputStream << inputStream
            outputStream.close()
            inputStream.close()
        }
        jarFile.close()
    }

    /**
     * 压缩生成jar包
     * @param srcPath
     * @param destPath
     */
    static void zipJarFile(String srcPath, String destPath) {
        def srcDir = new File(srcPath)
        if (!srcDir.exists()) {
            return
        }
        def destFile = new File(destPath)
        FileUtils.deleteIfExists(destFile)
        if (destFile.parentFile != null) {
            destFile.parentFile.mkdirs()
        }
        def jarOutputStream = new JarOutputStream(new FileOutputStream(destFile))
        srcDir.eachFileRecurse { file ->
            def entryName = FileUtils.relativePath(file, srcDir)
            jarOutputStream.putNextEntry(new ZipEntry(entryName))
            if (!file.directory) {
                def inputStream = new FileInputStream(file)
                jarOutputStream << inputStream
                inputStream.close()
            }
        }
        jarOutputStream.close()
    }
}
