package quest.quest.system;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BrewingStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import quest.quest.Quest;
import quest.quest.list.potionrecipelist;


public class potionbrewsystem implements Listener { //Bukkit.getPluginManager().registerEvents(new potionbrewsystem(),this);


    @EventHandler
    public void interact(InventoryClickEvent e)
    {
        try {
            Player p = (Player) e.getWhoClicked();
            if (e.getInventory().getType().equals(InventoryType.BREWING) && (e.getClickedInventory() != null) && (e.getClick() == ClickType.LEFT)) {
                if((e.getSlot() == 4) && (e.getSlotType() == InventoryType.SlotType.CRAFTING))
                {
                    return;
                }
                if((e.getSlotType() == InventoryType.SlotType.CONTAINER)|| (e.getSlotType() == InventoryType.SlotType.QUICKBAR))
                {
                    return;
                }
                BrewerInventory inv = (BrewerInventory) e.getInventory();
                ItemStack in = e.getCurrentItem();
                ItemStack cursor = e.getCursor().clone();
                changeitem(e, in, cursor);
                BrewingStand st = (BrewingStand) inv.getLocation().getBlock().getState();
                Bukkit.getScheduler().scheduleSyncDelayedTask(Quest.getPlugin(Quest.class), new Runnable() {
                    @Override
                    public void run() {
                        BrewingStand stand = (BrewingStand) inv.getLocation().getBlock().getState();
                        if (stand.getFuelLevel() > 0 && bottle(stand)) {
                            potionrecipelist.all(stand);
                            stand.update();
                        }
                    }
                }, 1L);

            }
        }
        catch (Exception error)
        {

        }
    }



    public void changeitem(InventoryClickEvent e, ItemStack in, ItemStack cursor)
    {
        if(cursor.getType().equals(Material.AIR))
        {
            return;
        }
        try {
            if (in.getItemMeta().getDisplayName() == cursor.getItemMeta().getDisplayName()) {
                return;
            }
        }
        catch (Exception error)
        {
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(Quest.getPlugin(Quest.class), new Runnable() {
            @Override
            public void run() {
                e.setCursor(in);
                e.getClickedInventory().setItem(e.getSlot(),cursor);
            }
        }, 1L);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Quest.getPlugin(Quest.class), new Runnable() {
            @Override
            public void run() {
                ((Player)e.getView().getPlayer()).updateInventory();
            }
        }, 1L);
        ((Player)e.getView().getPlayer()).updateInventory();
    }

    public static boolean bottle(BrewingStand stand)
    {
        try {
            if ((stand.getInventory().getItem(0).getItemMeta().getDisplayName() == stand.getInventory().getItem(1).getItemMeta().getDisplayName()) && (stand.getInventory().getItem(1).getItemMeta().getDisplayName() == stand.getInventory().getItem(2).getItemMeta().getDisplayName())) {
                return true;
            }
            return false;
        }
        catch (Exception error)
        {
            return false;
        }
    }

}
