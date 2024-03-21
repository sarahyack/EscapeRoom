package models;

/**
 * Interface for observing inventory changes. When notified, the observer updates the inventory display.
 */
public interface InventoryObserver {
	void inventoryChanged(Player player);
}
