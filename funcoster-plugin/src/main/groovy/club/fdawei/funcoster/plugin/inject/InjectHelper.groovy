package club.fdawei.funcoster.plugin.inject


import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter

/**
 * Create by david on 2019/06/12.
 */
class InjectHelper {

    static void inject(File classFile, File dir) {
        def reader = new ClassReader(new FileInputStream(classFile))
        def writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS)
        def finder = new MethodFinder(writer)

        reader.accept(finder, ClassReader.SKIP_DEBUG | ClassReader.EXPAND_FRAMES)

        def fos = new FileOutputStream(classFile)
        fos.write(writer.toByteArray())
        fos.flush()
        fos.close()
    }
}
