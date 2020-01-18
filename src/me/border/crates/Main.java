package me.border.crates;

import me.border.crates.listeners.ChestListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class Main extends JavaPlugin {

    Map<UUID, Integer> crateUsesMap;
    Random random;

    String[] prizes = {"Material:Diamond:&cDiamonddd", "Material:Dirt:&f&lDirt", "Material:Redstone:&cRedstone", "Rank:Owner:&4&lOwner", "Material:Bow:&1&lBow"};

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new ChestListener(this), this);
        this.random = new Random();
        this.crateUsesMap = new HashMap<UUID, Integer>();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Error! You cannot execute this command from console!");
            return true;
        }

        Player p = (Player) sender;
        if (cmd.getName().equals("crate") && p.hasPermission("crates.crate")){
            activateCrate(p);
        } else {
            p.sendMessage(ChatColor.RED + "Error! Insufficient permissions! If you believe this is an error please contact a server administrator!");
        }
        return false;
    }

    String choosePrize() {return prizes[this.random.nextInt(prizes.length)];}

    Material[] items = {Material.DIAMOND, Material.DIRT, Material.REDSTONE, Material.NAME_TAG, Material.REDSTONE, Material.BOW};

    public void activateCrate(Player p){
        if (this.crateUsesMap.get(p.getUniqueId()) == null){ this.crateUsesMap.put(p.getUniqueId(), 0); }
        this.crateUsesMap.put(p.getUniqueId(), this.crateUsesMap.get(p.getUniqueId()) + 1);

        Inventory inv = Bukkit.createInventory(null, InventoryType.CHEST, ChatColor.GREEN + "Crate!");
        p.openInventory(inv);
        startInventory(inv, p);
    }

    public void startInventory(final Inventory inv, final Player p){
        startFrame((short) 5, 0L, ChatColor.AQUA, inv, p);
        startFrame((short) 6, 10L, ChatColor.AQUA, inv, p);
        startFrame((short) 7, 15L, ChatColor.AQUA, inv, p);
        startFrame((short) 1, 20L, ChatColor.AQUA, inv, p);
        startFrame((short) 2, 25L, ChatColor.AQUA, inv, p);
        startFrame((short) 3, 30L, ChatColor.AQUA, inv, p);
        startFrame((short) 4, 35L, ChatColor.AQUA, inv, p);
        startFrame((short) 9, 40L, ChatColor.AQUA, inv, p);
        startFrame((short) 10, 45L, ChatColor.AQUA, inv, p);
        selectPrize(p, inv);
    }

    public void startFrame( final short sh, final long delay, final ChatColor chatColor, final Inventory inv, final Player p){
        final Sound sound = Sound.ORB_PICKUP;
        new BukkitRunnable(){
            public void run(){

                for (int x = 0;x<inv.getSize(); x++){
                    inv.setItem(x, new ItemStack(Material.STAINED_GLASS_PANE, 1, sh));
                }

                ItemStack is = new ItemStack(items[random.nextInt(items.length)]);
                ItemMeta im = is.getItemMeta();
                im.setDisplayName(chatColor + "?");
                is.setItemMeta(im);
                inv.setItem(13, is);
                p.playSound(p.getLocation(), sound, 1 ,1 );

                cancel();
            }
        }.runTaskLater(this, delay);
    }

    public void selectPrize( final Player p, final Inventory inv){
        new BukkitRunnable(){
            public void run(){
                String prize = choosePrize();
                String[] prizeIndex = prize.split("\\:");

                if (prize.contains("Rank")){
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "The user " + p.getDisplayName() + " won the rank " + prizeIndex[2] + "!"));
                    p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1 , 1);

                    ItemStack prizeItem =  new ItemStack(Material.NAME_TAG);
                    ItemMeta prizeMeta = prizeItem.getItemMeta();
                    prizeMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "Item: " + prizeIndex[2]));
                    prizeItem.setItemMeta(prizeMeta);
                    inv.setItem(13, prizeItem);
                } else if (prize.contains("Material")) {
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "The user " + p.getDisplayName() + " won the item " + prizeIndex[2] + "!"));
                    p.getInventory().addItem(new ItemStack(Material.valueOf(prizeIndex[1].toUpperCase())));
                    p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1 , 1);

                    ItemStack prizeItem =  new ItemStack(Material.matchMaterial(prizeIndex[1].toUpperCase()));
                    ItemMeta prizeMeta = prizeItem.getItemMeta();
                    prizeMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "Item: " + prizeIndex[2]));
                    prizeItem.setItemMeta(prizeMeta);
                    inv.setItem(13, prizeItem);
                }
            }
        }.runTaskLater(this, 55L);
    }

}