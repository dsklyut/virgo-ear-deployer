pipeline {
  agent any
  stages {
    stage('Compile') {
      steps {
        tool(name: 'MAVEN_3.5', type: 'maven')
        tool(name: 'JDK_1.8', type: 'jdk')
      }
    }
  }
}