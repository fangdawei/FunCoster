package club.fdawei.funcoster.plugin.inject

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Type
import org.objectweb.asm.commons.AdviceAdapter

/**
 * Created by david on 2019/06/13.
 */
class MethodInjector extends AdviceAdapter {

    private String className
    private String methodName
    private int localMethodEnterTime

    protected MethodInjector(int api, MethodVisitor mv, int access, String name, String desc, String className) {
        super(api, mv, access, name, desc)
        this.className = className
        this.methodName = name
    }

    @Override
    protected void onMethodEnter() {
        localMethodEnterTime = newLocal(Type.LONG_TYPE)
        visitMethodInsn(INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false)
        visitVarInsn(LSTORE, localMethodEnterTime)
    }

    @Override
    protected void onMethodExit(int opcode) {
        visitLdcInsn(String.format("%s#%s", className, methodName))
        visitMethodInsn(INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false)
        visitVarInsn(LLOAD, localMethodEnterTime)
        visitInsn(LSUB)
        visitMethodInsn(INVOKESTATIC, "club/fdawei/funcoster/api/FunCoster", "onFunCall",
                "(Ljava/lang/String;J)V", false)
    }
}
