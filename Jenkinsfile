pipeline {
    agent any
    stages {
        stage('Rule Format Test'){
            agent { label 'maven' }
            steps {
                script {
                    try {
                        sh script: "mvn clean install"
                        sh script: "java -jar target/promotion-rule-live-update-1.0-SNAPSHOT.jar"
                    }
                    catch (exc) {
                        error("Rule format test failed.")
                    }
                }
            }
        }
        stage("Ansible project sync") {
            steps {
                ansibleTowerProjectSync(
                towerServer: 'Red Hat Ansible Tower',
                project: 'NOS Automation',
                towerCredentialsId: 'ansible-nos-bot',
                removeColor: false,
                importTowerLogs: true,
                verbose: true
            )
            }
        }
        stage("Ansible job play") {
            steps {
                ansibleTower(
                towerServer: 'Red Hat Ansible Tower',
                towerCredentialsId: 'ansible-nos-bot',
                templateType: 'job',
                jobTemplate: "$PROMOTE_JOB_TEMPLATE",
                jobTags: "$PROMOTE_JOB_TAG",
                jobType: 'run',
                inventory: 'nos-inventory',
                credential: 'nos-vault-password',
                towerLogLevel: 'full',
                removeColor: false,
                verbose: true
            )
            }
        }
        stage("Rule load") {
            steps {
                script {
                    def url = "$INDY_API_URL/admin/promotion/validation/reload/all"
                    final String response = sh(script: "curl -u '$API_TEST_USER:$API_TEST_USER_PASSWORD' -X PUT $url", returnStdout: true).trim()

                    echo response
                    if(!response.contains("$RULE_FILE_NAME"))
                    {
                        error("No rule file: loaded.");
                    }
                    if(!response.contains("$RULE_SET_FILE_NAME"))
                    {
                        error("No rule-set file: loaded.");
                    }
                }
            }
        }
        stage("Rule get") {
            steps {
                script {
                    def ruleUrl = "$INDY_API_URL/admin/promotion/validation/rules/named/$RULE_FILE_NAME"
                    final String ruleResponse = sh(script: "curl --write-out '%{http_code}' --silent --output /dev/null $ruleUrl", returnStdout: true).trim()
                    if(ruleResponse != "200")
                    {
                        error("Fail to get rule content.");
                    }

                    def rulesetUrl = "$INDY_API_URL/admin/promotion/validation/rulesets/named/$RULE_SET_FILE_NAME"
                    final String rulesetResponse = sh(script: "curl --write-out '%{http_code}' --silent --output /dev/null $rulesetUrl", returnStdout: true).trim()
                    if(rulesetResponse != "200")
                    {
                        error("Fail to get rule set content.");
                    }
                }
            }
        }
        stage("Promote result") {
            steps {
                script {
                    def groupePromoteUrl = "$INDY_API_URL/promotion/groups/promote"
                    def promoteData = "{\"source\":\"$SOURCE_STORE_KEY\",\"target\":\"$TARGET_STORE_KEY\"}"
                    writeFile file: 'promote-data.json', text:promoteData
                    final String promoteResponse = sh(script: "curl -u \"$API_TEST_USER:$API_TEST_USER_PASSWORD\" -X POST $groupePromoteUrl -H \"accept: application/json\" -H \"Content-Type: application/json\" -T promote-data.json -vvvv", returnStdout: true).trim()
                    echo promoteResponse
                }
            }
        }
    }
}