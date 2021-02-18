# tabToMXL

# Introduction
tabToMXL is a java application which will convert tablature made for percussion, guitar, and bass guitar from a “.txt” format to a ".xml" format.

The specific xml format being used is musicxml and there are various resources on the web that will allow conversion from the resultant musicxml file into sheet music.

# Functionality
Currently, our application supports input in the form of plain text (pasted into a text area), or the selection of a .txt file from the user's file system.
The parser is a work in progress and no matter what input tab is submitted, one predetermined output will be generated.

# Requirements
tabToMXL uses Java 15.0.2 
Please ensure that version 15.0.2 of java is installed on your machine prior to setup.

# Setup
Outlined below are instructions for setting up the project in Eclipse.

  ### Eclipse
  
  #### Downloading the project via zip file
  1. Download the zip file from this repository.<br />
  ![image](https://user-images.githubusercontent.com/77293069/108298248-a4c86280-716a-11eb-8faa-70209c86a642.png)<br /><br />
  2. Unzip the file using some file extractor software.<br />
  ![image](https://user-images.githubusercontent.com/77293069/108298427-f113a280-716a-11eb-9f4f-caa21bf848c2.png)<br /><br />
  3. File -> Open Projects From File System.<br /> 
  ![image](https://user-images.githubusercontent.com/77293069/108298618-4354c380-716b-11eb-84c4-ec6438cbe6fe.png)<br /><br />
  4. Select 'Directory' and navigate to the TabToMXL folder.<br />                      
  ![image](https://user-images.githubusercontent.com/77293069/108298911-bb22ee00-716b-11eb-91c3-a57dc2a175c8.png)<br />
    Click 'Select Folder' and then click 'Finish'.<br /><br />                             
  
  #### Downloading the project via git
  1. Right click on Package Explorer section in Eclipse, select Import -> Projects from Git. <br />
  ![image](https://user-images.githubusercontent.com/77293069/108302384-e27cb980-7171-11eb-8fa3-8c4b1ca74c24.png)<br /><br />
  2. Click Next -> Clone URI -> Next and paste this link in the URI section: https://github.com/PBaciu/tabToMXL.git  <br />
  ![image](https://user-images.githubusercontent.com/77293069/108302639-5dde6b00-7172-11eb-9b4a-5a69fa55e6c3.png)<br /><br />
  3. Click 'Next' then unckeck the 'Develop' branch and then click 'Next'. Leave the next page as defaults and click Next. Then hit 'Finish'. <br />

  #### Running the project
  
  1. You should now see the Package Explorer on the left hand side of the window.<br /> 
  ![image](https://user-images.githubusercontent.com/77293069/108299292-2b317400-716c-11eb-89c3-d4977be688ed.png)<br /><br />
  2. Right click on the project 'TabToMXL' and navigate to Gradle -> Refresh Gradle Project. <br />
  ![image](https://user-images.githubusercontent.com/77293069/108299368-4dc38d00-716c-11eb-871c-232e43ad03ff.png)<br /><br />
  3. Right click on the project 'TabToMXL' and navigate to Run As -> Run Configurations. <br /><br />
  ![image](https://user-images.githubusercontent.com/77293069/108300067-a47d9680-716d-11eb-8ee7-055eb8ed2e9f.png)<br /><br />
  4. Select Gradle Task and then click the icon circled in red to create a new configuration.<br />
  ![image](https://user-images.githubusercontent.com/77293069/108300326-1f46b180-716e-11eb-8925-7008c3550c0e.png)<br /><br />
  5. Click Add and rename the configuration from 'task' to 'run', by clicking on the name. Then, select 'Workspace' and then choose 'TabToMXL'. Apply and Run.<br />
  ![image](https://user-images.githubusercontent.com/77293069/108300509-6fbe0f00-716e-11eb-9127-87d455b108d6.png)<br /><br />
  6. TabToMXL is now set up to run on your system. You can run the project in the future by clicking the run button on the top bar menu.<br />
  ![image](https://user-images.githubusercontent.com/77293069/108300643-b3b11400-716e-11eb-9086-c8810172aa1f.png)<br /><br />
 
# Issues
In case of issues, please open an Issue on the git page, or email Patrick at baciupat@my.yorku.ca
