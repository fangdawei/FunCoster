package club.fdawei.funcoster.plugin

import club.fdawei.funcoster.plugin.config.CosterConfig
import club.fdawei.funcoster.plugin.inject.InjectHelper
import club.fdawei.funcoster.plugin.util.ClassUtils
import club.fdawei.funcoster.plugin.util.JarUtils
import com.android.build.api.transform.*
import com.android.utils.FileUtils
import org.apache.commons.codec.digest.DigestUtils

/**
 * Create by david on 2019/06/12.
 */
class TransformHandler {

    TransformInvocation invocation
    CosterConfig config = null

    private TransformHandler(TransformInvocation invocation) {
        this.invocation = invocation
    }

    void handle() {
        if (!invocation.incremental) {
            invocation.outputProvider.deleteAll()
        }
        invocation.inputs.each { input ->
            input.directoryInputs.each { dir ->
                handleDir(dir)
            }

            input.jarInputs.each { jar ->
                handleJar(jar)
            }
        }
    }

    private void handleDir(DirectoryInput dir) {
        def destDir = invocation.outputProvider.getContentLocation(
                dir.name, dir.contentTypes, dir.scopes, Format.DIRECTORY)
        if (invocation.incremental) {
            dir.changedFiles.each {
                def destFile = new File(it.key.absolutePath.replace(dir.file.absolutePath, destDir.absolutePath))
                switch (it.value) {
                    case Status.REMOVED:
                        FileUtils.deleteIfExists(destFile)
                        break
                    case Status.ADDED:
                    case Status.CHANGED:
                        inject(it.key, dir.file)
                        FileUtils.copyFile(it.key, destFile)
                        break
                    case Status.NOTCHANGED:
                        break
                }
            }
        } else {
            dir.file.eachFileRecurse {
                if (!it.directory && it.name.endsWith('.class')) {
                    inject(it, dir.file)
                }
            }
            FileUtils.copyDirectory(dir.file, destDir)
        }
    }

    private void handleJar(JarInput jar) {
        def destJarName = DigestUtils.md5Hex(jar.file.absolutePath).concat('.jar')
        def destFile = invocation.outputProvider.getContentLocation(
                destJarName, jar.contentTypes, jar.scopes, Format.JAR)
        if (invocation.incremental) {
            switch (jar.status) {
                case Status.REMOVED:
                    FileUtils.deleteIfExists(destFile)
                    break
                case Status.ADDED:
                case Status.CHANGED:
                    handleJarInner(jar, destFile)
                    break
                case Status.NOTCHANGED:
                    break
            }
        } else {
            handleJarInner(jar, destFile)
        }
    }

    private void handleJarInner(JarInput jar, File destFile) {
        def tmpDir = new File(invocation.context.temporaryDir,
                DigestUtils.md5Hex(jar.file.absolutePath))
        JarUtils.unzipJarFile(jar.file.absolutePath, tmpDir.absolutePath)
        tmpDir.eachFileRecurse {
            if (!it.directory && it.name.endsWith('.class')) {
                inject(it, tmpDir)
            }
        }
        JarUtils.zipJarFile(tmpDir.absolutePath, destFile.absolutePath)
    }

    private void inject(File classFile, File dir) {
        if (config == null) {
            return
        }
        if (!config.enable) {
            return
        }
        def className = ClassUtils.getNameFromFile(classFile, dir)
        if (config.includeConfig.isMatch(className) && config.excludeConfig.isNotMatch(className)) {
            System.out.println("hook ${className}")
            InjectHelper.inject(classFile, dir)
        }
    }

    static TransformHandler of(TransformInvocation invocation) {
        return new TransformHandler(invocation)
    }
}
