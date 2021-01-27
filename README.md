# promotion-rule-live-update

<br>
This is used to test the whole process of promotion rule live update.
<br><br>
<b>Prepare:</b>
<br>
1. Upload rule/rule-set files into /promote corresponding path for format validation.
<br>
2. Push these rules files and update task main.yml on nos-ansible-playbooks gitlab.
<br><br>
<b>Build test in Jenkins:</b>
<br>
OCP4 Jenkins job: indy-promotion-rule-live-update-promote
<br><br>
<b>Pipeline steps:</b>
<br>
1. validate rule/rule-set format
<br>
2. sync ansible project
<br>
3. play ansible job template
<br>
4. load rule/rule-set
<br>
5. get rule/rule-set content
<br>
6. promote rule/rule-set and validate result
