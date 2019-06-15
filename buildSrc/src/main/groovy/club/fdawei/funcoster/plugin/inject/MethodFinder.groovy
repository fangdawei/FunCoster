package club.fdawei.funcoster.plugin.inject

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

/**
 * Created by david on 2019/06/13.
 */
class MethodFinder extends ClassVisitor {

    private String className

    MethodFinder(ClassVisitor classVisitor) {
        super(Opcodes.ASM7, classVisitor)
    }

    @Override
    void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.className = name
        super.visit(version, access, name, signature, superName, interfaces)
    }

    @Override
    MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        def mv = super.visitMethod(access, name, descriptor, signature, exceptions)
        return new MethodInjector(Opcodes.ASM7, mv, access, name, descriptor, className)
    }
}
