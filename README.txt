RewardsService

RewardsService code and test

Introduction

Thank you for downloading the sample Reward Service project

System Requirements & Installation

Java version: JDK 1.8
Spring Boot 1.5.2


Installation
1. Unzip the zip file system a location.
2. Create a sample workspace in Eclipse
3. In project explorer, Import the unziped project folder.
======================================================================================
TEST DATA 
Reward Service rewards mappings
SPORTS ---->  CHAMPIONS_LEAGUE_FINAL_TICKET
MUSIC  ---->  KARAOKE_PRO_MICROPHONE
MOVIES ---->  PIRATES_OF_THE_CARIBBEAN_COLLECTION
KIDS   ---->  N/A
NEWS   ---->  N/A


Eligibility Service test data and its responses
Account number  ---->  Response 
1111            ---->  CUSTOMER_ELIGIBLE
2222            ---->  CUSTOMER_INELIGIBLE
3333            ---->  InvalidAccountNumberException
4444            ---->  TechnicalFailureException


Test GET Requests

1. Valid customer account, eligible and subscriptions with rewards
http://localhost:8080/rewards/1111?channels=MUSIC,SPORTS,KIDS

2. Valid customer account, eligible and subscriptions with no rewards
http://localhost:8080/rewards/1111?channels=KIDS,NEWS

3. Valid customer account and customer not eligible for rewards 
http://localhost:8080/rewards/2222?channels=KIDS,NEWS

4. In valid customer account --> throws InvalidAccountNumberException
http://localhost:8080/rewards/3333?channels=MUSIC,SPORTS,KIDS
 
5. Valid customer account, But eligibility service is down --> throws TechnicalFailureException
http://localhost:8080/rewards/4444?channels=MUSIC,SPORTS,KIDS
 
 