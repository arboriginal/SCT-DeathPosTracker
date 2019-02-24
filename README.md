# SCT-DeathPosTracker

This is a tracker addon for [SimpleCompass](https://www.spigotmc.org/resources/simplecompass.63140/).
It allows to track the last death position.

## How to install

- Drop the [jar file](https://github.com/arboriginal/SCT-DeathPosTracker/releases) into your `plugins/SimpleCompass/trackers` folder
- Restart your server.

**BEWARE:** Apparently, this tracker can have issue with the plugin [SavageDeathCompass](https://dev.bukkit.org/projects/savagedeathcompass) (I'm not using it), resulting in duplicate bossbar compass when you die and receive its compass item. [the_happy_helper](https://www.spigotmc.org/members/the_happy_helper.650977/) explain [here](https://www.spigotmc.org/threads/simplecompass.351093/page-7#post-3320412):

> I managed to find a fix for the dupe problem with SavageDeathCompass, all I had to do was change "target-delay:" in SavageDeathCompass's config.yml to a higher value

## Configuration

Edit the file `plugins/SimpleCompass/trackers/DeathPosTracker.yml` (automatically created the first time the tracker is loaded).

Read [settings.yml](https://github.com/arboriginal/SCT-DeathPosTracker/blob/master/src/settings.yml) to have a look on available parameters.

## Permissions

- To use this tracker, players must have:
    - **scompass.use**
    - **scompass.track**
    - **scompass.track.DEATH_POSITION** (or **scompass.track.***)
