# NeonTD
A challenging 2D Tower Defense game made in the Slick2D engine.
You can find a prebuilt binary, ready to play, in the **release** section!

## Introduction
This game was done as a project in the second semester of "Visual Computing and Design"
at the Hochschule Hamm-Lippstadt. It is programmed using Java and the Slick2D game engine.
All the assets and sprites were done in Photoshop and the maps were designed using the TileD tilemap editor.

## Features
* **3 Maps** (although it's simple to add your own!)
* **4 Towers**
 	* **The Circle Tower:** Your standard, mediocre speed/damage/range tower. Attacks both ground and air targets
 	* **The Hexagon Tower:** More range, more damage but slower. Attacks ground targets only
 	* **The Pentagon Tower:** Really fast, standard range and a little less damage. Attacks air targets only
 	* **The Gear Tower:** Doesn't do any damage but slows down ground enemies quite a bit
* **2 Types of enemies**
 	* **Ground enemies:** Slower, with a bit more health
 	* **Air enemies:** A bit faster, and less health
* **10 Waves per map** (also adjustable for both original and custom maps)
* **Boss enemies:** Ground enemies, with quite a bit more health, and really challenging. By default, they appear every five 
waves (again, customizable).

The game by default is balanced in a way that it is pretty challenging. Cash is rare, the upgrades aren't cheap 
(but worth their price!) and you have to play with a good strategy to win this ;)

It also features a path-finding algorithm, so as long there is at least one free path, you can build anywhere 
you want (there's also a special maze map for all you maze TD lovers).

## Customization
As you may have seen, the game is customizable in many ways. At the first start, the game will create a folder 
called **/res** next to its executable. There you can find all the resource files, both images and maps, and either 
change the appearance of the game, change the balancing of the maps or add completely new maps as well!

While the images subfolder should be self-explaining, I'll go into more detail for how to modify/add maps to the 
game. In the **/res/maps** folder, you'll find the subfolders for each map that is shipped with the game.
Each map folder has the following files in it:
* **map.tmx:** This is the map file created by the TileD map editor. Open up any of the map files to see how 
they work.
* **preview.png:** This is a preview image for the map selection screen.
* **properties.xml:** This is where all the configuration happens.

While the path (all tiles that are accessible by enemies) is defined by the map.tmx file,
the spawn points (one or multiple) and the base (where the enemies move towards) are to be defined in the 
properties.xml file, among other things. I'll explain the file contents itself in the following section.

### properties.xml customization
The properties.xml file is, as you may have guessed, written in xml syntax.
There's always one, and only one root element called **\<Map>**, which contains all the properties of this map:
* **\<Name>:** The name of the map
* **\<StartMoney>:** The amount of money the player starts with in wave 1
* **\<MoneyPerKill>:** The amount of money the player gains for each kill
* **\<MoneyPerWave>:** The amount of money the player gains for each wave. You'll also get this if you call the 
next wave earlier, but you'll have to deal with the increase of enemies ;)
* **\<BaseHealthGround>:** This is the amount of health points a ground enemy has at wave 1
* **\<BaseHealthAir>:** Same thing for air enemies
* **\<BaseHealthBoss>:** Again, same thing for bosses
* **\<HealthMultiplier>:** This is a factor for the enemies' total health point increase by every wave. The 
formular for an enemy's health is as follows: hp = <BaseHealth> + wave * <HealthMultiplier>
* **\<Spawns>:** This is an array of spawn points, defined as follows
 	* **\<Spawn>:** A single spawn point entry
  		* **\<PosX>:** The x-axis coordinate for the spawn point
  		* **\<PosY>:** The y-axis coordinate for said spawn point
* **\<Base>:** The base coordinates work in pretty much the same way. Note that there can be only one base!
 	* **\<PosX>:** The x-axis coordinate of the base
 	* **\<PosY>:** The y-axis coordinate of said base
* **\<Waves>:** This is the list of waves, where each wave has entries for how much enemies of each type are 
going to spawn in that wave. Order in this list is important, the waves are read from top to bottom.
 	* **\<Wave>:** A single entry for one wave
  		* **\<Ground>:** A number that defines how many ground enemies are being spawned in this wave
  		* **\<Air>:** Same thing for air enemies
  		* **\<Boss>:** Again, same thing for bosses

And this is pretty much it! Balancing a map takes quite a while, as there are many important factors that play 
into this, such as all the money modifiers, health modifiers and of course the enemy numbers, enemy type and how 
the map is actually structured. Just play around with it for a while, change a parameter that seems to hurt 
balancing, and play again. Beware, it's addictive ;)
