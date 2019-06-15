package club.fdawei.funcoster.plugin.util

import com.android.utils.FileUtils

/**
 * Created by david on 2019/06/14.
 */
class ClassUtils {

    static String getNameFromFile(File classFile, File dir) {
        def relativePath = FileUtils.relativePath(classFile, dir)
        char point = '.'
        def className = relativePath.replace(File.separatorChar, point)
        return className
    }
}
