package club.fdawei.funcoster.plugin.config

import java.util.regex.Pattern

/**
 * Created by david on 2019/06/14.
 */
class NameConfig {

    Collection<CharSequence> prefixes = new LinkedList<>()
    Collection<CharSequence> suffixes = new LinkedList<>()
    Collection<String> regexes = new LinkedList<>()

    void prefix(CharSequence namePrefix) {
        prefixes.add(namePrefix)
    }

    void suffix(CharSequence nameSuffix) {
        suffixes.add(nameSuffix)
    }

    void regex(String regexString) {
        regexes.add(regexString)
    }

    boolean isMatch(CharSequence name) {
        if (name == null || name.empty) {
            return false
        }
        for (prefix in prefixes) {
            if (name.startsWith(prefix)) {
                return true
            }
        }
        for (suffix in suffixes) {
            if (name.endsWith(suffix)) {
                return true
            }
        }
        for (regex in regexes) {
            if (Pattern.matches(regex, name)) {
                return true
            }
        }
        return false
    }

    boolean isNotMatch(CharSequence name) {
        !isMatch(name)
    }
}