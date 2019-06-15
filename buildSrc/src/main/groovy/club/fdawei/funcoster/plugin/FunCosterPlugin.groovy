package club.fdawei.funcoster.plugin

import club.fdawei.funcoster.plugin.config.CosterConfig
import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Create by david on 2019/06/12.
 */
class FunCosterPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.extensions.add("costerConfig", CosterConfig)

        def android = project.extensions.findByType(AppExtension)
        if (android != null) {
            android.registerTransform(new FunCosterTransform(project))
        }
    }
}
