def call(body) {
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()
    
    Branch = sh(script: echo "${config.projectName}")
    return Branch
}
