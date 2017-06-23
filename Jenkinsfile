pipeline {
  agent any
  stages {
    stage('Compile') {
      steps {
        parallel(
          "Compile": {
            tool(name: 'MAVEN_3.5', type: 'maven')
            tool(name: 'JDK_1.8', type: 'jdk')
            
          },
          "Step 1": {
            sh 'mvn --version'
            echo 'Step 1'
            
          }
        )
      }
    }
  }
}