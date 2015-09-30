package com.clover.sdk.v3.inventory;

import com.clover.sdk.v3.inventory.Attribute;
import com.clover.sdk.v3.inventory.Category;
import com.clover.sdk.v3.inventory.Discount;
import com.clover.sdk.v3.inventory.Item;
import com.clover.sdk.v3.inventory.ItemGroup;
import com.clover.sdk.v3.inventory.Modifier;
import com.clover.sdk.v3.inventory.ModifierGroup;
import com.clover.sdk.v3.inventory.Option;
import com.clover.sdk.v3.inventory.OptionItem;
import com.clover.sdk.v3.inventory.Tag;
import com.clover.sdk.v3.inventory.TaxRate;
import com.clover.sdk.v1.ResultStatus;

interface IInventoryService {
  /**
   *  Retrieves all items from the local database; this will return a maximum of 500 items before
   *  returning a fault.  For iterating through the entire item database efficiently, use the
   *  content provider described by {@link com.clover.sdk.v3.inventory.InventoryContract}
   *  or get a list of item IDs via getItemIds() and retrieve individual items through the getItem() call.
   */
  List<Item> getItems(out ResultStatus resultStatus);

  /**
   * Same as getItems() but returns the list of categories to which each item belongs.
   */
  List<Item> getItemsWithCategories(out ResultStatus resultStatus);

  /**
   *  Retrieves all item IDs from the local database; use this in place of getItems() when the
   *  inventory database is large, and make subsequent calls to getItem() for the full item as necessary.
   */
  List<String> getItemIds(out ResultStatus resultStatus);

  /**
   *  Retrieve an individual item using the item ID. If the item is not in the local database, an
   *  attempt will be made to fetch the item from the server.
   */
  Item getItem(in String itemId, out ResultStatus resultStatus);

  /**
   * Same as getItem() but also includes the list of categories to which the item belongs.
   */
  Item getItemWithCategories(in String itemId, out ResultStatus resultStatus);

  /**
   *  Inserts a new item into the database. If the client is in offline mode, the item will be inserted
   *  into the local cache and a request to create the new item on the server will be queued until
   *  the client is online again.  Returns the newly created item as it exists in the local content
   *  provider/cache.
   */
  Item createItem(in Item item, out ResultStatus resultStatus);

  /**
   *  Updates an existing item.
   *  TODO: Describe what parts of the item are updated through this method
   */
  void updateItem(in Item item, out ResultStatus resultStatus);

  /**
   *  Deletes an existing item.
   */
  void deleteItem(in String itemId, out ResultStatus resultStatus);

  /**
   * Retrieve the list of categories.
   */
  List<Category> getCategories(out ResultStatus resultStatus);

  /**
   * Adds a new category.
   */
  Category createCategory(in Category category, out ResultStatus resultStatus);

  /**
   * Updates an existing category.
   */
  void updateCategory(in Category category, out ResultStatus resultStatus);

  /**
   * Deletes an existing category.
   */
  void deleteCategory(in String categoryId, out ResultStatus resultStatus);

  /**
   * Adds an item to a category.
   */
  void addItemToCategory(in String itemId, in String categoryId, out ResultStatus resultStatus);

  /**
   * Removes an item from a category.
   */
  void removeItemFromCategory(in String itemId, in String categoryId, out ResultStatus resultStatus);

  /**
   * Moves an item's position within an existing category. If 'direction' is negative, the item is moved to the left.
   */
  void moveItemInCategoryLayout(in String itemId, in String categoryId, in int direction, out ResultStatus resultStatus);

  /**
   * Retrieve the list of modifier groups.
   */
  List<ModifierGroup> getModifierGroups(out ResultStatus resultStatus);

  /**
   * Adds a new modifier group.
   */
  ModifierGroup createModifierGroup(in ModifierGroup group, out ResultStatus resultStatus);

  /**
   * Updates an existing modifier group.
   */
  void updateModifierGroup(in ModifierGroup group, out ResultStatus resultStatus);

  /**
   * Deletes an existing modifier group.
   */
  void deleteModifierGroup(in String groupId, out ResultStatus resultStatus);

  /**
   * Associates a modifier group with an item.
   */
  void assignModifierGroupToItem(in String modifierGroupId, in String itemId, out ResultStatus resultStatus);

  /**
   * Removes a modifier group association from an item.
   */
  void removeModifierGroupFromItem(in String modifierGroupId, in String itemId, out ResultStatus resultStatus);

  /**
   * Retrieve the list of modifiers belonging to a modifier group.
   */
  List<Modifier> getModifiers(in String modifierGroupId, out ResultStatus resultStatus);

  /**
   * Adds a new modifier.
   */
  Modifier createModifier(in String modifierGroupId, in Modifier modifier, out ResultStatus resultStatus);

  /**
   * Updates an existing modifier.
   */
  void updateModifier(in Modifier modifier, out ResultStatus resultStatus);

  /**
   * Deletes an existing modifier.
   */
  void deleteModifier(in String modifierId, out ResultStatus resultStatus);

  /**
   * Retrieve the list of tax rates for an item.
   */
  List<TaxRate> getTaxRatesForItem(in String itemId, out ResultStatus resultStatus);

  /**
   * Assign a list of tax rates (identified by their unique ID) to an item.
   */
  void assignTaxRatesToItem(in String itemId, in List<String> taxRates, out ResultStatus resultStatus);

  /**
   * Remove a list of tax rates (identified by their unique ID) from an item.
   */
  void removeTaxRatesFromItem(in String itemId, in List<String> taxRates, out ResultStatus resultStatus);

  /**
   * Gets all defined tax rates for the merchant.
   */
  List<TaxRate> getTaxRates(out ResultStatus resultStatus);

  /**
   * Gets a single tax rate identified by its unique ID.
   */
  TaxRate getTaxRate(in String taxRateId, out ResultStatus resultStatus);

  /**
   * Creates a new tax rate.
   */
  TaxRate createTaxRate(in TaxRate taxRate, out ResultStatus resultStatus);

  /**
   * Updates an existing tax rate.
   */
  void updateTaxRate(in TaxRate taxRate, out ResultStatus resultStatus);

  /**
   * Deletes a tax rate.
   */
  void deleteTaxRate(in String taxRateId, out ResultStatus resultStatus);

  /**
   * Retrieve the list of discounts.
   */
  List<Discount> getDiscounts(out ResultStatus resultStatus);

  /**
   * Gets a single discount identified by its unique ID.
   */
  Discount getDiscount(in String discountId, out ResultStatus resultStatus);

  /**
   * Adds a new discount.
   */
  Discount createDiscount(in Discount discount, out ResultStatus resultStatus);

  /**
   * Updates an existing discount.
   */
  void updateDiscount(in Discount discount, out ResultStatus resultStatus);

  /**
   * Deletes a discount.
   */
  void deleteDiscount(in String discountId, out ResultStatus resultStatus);

  /**
   * Retrieve the list of modifier groups for a particular item.
   */
  List<ModifierGroup> getModifierGroupsForItem(in String itemId, out ResultStatus resultStatus);

  /**
   * Gets all defined tags for the merchant.
   */
  List<com.clover.sdk.v3.inventory.Tag> getTags(out ResultStatus resultStatus);

  /**
   * Gets a single tag identified by its unique ID.
   */
  com.clover.sdk.v3.inventory.Tag getTag(in String tagId, out ResultStatus resultStatus);

  /**
   * Creates a new tag.
   */
  com.clover.sdk.v3.inventory.Tag createTag(in com.clover.sdk.v3.inventory.Tag tag, out ResultStatus resultStatus);

  /**
   * Updates an existing tag.
   */
  void updateTag(in com.clover.sdk.v3.inventory.Tag tag, out ResultStatus resultStatus);

  /**
   * Deletes a tag.
   */
  void deleteTag(in String tagId, out ResultStatus resultStatus);

  /**
   * Retrieve the list of tags for an item.
   */
  List<com.clover.sdk.v3.inventory.Tag> getTagsForItem(in String itemId, out ResultStatus resultStatus);

  /**
   * Assign a list of tags (identified by their unique ID) to an item.
   */
  void assignTagsToItem(in String itemId, in List<String> tags, out ResultStatus resultStatus);

  /**
   * Remove a list of tags (identified by their unique ID) from an item.
   */
  void removeTagsFromItem(in String itemId, in List<String> tags, out ResultStatus resultStatus);

  /**
   * Retrieve the list of tags for a printer.
   */
  List<com.clover.sdk.v3.inventory.Tag> getTagsForPrinter(in String printerMac, out ResultStatus resultStatus);

  /**
   * Assign a list of tags (identified by their unique ID) to a printer.
   */
  void assignTagsToPrinter(in String printerUid, in List<String> tags, out ResultStatus resultStatus);

  /**
   * Remove a list of tags (identified by their unique ID) from a printer.
   */
  void removeTagsFromPrinter(in String printerUid, in List<String> tags, out ResultStatus resultStatus);

  /**
   * Assign a list of items (identified by their unique ID) to a tag.
   */
  void assignItemsToTag(in String tagId, in List<String> items, out ResultStatus resultStatus);

  /**
   * Remove a list of items (identified by their unique ID) from a tag.
   */
  void removeItemsFromTag(in String tagId, in List<String> items, out ResultStatus resultStatus);

  /**
   * Update modifier sort order for a modifier group.
   */
  void updateModifierSortOrder(in String modifierGroupId, in List<String> modifierIds, out ResultStatus resultStatus);

  /**
   * Update stock count for an item. This is the old way of updating stock that takes a long, the new way is
   * updateItemStockQuantity and takes a double.
   */
  void updateItemStock(in String itemId, in long stockCount, out ResultStatus resultStatus);

  /**
   * Remove stock count for an item.
   */
  void removeItemStock(in String itemId, out ResultStatus resultStatus);

  /**
   * Gets all defined attributes for the merchant.
   */
  List<com.clover.sdk.v3.inventory.Attribute> getAttributes(out ResultStatus resultStatus);

  /**
   * Gets a single attribute identified by its unique ID.
   */
  com.clover.sdk.v3.inventory.Attribute getAttribute(in String attributeId, out ResultStatus resultStatus);

  /**
   * Creates a new attribute.
   */
  com.clover.sdk.v3.inventory.Attribute createAttribute(in com.clover.sdk.v3.inventory.Attribute attribute, out ResultStatus resultStatus);

  /**
   * Updates an existing attribute.
   */
  void updateAttribute(in com.clover.sdk.v3.inventory.Attribute attribute, out ResultStatus resultStatus);

  /**
   * Deletes an attribute, deletes all the options in that attribute and removes all the associations between those options and items.
   */
  void deleteAttribute(in String attributeId, out ResultStatus resultStatus);

  /**
   * Gets all defined options for the merchant.
   */
  List<com.clover.sdk.v3.inventory.Option> getOptions(out ResultStatus resultStatus);

  /**
   * Gets a single option identified by its unique ID.
   */
  com.clover.sdk.v3.inventory.Option getOption(in String optionId, out ResultStatus resultStatus);

  /**
   * Creates a new option.
   */
  com.clover.sdk.v3.inventory.Option createOption(in com.clover.sdk.v3.inventory.Option option, out ResultStatus resultStatus);

  /**
   * Updates an existing option.
   */
  void updateOption(in com.clover.sdk.v3.inventory.Option option, out ResultStatus resultStatus);

  /**
   * Deletes an option and removes all the associations between that option and items.
   */
  void deleteOption(in String optionId, out ResultStatus resultStatus);

  /**
   * Retrieve the list of options for an item.
   */
  List<com.clover.sdk.v3.inventory.Option> getOptionsForItem(in String itemId, out ResultStatus resultStatus);

  /**
   * Associate the given options with an item.
   */
  void assignOptionsToItem(in String itemId, in List<String> optionIds, out ResultStatus resultStatus);

  /**
   * Remove the association between the given options and an item.
   */
  void removeOptionsFromItem(in String itemId, in List<String> optionIds, out ResultStatus resultStatus);

  /**
   * Gets a single item group identified by its unique ID.
   */
  com.clover.sdk.v3.inventory.ItemGroup getItemGroup(in String itemGroupId, out ResultStatus resultStatus);

  /**
   * Creates a new item group.
   */
  com.clover.sdk.v3.inventory.ItemGroup createItemGroup(in com.clover.sdk.v3.inventory.ItemGroup itemGroup, out ResultStatus resultStatus);

  /**
   * Updates an existing item group.
   */
  void updateItemGroup(in com.clover.sdk.v3.inventory.ItemGroup itemGroup, out ResultStatus resultStatus);

  /**
   * Deletes an item group, but does not delete the items in a group, they become items without an item group.
   */
  void deleteItemGroup(in String itemGroupId, out ResultStatus resultStatus);

  /**
   * Update stock for an item.
   */
  void updateItemStockQuantity(in String itemId, in double quantity, out ResultStatus resultStatus);

}
