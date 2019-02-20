# SCT-DeathPosTracker

This is a tracker addon for [SimpleCompass](https://www.spigotmc.org/resources/simplecompass.63140/).
It allows to track the last death position.

## How to install

- Drop the [jar file](https://github.com/arboriginal/SCT-DeathPosTracker/releases) into your `plugins/SimpleCompass/trackers` folder
- Restart your server.

**BEWARE:** The plugin [SavageDeathCompass](https://dev.bukkit.org/projects/savagedeathcompass) seems not to be compatible with this tracker, resulting to duplicate bossbar compass when you die and receive its compass item. This will be fixed in a future version maybe, but as I don't use this plugin and have no time, for now it's not.

## Configuration

Edit the file `plugins/SimpleCompass/trackers/DeathPosTracker.yml` (automatically created the first time the tracker is loaded).

Read [settings.yml](https://github.com/arboriginal/SCT-DeathPosTracker/blob/master/src/settings.yml) to have a look on available parameters.

## Permissions

- To use this tracker, players must have:
    - **scompass.use**
    - **scompass.track**
    - **scompass.track.DEATH_POSITION** (or **scompass.track.***)
