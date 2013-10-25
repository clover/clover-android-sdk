/*
 * Copyright (C) 2013 Clover Network, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.clover.sdk.v1.inventory;

import com.clover.sdk.v1.inventory.CategoryDescription;
import com.clover.sdk.v1.inventory.Discount;
import com.clover.sdk.v1.inventory.Item;
import com.clover.sdk.v1.inventory.Modifier;
import com.clover.sdk.v1.inventory.ModifierGroup;
import com.clover.sdk.v1.inventory.TaxRate;
import com.clover.sdk.v1.ResultStatus;

interface IInventoryService {
  /**
   *  Retrieves all items from the local database; this will return a maximum of 500 items before
   *  returning a fault.  For iterating through the entire item database efficiently, use the
   *  content provider described by {@link com.clover.sdk.v1.inventory.InventoryContract}
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
  List<CategoryDescription> getCategories(out ResultStatus resultStatus);

  /**
   * Adds a new category.
   */
  CategoryDescription createCategory(in CategoryDescription category, out ResultStatus resultStatus);

  /**
   * Updates an existing category.
   */
  void updateCategory(in CategoryDescription category, out ResultStatus resultStatus);

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

  List<Discount> getDiscounts(out ResultStatus resultStatus);
  Discount getDiscount(in String discountId, out ResultStatus resultStatus);
  Discount createDiscount(in Discount discount, out ResultStatus resultStatus);
  void updateDiscount(in Discount discount, out ResultStatus resultStatus);
  void deleteDiscount(in String discountId, out ResultStatus resultStatus);

}
