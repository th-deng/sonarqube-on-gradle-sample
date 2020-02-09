# sonarqube-on-gradle-sample

The sample project of [Gradle 프로젝트에 JaCoCo 설정하기](http://woowabros.github.io/experience/2020/02/02/jacoco-config-on-gradle-project.html)
from [우아한 형제들 기술 블로그](http://woowabros.github.io/)

# Run sonaqube

프로젝트에서 [필요한 플러그인이 포함된 sonarqube](./sonarqube) 실행

```bash
$ cd ./sonarqube
$ ./sonarqube.sh
```


## Included sonarqube plugins

- [https://docs.sonarqube.org/latest/analysis/languages/kotlin/](Kotlin)
- [https://docs.sonarqube.org/latest/analysis/languages/java/](Java)
- [https://docs.sonarqube.org/display/PLUG/JaCoCo+Plugin](JaCoCo Plugin)


# Run tests and generate JaCoCo report

`test` task를 실행하면 `jacocoTestReport` task가 따라서 실행된다.

```bash
$ ./gradlew clean test
$ ./gradlew clean test jacocoTestReport jacocoTestCoverageVerification
```
