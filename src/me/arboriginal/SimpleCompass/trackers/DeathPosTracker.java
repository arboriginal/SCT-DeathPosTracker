package me.arboriginal.SimpleCompass.trackers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import com.google.common.collect.ImmutableMap;
import me.arboriginal.SimpleCompass.plugin.AbstractTracker;
import me.arboriginal.SimpleCompass.plugin.SimpleCompass;
import me.arboriginal.SimpleCompass.utils.CacheUtil;

public class DeathPosTracker extends AbstractTracker implements Listener {
  // ----------------------------------------------------------------------------------------------
  // Constructor methods
  // ----------------------------------------------------------------------------------------------

  public DeathPosTracker(SimpleCompass plugin) {
    super(plugin);
    sc.getServer().getPluginManager().registerEvents(this, sc);
  }

  // ----------------------------------------------------------------------------------------------
  // Tracker methods
  // ----------------------------------------------------------------------------------------------

  @Override
  public String github() {
    return "arboriginal/SCT-DeathPosTracker";
  }

  @Override
  public String trackerID() {
    return "DEATH_POSITION";
  }

  @Override
  public String version() {
    return "9";
  }

  // ----------------------------------------------------------------------------------------------
  // Listener methods
  // ----------------------------------------------------------------------------------------------

  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent event) {
    Player player = event.getEntity();
    if (!hasPermission(player)) return;
    set(player, "" + (CacheUtil.now() + settings.getInt("settings.keep_position") * 1000),
        new double[] { player.getLocation().getX(), player.getLocation().getZ() });
  }

  @EventHandler
  public void onPlayerRespawn(PlayerRespawnEvent event) {
    if (!settings.getBoolean("settings.auto_activated")) return;
    Player player = event.getPlayer();
    if (!hasPermission(player)) return;
    String target = lastDeath(player);
    if (target == null) return;
    activate(player, "" + target, false);
  }

  // ----------------------------------------------------------------------------------------------
  // Actions methods
  // ----------------------------------------------------------------------------------------------

  @Override
  public List<TrackingActions> getActionsAvailable(Player player, boolean keepUnavailable) {
    List<TrackingActions> list = super.getActionsAvailable(player, keepUnavailable);

    if (keepUnavailable || lastDeath(player) != null) {
      list.add(TrackingActions.START);
      list.add(TrackingActions.STOP);
    }

    return list;
  }

  @Override
  public TargetSelector requireTarget(TrackingActions action) {
    if (action.equals(TrackingActions.START) || action.equals(TrackingActions.STOP))
      return TargetSelector.NONE;

    return super.requireTarget(action);
  }

  // ----------------------------------------------------------------------------------------------
  // Targets methods
  // ----------------------------------------------------------------------------------------------

  @Override
  public List<String> autoloadTargets(Player player, String startWith) {
    return new ArrayList<String>();
  }

  @Override
  public double[] get(Player player, String name) {
    double[] coords = super.get(player, name);

    if (coords != null)
      try {
        long until = Long.parseLong(name);
        if (CacheUtil.now() > until) {
          sendMessage(player, "target_expired");
          return null;
        }
      }
      catch (Exception e) {
        return null;
      }

    return coords;
  }

  @Override
  public List<String> list(Player player, TrackingActions action, String startWith) {
    List<String> list = new ArrayList<String>();

    for (String name : super.list(player, action, startWith)) {
      double[] coords = get(player, name);
      if (coords == null) continue;
      list.add(prepareMessage("list_coord", ImmutableMap.of("x", "" + (int) coords[0], "z", "" + (int) coords[1])));
    }

    return list;
  }

  @Override
  public boolean set(Player player, String name, double[] coords) {
    if (super.set(player, name, coords)) {
      List<String> list = sc.datas.activeTargetsList(player, trackerID());
      list.addAll(availableTargets(player, ""));

      if (!list.isEmpty()) list.forEach(target -> {
        if (!target.equals(name)) del(player, target);
      });

      return true;
    }

    return false;
  }

  // ----------------------------------------------------------------------------------------------
  // Command methods
  // ----------------------------------------------------------------------------------------------

  @Override
  public List<String> commandSuggestions(Player player, String[] args, HashMap<String, Object> parsed) {
    if (args.length > 2) return null;
    return super.commandSuggestions(player, args, parsed);
  }

  @Override
  public boolean perform(Player player, String command, TrackingActions action, String target, String[] args) {
    if (args.length != 2 || target != null) return false;
    target = lastDeath(player);

    if (target == null) {
      sendMessage(player, "target_not_found", ImmutableMap.of("target", trackerID()));
      return true;
    }

    switch (action) {
      case START:
        activate(player, target, false);
        sendMessage(player, "START");
        break;

      case STOP:
        disable(player, target);
        sendMessage(player, "STOP");
        break;

      default:
        return false;
    }

    return true;
  }

  // ----------------------------------------------------------------------------------------------
  // Specific methods
  // ----------------------------------------------------------------------------------------------

  private boolean hasPermission(Player player) {
    return player.hasPermission("scompass.track.DEATH_POSITION") || player.hasPermission("scompass.track.*");
  }

  private String lastDeath(Player player) {
    for (String name : super.list(player, null, "")) if (get(player, name) != null) return name;
    return null;
  }
}
