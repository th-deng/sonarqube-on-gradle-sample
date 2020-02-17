import jenkins.model.*

pipeline {
    agent any
    stages {
        stage('SCM') {
            steps {
                git 'https://github.com/th-deng/sonarqube-on-gradle-sample'
            }
        }
        stage('SonarQube analysis') {
            steps {
                withSonarQubeEnv('sonarqube') { // Will pick the global server connection you have configured
                    sh './gradlew clean check sonarqube'
                }
            }
        }
        stage('Jacoco Report') {
            steps {
                jacoco execPattern: '**/build/jacoco/*.exec',
                        classPattern: '**/build/classes/kotlin,**/build/classes/java',
                        inclusionPattern: '**/*.class',
                        exclusionPattern: '**/test/**,**/integration-test/**,**/*Test.class,**/Q*.class',
                        sourcePattern: '**/src/main',
                        sourceInclusionPattern: '**/*.kt,**/*.java',
                        changeBuildStatus: true,
                        maximumBranchCoverage: params.MINIMUM_BRANCH_COVERAGE,
                        minimumBranchCoverage: params.MINIMUM_BRANCH_COVERAGE,
                        maximumLineCoverage: params.MINIMUM_LINE_COVERAGE,
                        minimumLineCoverage: params.MINIMUM_LINE_COVERAGE
            }
        }
        stage("Quality Gate"){
            steps {
                timeout(time: 1, unit: 'HOURS') { // Just in case something goes wrong, pipeline will be killed after a timeout
                    script {
                        def qg = waitForQualityGate() // Reuse taskId previously collected by withSonarQubeEnv
                        if (qg.status != 'OK') {
                            error "Pipeline aborted due to quality gate failure: ${qg.status}"
                        }
                    }
                }
            }
        }
    }
}
