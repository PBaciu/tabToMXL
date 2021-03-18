# TAB2MXL

# Introduction
TAB2MXL is a java application which will convert tablature made for percussion, guitar, and bass guitar from a “.txt” format to a ".musicxml" format.

The specific xml format being used is musicxml and there are various resources on the web that will allow conversion from the resultant musicxml file into sheet music.

# Functionality
Currently, our application supports input in the form of plain text (pasted into a text area), or the selection of a .txt file from the user's file system.

# Requirements
TAB2MXL uses Java 15.0.2, so please ensure that version 15.0.2 of java is installed on your machine prior to setup.

# Supported Features
We support the conversion of simple guitar tabs. These tabs must not contain additional information such as title, repeats, and text between lines of the tab.
We currently support chords.
We currently only support 6 string guitars in standard tuning.
Hammerons and pulloffs are only supported outside of chords and in pairs of two.
These features are currently being added.

Two example tabs are shown below.

```
e|-------5-7-----7-|-8-----8-2-----2-|-0---------0-----|-----------------|
B|-----5-----5-----|---5-------3-----|---1---1-----1---|-0-1-1-----------|
G|---5---------5---|-----5-------2---|-----2---------2-|-0-2-2---2-------|
D|-7-------6-------|-5-------4-------|-3---------------|-----------------|
A|-----------------|-----------------|-----------------|-2-0-0---0---8-7-|
E|-----------------|-----------------|-----------------|-----------------|
 
e|---------7-----7-|-8-----8-2-----2-|-0---------0-----|-----------------|
B|-------5---5-----|---5-------3-----|---1---1-----1---|-0-1-1-----------|
G|-----5-------5---|-----5-------2---|-----2---------2-|-0-2-2-----------|
D|---7-----6-------|-5-------4-------|-3---------------|-----------------|
A|-0---------------|-----------------|-----------------|-2-0-0-------0-2-|
E|-----------------|-----------------|-----------------|-----------------|
```

```
|-----------0-----|-0---------------|
|---------0---0---|-0---------------|
|-------1-------1-|-1---------------|
|-----2-----------|-2---------------|
|---2-------------|-2---------------|
|-0---------------|-0---------------|
```

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
  
  1. The Package Explorer should now appear on the left hand side of the window.<br /> 
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

# Usage
Our application currently consists of three pages
  
  #### Input Page
  This page allows the insertion of text via a .txt file that can be selected from the file system, or copied text. Incase of the user already having the file open,   the user could also drag and drop the file into the designated space. <br />
  <img width="897" alt="Screen Shot 2021-03-17 at 11 22 51 PM" src="https://user-images.githubusercontent.com/77417270/111568893-fa972700-8777-11eb-8746-b4c25bfb6698.png">

  
   If the user made changes right after the user inserted the .txt file from the file explorer. The user would be notified with a message saying that changes has been made and if you would like to save those changes, where you can click the save changes button.
  ![image](https://user-images.githubusercontent.com/42554604/111553100-0f64c200-875a-11eb-9894-b8a427270c94.JPG)<br /><br />
  
  #### Output Page
  This page allows the user to view the resulting xml and gives the option to save the file, convert another file, or go back to the previously inputted file.<br />
  ![image](https://user-images.githubusercontent.com/77293069/109452346-8b060580-7a1d-11eb-979a-ed912778f244.png)<br /><br />

# Issues
In case of any problems, please open an issue on this github page, or email Patrick at baciupat@my.yorku.ca
