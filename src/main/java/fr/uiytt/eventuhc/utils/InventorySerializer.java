package fr.uiytt.eventuhc.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

//Adapter from here : https://gist.github.com/graywolf336/8153678
public class InventorySerializer {

    /**
     * Transform an Array of {@link ItemStack} into a base64 string.
     * @param content an ItemStack[]
     * @return a string containing the base64, or an empty string if exception.
     */
    public static String contentToBase64(ItemStack[] content) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(content.length);
            for(ItemStack item : content) {
                dataOutput.writeObject(item);
            }
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Transform a String of base64 into an array of {@link ItemStack}
     * @param base64 a string from contentToBase64()
     * @return ItemStack[] or if there is an exception, it returns ItemStack[0]
     */
    public static ItemStack[] base64ToContent(String base64) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(base64));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

            int size = dataInput.readInt();
            ItemStack[] result = new ItemStack[size];
            for(int i=0;i<size;i++) {
                result[i] = (ItemStack) dataInput.readObject();
            }
            dataInput.close();
            return result;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ItemStack[0];
        }
    }
}
