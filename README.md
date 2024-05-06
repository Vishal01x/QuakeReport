This app is used for fetching the earthquake data using Http connection via JSON parsing
In the first phase I have made listview and customise adapter as per the data representation requires by creating a CustomText class 
Firstly I have taken the string of dummy data in JSON thereafter use the JSON parsing for fetching the required Info.
JSON parsing -> Tree like structure, that can stores both primitive and non primitive data type. In json we access any data in form of string 
or its real datatype based on our needs
if we get '{' it means we have a json object
if we get '[' it means an json array can contains a json object, string, again a json array
