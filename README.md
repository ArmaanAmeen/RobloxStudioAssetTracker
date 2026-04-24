# Roblox Studio Project Asset Tracker

## Project Description

The Roblox Studio Project Asset Tracker is a Java desktop application made for Roblox developers who need to organize assets used in their games. Roblox Studio projects can contain many weapons, maps, animations, sounds, VFX, UI elements, scripts, skins, and wraps. This application helps developers track those assets in one place.

Users can add new assets, edit asset information, delete assets, search by name, filter by asset type, filter by status, update progress, add notes, and export the asset list to a CSV file.

## Features

- Add Roblox Studio assets
- View all saved assets in a table
- Edit selected assets
- Delete selected assets
- Search assets by name
- Filter assets by asset type
- Filter assets by status
- Track Roblox Asset ID or Studio path
- Mark assets as Finished, In Progress, Needs Fixing, or Not Started
- Add notes for each asset
- Save assets automatically to a local CSV file
- Export the asset list to a separate CSV file
- Dashboard summary showing total assets and asset status counts

## Asset Types

- Weapon
- Skin
- Wrap
- Animation
- Sound
- Map
- VFX
- UI
- Script
- Other

## Status Options

- Finished
- In Progress
- Needs Fixing
- Not Started

## How to Run

Open a terminal in the project folder and run:

javac -d out main/*.java

java -cp out main


## Use Cases

1. User can add a new asset.
2. User can view all assets.
3. User can edit an existing asset.
4. User can delete an asset.
5. User can search for an asset by name.
6. User can filter assets by type.
7. User can filter assets by status.
8. User can mark an asset as Finished, In Progress, Needs Fixing, or Not Started.
9. User can add notes to an asset.
10. User can export the asset list.

## Example Asset


Name: Greatsword
Type: Weapon
Roblox Asset ID: N/A
Studio Path: ReplicatedStorage.Shared.ToolFolder.Primary.Greatsword
Status: In Progress


## Future Improvements

- Add login system
- Add asset images
- Add direct Roblox API support
- Add project folders
- Add charts for asset progress w/ chart color coding from red to green depending on % complete
- Add due dates and color coding for unfinished assets
- Assign team members to certain assets
