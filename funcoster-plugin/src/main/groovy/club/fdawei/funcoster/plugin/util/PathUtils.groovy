package club.fdawei.funcoster.plugin.util

/**
 * Create by david on 2019/06/19.
 */
class PathUtils {

    static String relativePath(String filePath, String dirPath) {
        def standardDirPath = dirPath
        if (!standardDirPath.endsWith(File.separator)) {
            standardDirPath = standardDirPath.concat(File.separator)
        }
        return filePath.replace(standardDirPath, "")
    }

    static String relativePath(File file, File dir) {
        return relativePath(file.absolutePath, dir.absolutePath)
    }
}
