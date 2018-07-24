def call(body) {
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()
    
    string Branch = config.BranchName
    return Branch
}
