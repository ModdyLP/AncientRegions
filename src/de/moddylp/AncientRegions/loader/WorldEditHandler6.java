package de.moddylp.AncientRegions.loader;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
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
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.util.io.Closer;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.registry.WorldData;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.utils.Console;
import org.bukkit.entity.Player;

import java.io.*;

public class WorldEditHandler6 {
    private final Main plugin;

    public WorldEditHandler6(Main plugin) {
        this.plugin = plugin;
    }

    public boolean restoreRegionBlocks(File file, String regionname, Player p, ProtectedRegion region, Vector dimension) {
        Console.send("Restore in progress...");
        try {
            LocalWorldAdapter world = LocalWorldAdapter.adapt(new BukkitWorld(p.getWorld()));
            EditSession editSession = this.plugin.setupWorldEdit().getWorldEdit().getEditSessionFactory().getEditSession((World) world, 999999999);
            Vector origin = new Vector(region.getMinimumPoint().getBlockX(), region.getMinimumPoint().getBlockY(), region.getMinimumPoint().getBlockZ());
            Closer closer = Closer.create();
            FileInputStream fis = (FileInputStream) closer.register((Closeable) new FileInputStream(file));
            BufferedInputStream bis = (BufferedInputStream) closer.register((Closeable) new BufferedInputStream(fis));
            ClipboardReader reader = ClipboardFormat.SCHEMATIC.getReader(bis);
            WorldData worldData = world.getWorldData();
            LocalSession session = new LocalSession(this.plugin.setupWorldEdit().getLocalConfiguration());
            Clipboard clipboard = reader.read(worldData);
            clipboard.setOrigin(clipboard.getMinimumPoint());
            ClipboardHolder clipboardHolder = new ClipboardHolder(clipboard, worldData);
            session.setBlockChangeLimit(999999999);
            session.setClipboard(clipboardHolder);
            Operation operation = clipboardHolder.createPaste(editSession, editSession.getWorld().getWorldData()).to(origin).build();
            Operations.completeLegacy(operation);
            try {
                closer.close();
            } catch (IOException iOException) {
                // empty catch block
            }
            file.delete();
        } catch (Exception e) {
            Console.error(e.getMessage());
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
            world = LocalWorldAdapter.adapt(new BukkitWorld(p.getWorld()));
        }
        if (world == null) {
            Console.error("Did not save region " + regionname + ", world not found: " + p.getWorld().getName());
            return false;
        }
        EditSession editSession = this.plugin.setupWorldEdit().getWorldEdit().getEditSessionFactory().getEditSession((World) world, 9999999);
        CuboidRegion selection = new CuboidRegion((World) world, region.getMinimumPoint(), region.getMaximumPoint());
        BlockArrayClipboard clipboard = new BlockArrayClipboard(selection);
        clipboard.setOrigin(region.getMinimumPoint());
        ForwardExtentCopy copy = new ForwardExtentCopy(editSession, new CuboidRegion((World) world, region.getMinimumPoint(), region.getMaximumPoint()), clipboard, region.getMinimumPoint());
        try {
            Operations.completeLegacy(copy);
        } catch (MaxChangedBlocksException e1) {
            Console.error("Exeeded the block limit while saving schematic of " + regionname);
            return false;
        }
        Closer closer = Closer.create();
        try {
            FileOutputStream fos = (FileOutputStream) closer.register((Closeable) new FileOutputStream(file));
            BufferedOutputStream bos = (BufferedOutputStream) closer.register((Closeable) new BufferedOutputStream(fos));
            ClipboardWriter writer = (ClipboardWriter) closer.register((Closeable) ClipboardFormat.SCHEMATIC.getWriter(bos));
            writer.write(clipboard, world.getWorldData());
            return true;
        } catch (IOException e) {
            Console.error("An error occured while saving schematic of " + regionname + ", " + e.getMessage());
            return false;
        } finally {
            try {
                closer.close();
            } catch (IOException ignored) {
            }
        }
    }
}

