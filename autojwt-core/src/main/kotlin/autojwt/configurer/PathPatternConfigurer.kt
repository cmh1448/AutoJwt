package autojwt.configurer

class PathPatternConfigurer {
    internal val includePatternList = mutableListOf<String>()
    internal val excludePatternList = mutableListOf<String>()

    fun includePattern(path: String) {
        includePatternList.add(path)
    }

    fun includeAll() {
        includePatternList.add("/**")
    }

    fun excludePattern(path: String) {
        excludePatternList.add(path)
    }
}