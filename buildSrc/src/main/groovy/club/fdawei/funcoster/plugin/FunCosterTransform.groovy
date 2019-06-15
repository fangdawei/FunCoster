package club.fdawei.funcoster.plugin

import club.fdawei.funcoster.plugin.config.CosterConfig
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import org.gradle.api.Project

/**
 * Created by david on 2019/06/12.
 */
class FunCosterTransform extends Transform {

    private Project project

    FunCosterTransform(Project project) {
        this.project = project
    }

    @Override
    String getName() {
        return "FunCoster"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_IR_FOR_SLICING
    }

    @Override
    boolean isIncremental() {
        return true
    }

    @Override
    void transform(TransformInvocation transformInvocation)
            throws TransformException, InterruptedException, IOException {
        def handler = TransformHandler.of(transformInvocation)
        handler.config = getConfig()
        handler.handle()
    }

    private CosterConfig getConfig() {
        def config = project.extensions.findByType(CosterConfig)
        return config != null ? config : new CosterConfig()
    }
}
