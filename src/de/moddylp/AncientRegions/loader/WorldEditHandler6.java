package de.moddylp.AncientRegions.loader;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.EditSessionFactory;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitConfiguration;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.internal.LocalWorldAdapter;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.session.PasteBuilder;
import com.sk89q.worldedit.util.io.Closer;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.registry.WorldData;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.moddylp.AncientRegions.Main;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;
import org.bukkit.entity.Player;

public class WorldEditHandler6 {
    private final Main plugin;

    public WorldEditHandler6(Main plugin) {
        this.plugin = plugin;
    }

    public boolean restoreRegionBlocks(File file, String regionname, Player p, ProtectedRegion region, Vector dimension) {
        Main.getInstance().getLogger().info("Restore in progress...");
        try {
            LocalWorldAdapter world = LocalWorldAdapter.adapt((World)new BukkitWorld(p.getWorld()));
            EditSession editSession = this.plugin.setupWorldEdit().getWorldEdit().getEditSessionFactory().getEditSession((World)world, 999999999);
            Vector origin = new Vector(region.getMinimumPoint().getBlockX(), region.getMinimumPoint().getBlockY(), region.getMinimumPoint().getBlockZ());
            Closer closer = Closer.create();
            FileInputStream fis = (FileInputStream)closer.register((Closeable)new FileInputStream(file));
            BufferedInputStream bis = (BufferedInputStream)closer.register((Closeable)new BufferedInputStream(fis));
            ClipboardReader reader = ClipboardFormat.SCHEMATIC.getReader((InputStream)bis);
            WorldData worldData = world.getWorldData();
            LocalSession session = new LocalSession((LocalConfiguration)this.plugin.setupWorldEdit().getLocalConfiguration());
            Clipboard clipboard = reader.read(worldData);
            clipboard.setOrigin(clipboard.getMinimumPoint());
            ClipboardHolder clipboardHolder = new ClipboardHolder(clipboard, worldData);
            session.setBlockChangeLimit(999999999);
            session.setClipboard(clipboardHolder);
            Operation operation = clipboardHolder.createPaste((Extent)editSession, editSession.getWorld().getWorldData()).to(origin).build();
            Operations.completeLegacy((Operation)operation);
            try {
                closer.close();
            }
            catch (IOException iOException) {
                // empty catch block
            }
            file.delete();
        }
        catch (Exception e) {
            Main.getInstance().getLogger().warning(e.getMessage());
            return false;
        }
        return true;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean saveRegionBlocks(File file, String regionname, Player p, ProtectedRegion region) {
        LocalWorldAdapter world = null;
        if (p.getWorld() != null) {
            world = LocalWorldAdapter.adapt((World)new BukkitWorld(p.getWorld()));
        }
        if (world == null) {
            Main.getInstance().getLogger().warning("Did not save region " + regionname + ", world not found: " + p.getWorld().getName());
            return false;
        }
        EditSession editSession = this.plugin.setupWorldEdit().getWorldEdit().getEditSessionFactory().getEditSession((World)world, 9999999);
        CuboidRegion selection = new CuboidRegion((World)world, (Vector)region.getMinimumPoint(), (Vector)region.getMaximumPoint());
        BlockArrayClipboard clipboard = new BlockArrayClipboard((Region)selection);
        clipboard.setOrigin((Vector)region.getMinimumPoint());
        ForwardExtentCopy copy = new ForwardExtentCopy((Extent)editSession, (Region)new CuboidRegion((World)world, (Vector)region.getMinimumPoint(), (Vector)region.getMaximumPoint()), (Extent)clipboard, (Vector)region.getMinimumPoint());
        try {
            Operations.completeLegacy((Operation)copy);
        }
        catch (MaxChangedBlocksException e1) {
            Main.getInstance().getLogger().warning("Exeeded the block limit while saving schematic of " + regionname);
            return false;
        }
        Closer closer = Closer.create();
        try {
            FileOutputStream fos = (FileOutputStream)closer.register((Closeable)new FileOutputStream(file));
            BufferedOutputStream bos = (BufferedOutputStream)closer.register((Closeable)new BufferedOutputStream(fos));
            ClipboardWriter writer = (ClipboardWriter)closer.register((Closeable)ClipboardFormat.SCHEMATIC.getWriter((OutputStream)bos));
            writer.write((Clipboard)clipboard, world.getWorldData());
            boolean bl = true;
            return bl;
        }
        catch (IOException e) {
            Main.getInstance().getLogger().warning("An error occured while saving schematic of " + regionname + ", " + e.getMessage());
            boolean bos = false;
            return bos;
        }
        finally {
            try {
                closer.close();
            }
            catch (IOException writer) {}
        }
    }
}

