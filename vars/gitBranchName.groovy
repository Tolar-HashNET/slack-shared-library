def call(body) {
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()
    
    string Branch = sh(returnStdout: true, script: "${config.BranchName}")
    return Branch
}
