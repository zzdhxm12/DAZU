// 젠킨스 파이프라인 플러그인을 호출하기 위한 블록
pipeline {
// 파이프라인을 실행하고 싶은 위치 정의
agent none
// gitlab의 소스를 jenkins 디렉토리로 내려받을 시
// skipDefaultCheckout(true)일 경우 내려받는 프로세스 skip
// skipDefaultCheckout(false)일 경우 gitlab 소스 체크
options { skipDefaultCheckout(true) }
// stage의 모음
stages {
// 실제 작업이 수행되는 블록
// 해당 stage 명으로 jenkins 화면에 표시된다
stage('Build and Test') {
// docker image에 명시된 image를 활용하여 steps 수행
agent {
docker {
image 'maven:3-alpine'
args '-v /root/.m2:/root/.m2'
}
}
options { skipDefaultCheckout(false) }
steps {
sh 'mvn -B -DskipTests -f ./Backend/pom.xml clean package'
}
}
stage('Docker build') {
agent any
steps {
sh 'docker build -t dazufront:latest /var/jenkins_home/workspace/test/Frontend/vue-front'
sh 'docker build -t dazuback:latest /var/jenkins_home/workspace/test/Backend'
}
}
stage('Docker run') {
        agent any
        steps {
        // 현재 동작중인 컨테이너 중 <front-image-name>의 이름을 가진
        // 컨테이너를 stop 한다
            sh 'docker ps -f name=dazufront -q \
            | xargs --no-run-if-empty docker container stop'
            // 현재 동작중인 컨테이너 중 <back-image-name>의 이름을 가진
            // 컨테이너를 stop 한다
            sh 'docker ps -f name=dazuback -q \
            | xargs --no-run-if-empty docker container stop'
            // <front-image-name>의 이름을 가진 컨테이너를 삭제한다.
            sh 'docker container ls -a -f name=dazufront -q \
            | xargs -r docker container rm'
            // <back-image-name>의 이름을 가진 컨테이너를 삭제한다.
            sh 'docker container ls -a -f name=dazuback -q \
            | xargs -r docker container rm'
            // docker image build 시 기존에 존재하던 이미지는
            // dangling 상태가 되기 때문에 이미지를 일괄 삭제
            sh 'docker images -f dangling=true && \
            docker rmi $(docker images -f "dangling=true" -q)'
            sh 'docker run -d --name dazufront \
            -p 80:80 \
            -p 443:443 \
            -v /home/ubuntu/sslkey/:/var/jenkins_home/workspace/test/sslkey/ \
            --network dazunet \
            dazufront:latest'
            sh 'docker run -d --name dazuback \
            --network dazunet \
            dazuback:latest'
            }
        }
    }
}
