package me.border.crates.listeners;

import me.border.crates.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ChestListener implements Listener {

    private Main plugin;

    public ChestListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e){
        String title = e.getInventory().getTitle();
        if (title.equals(ChatColor.GREEN + "Crate!")){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onClickChest(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Block b = e.getClickedBlock();

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (b.getType() == Material.CHEST) {
                if (p.getItemInHand().getType() == Material.TRIPWIRE_HOOK && p.getItemInHand().getItemMeta().getDisplayName().equals("key!")) {
                    Chest chest = (Chest) b.getState();
                    if (chest.getInventory().getTitle().equals("Crate!")) {
                        if (!p.isOp()) {
                            p.setOp(true);
                            Bukkit.dispatchCommand(p, "crate");
                            e.setCancelled(true);
                            p.setOp(false);
                        } else {
                            Bukkit.dispatchCommand(p, "crate");
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}
