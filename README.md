# Fight
The official plugin, gamemode ran by TGM. Original - PGM, creators of TGM: Teejers, MatrixTunnel, Luuke.

## Local Server Setup
 
1. Start with the latest [Spigot](https://www.spigotmc.org/) build. 
 
2. Create a `maps` folder inside of the server and insert a supported TGM map. Use the repository `Maps` folder for some packaged example maps. 
 
3. Create a file named `rotation` in the server folder. This is a list of maps that the plugin will automatically cycle to. Put any of your maps on their own line in the file. If you are using Fracture, simply put "Fracture" on the first line and save the file. It's important to know that the name of the map specified in the map.json file is used here, not the folder name.
 
4. (Optional) Install World Edit to make the Teleport Tool work.  
 
5. Start the server.
 
## Developer Tips

1. We use Lombok. Make sure you have the Lombok plugin installed on your preferred IDE.

2. We use maven. Like any other maven project, run `mvn clean install` in the top level folder to generated required libraries.

