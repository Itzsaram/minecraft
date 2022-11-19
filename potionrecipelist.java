package quest.quest.list;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BrewingStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import quest.quest.Quest;
import quest.quest.itemlist;
import java.util.ArrayList;
import java.util.HashMap;

public class potionrecipelist {
    public static HashMap<Location,BrewerInventory> stands = new HashMap<>();
    public static Player p = Bukkit.getPlayer("Itzsaram");

    public static ArrayList<ItemStack> top = new ArrayList<>();
    public static ArrayList<ItemStack> under = new ArrayList<>();
    public static ArrayList<ItemStack> value = new ArrayList<>();

    public static void innit()// input "potionrecipelist.innit();" at onEnable
    {
        put(new ItemStack(Material.MUD),new ItemStack(Material.GLASS_BOTTLE),itemlist.mudli);
        put(new ItemStack(Material.TORCH), new ItemStack(Material.SAND), itemlist.sandcrys);
    }



    public static void put(ItemStack ingredient1, ItemStack ingredient2, ItemStack result)
    {
        top.add(ingredient1);
        under.add(ingredient2);
        value.add(result);
    }

    public static void all(BrewingStand st)
    {
        for (int i = 0; i < top.size(); i++)
        {
            if (st.getInventory().contains(top.get(i)) && st.getInventory().contains(under.get(i)) && counts(st,1,1,1,1))
            {
                st.setFuelLevel(st.getFuelLevel()-1);
                stands.put(st.getLocation(),st.getInventory());
                runnable(st,i);
            }
        }
    }

    public static void runnable(BrewingStand st, int i)
    {
        st.setFuelLevel(st.getFuelLevel()-1);
        stands.put(st.getLocation(),st.getInventory());
        new BukkitRunnable()
        {
            int counter = 400;

            @Override
            public void run() {
                if(st.getLocation() == null)
                {
                    this.cancel();
                    return;
                }
                if(contrast(stands.get(st.getLocation()),st.getInventory()))
                {
                    this.cancel();
                    return;
                }
                counter--;

                st.setBrewingTime(counter);
                st.update();
                if(counter == 0)
                {
                    changeitem(st, value.get(i),(ArrayList<Player>) st.getLocation().getNearbyPlayers(5));
                    this.cancel();
                    return;
                }
            }
            @EventHandler
            public void click(InventoryClickEvent click)
            {
                p.sendMessage("asdf");
            }

        }.runTaskTimer(Quest.getPlugin(Quest.class),0,1);
    }


    public static boolean counts(BrewingStand st,int one,int two,int three,int mata)
    {
        if(st.getInventory().getItem(0).getAmount() != one)
        {
            return false;
        }
        if(st.getInventory().getItem(1).getAmount() != two)
        {
            return false;
        }
        if(st.getInventory().getItem(2).getAmount() != three)
        {
            return false;
        }
        if(st.getInventory().getItem(3).getAmount() != mata)
        {
            return false;
        }
        return true;
    }

    public static boolean contrast(BrewerInventory one, BrewerInventory two)
    {
        try {
            if (one.getItem(0).getItemMeta().getDisplayName() != two.getItem(0).getItemMeta().getDisplayName()) {
                return true;
            }
            if (one.getItem(1).getItemMeta().getDisplayName() != two.getItem(1).getItemMeta().getDisplayName()) {
                return true;
            }
            if (one.getItem(2).getItemMeta().getDisplayName() != two.getItem(2).getItemMeta().getDisplayName()) {
                return true;
            }
            if (one.getItem(3).getItemMeta().getDisplayName() != two.getItem(3).getItemMeta().getDisplayName()) {
                return true;
            }
            if(one.getItem(4) == null)
            {
                return false;
            }
            if (one.getItem(4).getItemMeta().getDisplayName() != two.getItem(4).getItemMeta().getDisplayName()) {
                return true;
            }
            return false;
        }
        catch (Exception error)
        {
            return true;
        }
    }

    public static void changeitem(BrewingStand st,ItemStack item,ArrayList<Player> players)
    {
        st.getInventory().setItem(3,new ItemStack(Material.AIR));
        st.getInventory().setItem(0,item);
        st.getInventory().setItem(1,item);
        st.getInventory().setItem(2,item);
        for(Player p : players)
        {
            p.playSound(p.getLocation(), Sound.BLOCK_BREWING_STAND_BREW,1,1);
        }
    }
}
