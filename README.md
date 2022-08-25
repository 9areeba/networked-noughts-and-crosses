# Noughts and Crosses
A noughts and crosses game which can run multiple games simultaneously, through the use of java sockets.

## How to run in IntelliJ
1. Create a new project in IntelliJ and set the root directory to the directory into which you have cloned this repo.

2. Download javfx 17, if not already installed, from https://gluonhq.com/products/javafx/ and unzip the folder in some directory.

3. Go to Project Structure > Modules > Dependencies and Add JARS by clicking the plus button. Here add all the JAR files found in the 'lib' directroy of your Javafx folder.

4. Modify run configurations of 'ClientSide/PlayerMain.java' and the following VM options: <br> <br>
--module-path /path-to-sdk-in-ur-comp/javafx-sdk-17.0.2/lib --add-modules javafx.controls,javafx.fxml <br> <br>
Make sure the 'path-to-sdk-in-your-comp' points to the folder where you unzipped your Javafx distribution and the javafx version matches

5. Also check 'Allow multiple instances' of the PlayerMain run configuration.

6. Finally to play, start 'ServerSide/PlayerServer.java' first and then run at least 2 instnaces of 'ClientSide/PlayerMain.java'. (Can run more instances to play multiple games)
