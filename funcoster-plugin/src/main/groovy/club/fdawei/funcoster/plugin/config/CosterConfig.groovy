package club.fdawei.funcoster.plugin.config

import org.gradle.api.Action

/**
 * Created by david on 2019/06/13.
 */
class CosterConfig {
    boolean enable = false
    NameConfig includeConfig = new NameConfig()
    NameConfig excludeConfig = new NameConfig()

    void include(Action<NameConfig> action) {
        action.execute(includeConfig)
    }

    void exclude(Action<NameConfig> action) {
        action.execute(excludeConfig)
    }
}
