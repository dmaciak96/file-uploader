# File Uploader

## Build Instruction
1. Go to root project directory: ```cd <ROOT_PROJECT_DIRECTORY>```
2. Build project: ```mvnw clean package```
3. Run application: ```java -jar target\file-uploader-0.0.1-SNAPSHOT.jar```
## How to test
1. Run Postman,
2. Select the POST request,
3. Enter "http://localhost:8080/metadata" as the URL,
4. Select "form-data" as the Body type,
5. Enter "files" as the Key,
6. Select "File" as the Value type,
7. Choose one or more files to upload.